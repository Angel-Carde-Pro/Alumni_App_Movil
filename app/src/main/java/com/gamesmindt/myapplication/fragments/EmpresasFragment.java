package com.gamesmindt.myapplication.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gamesmindt.myapplication.Model.Empresa;
import com.gamesmindt.myapplication.Model.EmpresaDTO;
import com.gamesmindt.myapplication.Model.OfertaLaboralDTO;
import com.gamesmindt.myapplication.R;
import com.gamesmindt.myapplication.Services.EmpresarioServiceApi;
import com.gamesmindt.myapplication.activities.DetallePostuladoActivity;
import com.gamesmindt.myapplication.activities.MainActivity;
import com.gamesmindt.myapplication.adapters.EmpresasAdapter;
import com.gamesmindt.myapplication.adapters.Job_EnterpriseAdapter;
import com.gamesmindt.myapplication.bd.DBHelper;
import com.gamesmindt.myapplication.bd.Utilidades;
import com.gamesmindt.myapplication.configs.LoadingDialog;
import com.gamesmindt.myapplication.configs.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmpresasFragment extends Fragment {
    private RecyclerView recyclerView;

    private EmpresasAdapter empresasAdapter;
    private List<EmpresaDTO> dtoList;

    private LoadingDialog loadingDialog;
    private SearchView searchView;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
         loadingDialog = new LoadingDialog(requireActivity());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Obtener los datos persistentes
        SharedPreferences preferences = getContext().getSharedPreferences("user_data", MODE_PRIVATE);
        String usuario = preferences.getString("username", "");
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


        if ("EMPRESARIO".equals(roles)){
            if (DBHelper.hayConexionInternet(getContext())){
                EmpresarioServiceApi serviceApi = RetrofitClient.getRetrofitInstance().create(EmpresarioServiceApi.class);
                Call<List<EmpresaDTO>> call = serviceApi.getEmpresaByUsuario(usuario, "Bearer "+token);

                call.enqueue(new Callback<List<EmpresaDTO>>() {
                    @Override
                    public void onResponse(Call<List<EmpresaDTO>> call, Response<List<EmpresaDTO>> response) {
                        System.out.println("RESPUESTA ORIGINAL" + response.body());
                        DBHelper dbHelper = new DBHelper(getContext());
                        SQLiteDatabase db = dbHelper.getWritableDatabase();

                        if (response.isSuccessful()){
                            dtoList = response.body();
                            System.out.println("listaOfertas.get(0).getOtherFields() = " + dtoList.get(0).getArea());
                            // Verificar si la tabla está vacía
                            if (isTableEmpty(db, Utilidades.TABLE_NAME)) {
                                for (EmpresaDTO empresaDTO : dtoList) {
                                    ContentValues values = new ContentValues();
                                    values.put(Utilidades.COLUMN_EMPRESARIO, empresaDTO.getEmpresario());
                                    values.put(Utilidades.COLUMN_RUC, empresaDTO.getRuc());
                                    values.put(Utilidades.COLUMN_RAZON_SOCIAL, empresaDTO.getRazonSocial());
                                    values.put(Utilidades.COLUMN_NOMBRE, empresaDTO.getNombre());
                                    db.insert(Utilidades.TABLE_NAME, null, values);
                                }
                            } else {
                                for (EmpresaDTO empresaDTO : dtoList) {
                                    // Construye la cláusula WHERE para buscar registros con los mismos valores
                                    String selection = Utilidades.COLUMN_EMPRESARIO + " = ? AND " +
                                            Utilidades.COLUMN_RUC + " = ? AND " +
                                            Utilidades.COLUMN_RAZON_SOCIAL + " = ? AND " +
                                            Utilidades.COLUMN_NOMBRE + " = ?";
                                    String[] selectionArgs = {
                                            empresaDTO.getEmpresario(),
                                            empresaDTO.getRuc(),
                                            empresaDTO.getRazonSocial(),
                                            empresaDTO.getNombre()
                                    };

                                    // Ejecuta la consulta para verificar si existe un registro con los mismos valores
                                    Cursor cursor = db.query(Utilidades.TABLE_NAME, null, selection, selectionArgs, null, null, null);

                                    // Si no hay registros duplicados, procede con la inserción
                                    if (cursor.getCount() == 0) {
                                        ContentValues values = new ContentValues();
                                        values.put(Utilidades.COLUMN_EMPRESARIO, empresaDTO.getEmpresario());
                                        values.put(Utilidades.COLUMN_RUC, empresaDTO.getRuc());
                                        values.put(Utilidades.COLUMN_RAZON_SOCIAL, empresaDTO.getRazonSocial());
                                        values.put(Utilidades.COLUMN_NOMBRE, empresaDTO.getNombre());
                                        db.insert(Utilidades.TABLE_NAME, null, values);
                                    }

                                    cursor.close();
                                }
                            }
                            empresasAdapter = new EmpresasAdapter(getContext(), dtoList);
                            recyclerView.setAdapter(empresasAdapter);

                        }else {
                            System.out.println("No se encontraron Trabajos .");
                            Toast.makeText(getContext(), "No se encontraron Trabajos", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<List<EmpresaDTO>> call, Throwable t) {
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

                String selection = Utilidades.COLUMN_EMPRESARIO + " = ?";
                String[] selectionArgs = { usuario };

                Cursor cursor = db.query(Utilidades.TABLE_NAME, null, selection, selectionArgs, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    dtoList = new ArrayList<>();
                    do {
                        // Leer los datos del cursor y crear objetos EmpresaDTO
                        int empresarioIndex = cursor.getColumnIndex(Utilidades.COLUMN_EMPRESARIO);
                        int rucIndex = cursor.getColumnIndex(Utilidades.COLUMN_RUC);
                        int razonSocialIndex = cursor.getColumnIndex(Utilidades.COLUMN_RAZON_SOCIAL);
                        int nombreIndex = cursor.getColumnIndex(Utilidades.COLUMN_NOMBRE);


                        String empresario = cursor.getString(empresarioIndex);
                        String ruc = cursor.getString(rucIndex);
                        String razonSocial = cursor.getString(razonSocialIndex);
                        String nombre = cursor.getString(nombreIndex);
                        System.out.println(razonSocial+ruc+empresario + "EMPRESARIOOOOOOOOOOOOOO");

                        EmpresaDTO empresaDTO = new EmpresaDTO(1, empresario, null, null, ruc, nombre,null, null,null, null, null, null); // Reemplaza esto con el constructor real de EmpresaDTO
                        dtoList.add(empresaDTO);
                    } while (cursor.moveToNext());

                    cursor.close();

                    // Configurar el adaptador y mostrar los datos en el RecyclerView
                    empresasAdapter = new EmpresasAdapter(getContext(), dtoList);
                    recyclerView.setAdapter(empresasAdapter);
                } else {
                    loadingDialog.showFailureDialog("No tiene Conexion", "No tiene datos guardados localmente");
                }
            }

        }
        return view;
    }

   private void filter(String text) {
        List<EmpresaDTO> filteredList = new ArrayList<>();

        for (EmpresaDTO empresa : dtoList) {
            // Aquí puedes ajustar la lógica de filtrado según tus necesidades
            if (empresa.getNombre().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(empresa);
            }
        }

        empresasAdapter.filterList(filteredList);
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
