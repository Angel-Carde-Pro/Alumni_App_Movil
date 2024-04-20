package com.gamesmindt.myapplication.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gamesmindt.myapplication.Model.EmpresaDTO;
import com.gamesmindt.myapplication.Model.EmpresaDetalle;
import com.gamesmindt.myapplication.Model.OfertaDetalle;
import com.gamesmindt.myapplication.Model.OfertaLaboralDTO;
import com.gamesmindt.myapplication.R;
import com.gamesmindt.myapplication.Services.EmpresarioServiceApi;
import com.gamesmindt.myapplication.adapters.EmpresasAdapter;
import com.gamesmindt.myapplication.adapters.Job_EnterpriseAdapter;
import com.gamesmindt.myapplication.bd.DBHelper;
import com.gamesmindt.myapplication.bd.JobTable;
import com.gamesmindt.myapplication.bd.Utilidades;
import com.gamesmindt.myapplication.configs.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Job_EnterpriseFragment extends Fragment {
    private RecyclerView recyclerView;

    private Job_EnterpriseAdapter JobAdapter;

    private List<OfertaDetalle> dtoList;

    private SearchView searchView;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Obtener los datos persistentes
        SharedPreferences preferences = getContext().getSharedPreferences("user_data", MODE_PRIVATE);
        String empresa = preferences.getString("nombreempresa", "");
        String roles = preferences.getString("userRoles", "");
        String token = preferences.getString("accessToken", "");

        //Preparar el reciclerView
        View view = inflater.inflate(R.layout.fragment_listar_ofertas, container, false);
        recyclerView = view.findViewById(R.id.listRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //Preparar Buscador
        searchView = view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });


        if ("EMPRESARIO".equals(roles)) {
            if (DBHelper.hayConexionInternet(getContext())){

                EmpresarioServiceApi serviceApi = RetrofitClient.getRetrofitInstance().create(EmpresarioServiceApi.class);
                Call<List<OfertaDetalle>> call = serviceApi.getOfertasByEmpresa(empresa, "Bearer " + token);

            call.enqueue(new Callback<List<OfertaDetalle>>() {
                @Override
                public void onResponse(Call<List<OfertaDetalle>> call, Response<List<OfertaDetalle>> response) {
                    System.out.println("RESPUESTA ORIGINAL" + response.body());
                       DBHelper dbHelper = new DBHelper(getContext());
                       SQLiteDatabase db = dbHelper.getWritableDatabase();

                       if (response.isSuccessful()) {
                           dtoList = response.body();
                           if (isTableEmpty(db, Utilidades.TABLE_NAME)) {
                               for (OfertaDetalle ofertaLaboralDTO : dtoList) {
                                   ContentValues values = new ContentValues();
                                   values.put(JobTable.COLUMN_ID_JOB, ofertaLaboralDTO.getId());
                                   values.put(JobTable.COLUMN_NOMBRE_EMPRESA, ofertaLaboralDTO.getEmpresa().getNombre());
                                   values.put(JobTable.COLUMN_CARGO_EMPRESA, ofertaLaboralDTO.getCargo());
                                   db.insert(Utilidades.TABLE_NAME, null, values);
                               }
                           } else {
                               for (OfertaDetalle ofertaLaboralDTO : dtoList) {
                                   // Construye la cláusula WHERE para buscar registros con los mismos valores
                                   String selection = JobTable.COLUMN_CARGO_EMPRESA + " = ? AND " +
                                           JobTable.COLUMN_NOMBRE_EMPRESA + " = ? AND " +
                                           JobTable.COLUMN_ID_JOB + " = ?";
                                   String[] selectionArgs = {
                                           ofertaLaboralDTO.getCargo(),
                                   };

                                   // Ejecuta la consulta para verificar si existe un registro con los mismos valores
                                   Cursor cursor = db.query(JobTable.TABLE_NAME, null, selection, selectionArgs, null, null, null);

                                   // Si no hay registros duplicados, procede con la inserción
                                   if (cursor.getCount() == 0) {
                                       ContentValues values = new ContentValues();
                                       values.put(JobTable.COLUMN_ID_JOB, ofertaLaboralDTO.getId());
                                       values.put(JobTable.COLUMN_NOMBRE_EMPRESA, ofertaLaboralDTO.getEmpresa().getNombre());
                                       values.put(JobTable.COLUMN_CARGO_EMPRESA, ofertaLaboralDTO.getCargo());
                                       db.insert(JobTable.TABLE_NAME, null, values);
                                   }

                                   cursor.close();
                               }
                           }
                           JobAdapter = new Job_EnterpriseAdapter(getContext(), dtoList);
                           recyclerView.setAdapter(JobAdapter);

                       } else {
                           System.out.println("No se encontraron Trabajos .");
                           Toast.makeText(getContext(), "No se encontraron Trabajos", Toast.LENGTH_SHORT).show();

                       }
                }

                @Override
                public void onFailure(Call<List<OfertaDetalle>> call, Throwable t) {
                    System.out.println(R.string.error_conexion + t.getMessage());
                    Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
                    // Manejar el caso de fallo en la conexión
                    t.printStackTrace();
                }
            });
        }else {
                // Si no hay conexión a Internet, recuperar los datos de la base de datos local
                DBHelper dbHelper = new DBHelper(getContext());
                SQLiteDatabase db = dbHelper.getReadableDatabase();

                String selection = JobTable.COLUMN_NOMBRE_EMPRESA + " = ?";
                String[] selectionArgs = { empresa };

                Cursor cursor = db.query(JobTable.TABLE_NAME, null, selection, selectionArgs, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    dtoList = new ArrayList<>();
                    do {
                        // Leer los datos del cursor y crear objetos EmpresaDTO
                        int empresaIndex = cursor.getColumnIndex(JobTable.COLUMN_NOMBRE_EMPRESA);
                        int cargoIndex = cursor.getColumnIndex(JobTable.COLUMN_CARGO_EMPRESA);
                        int idIndex = cursor.getColumnIndex(JobTable.COLUMN_ID_JOB);


                        String empresas = cursor.getString(empresaIndex);
                        String cargo = cursor.getString(cargoIndex);
                        int id = cursor.getInt(idIndex);

                        EmpresaDetalle empresaDetalle = new EmpresaDetalle();
                        empresaDetalle.setNombre(empresas);

                        OfertaDetalle ofertaLaboralDTO = new OfertaDetalle(id, '0',null, null, cargo, null, null,null, null,null, empresaDetalle, null, null); // Reemplaza esto con el constructor real de EmpresaDTO
                        dtoList.add(ofertaLaboralDTO);
                    } while (cursor.moveToNext());

                    cursor.close();

                    // Configurar el adaptador y mostrar los datos en el RecyclerView
                    JobAdapter = new Job_EnterpriseAdapter(getContext(), dtoList);
                    recyclerView.setAdapter(JobAdapter);
                } else {
                    //loadingDialog.showFailureDialog("No tiene Conexion", "No tiene datos guardados localmente");
                }


            }
        }
        return view;
    }

    private void filter(String text) {
        List<OfertaDetalle> filteredList = new ArrayList<>();

        for (OfertaDetalle oferta : dtoList) {
            // Aquí puedes ajustar la lógica de filtrado según tus necesidades
            if (oferta.getCargo().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(oferta);
            }
        }

        JobAdapter.filterList(filteredList);
    }

    public void onDestroy() {
        super.onDestroy();
    }
    private boolean isTableEmpty(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + tableName, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count == 0;
    }
}
