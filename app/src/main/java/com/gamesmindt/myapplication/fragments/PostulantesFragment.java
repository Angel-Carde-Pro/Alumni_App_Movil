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
import android.widget.SearchView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gamesmindt.myapplication.Model.Empresa;
import com.gamesmindt.myapplication.Model.EmpresaDTO;
import com.gamesmindt.myapplication.Model.Graduado;
import com.gamesmindt.myapplication.Model.OfertaLaboralDTO;
import com.gamesmindt.myapplication.Model.Persona;
import com.gamesmindt.myapplication.Model.Usuario;
import com.gamesmindt.myapplication.R;
import com.gamesmindt.myapplication.Services.EmpresarioServiceApi;
import com.gamesmindt.myapplication.Services.OfertasServiceApi;
import com.gamesmindt.myapplication.adapters.EmpresasAdapter;
import com.gamesmindt.myapplication.adapters.OfertasAdapter;
import com.gamesmindt.myapplication.adapters.PostulantesAdapter;
import com.gamesmindt.myapplication.bd.DBHelper;
import com.gamesmindt.myapplication.bd.GraduadoTable;
import com.gamesmindt.myapplication.bd.Utilidades;
import com.gamesmindt.myapplication.configs.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostulantesFragment extends Fragment {
    private RecyclerView recyclerView;
    private PostulantesAdapter postulantesAdapter;
    private List<Graduado> graduadoList;
    private SearchView searchView;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Obtener los datos persistentes
        SharedPreferences preferences = getActivity().getSharedPreferences("user_data", MODE_PRIVATE);
        String ofertaLaboralid = preferences.getString("ofertaLaboralid", "");
        String roles = preferences.getString("userRoles", "");
        String token = preferences.getString("accessToken", "");

        System.out.println(roles + "AAAAAAAAAAAAAAAAAAAAAA");

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
            if (DBHelper.hayConexionInternet(getContext())) {
                EmpresarioServiceApi empresa = RetrofitClient.getRetrofitInstance().create(EmpresarioServiceApi.class);
                Call<List<Graduado>> call = empresa.getGraduadosByOfertaId(ofertaLaboralid, "Bearer " + token);
                System.out.println("empresa.getGraduadosByOfertaId" + ofertaLaboralid);
                call.enqueue(new Callback<List<Graduado>>() {
                    @Override
                    public void onResponse(Call<List<Graduado>> call, Response<List<Graduado>> response) {
                        System.out.println("RESPUESTA ORIGINAL" + response.body());
                        DBHelper dbHelper = new DBHelper(getContext());
                        SQLiteDatabase db = dbHelper.getWritableDatabase();

                        try {
                            if (response.isSuccessful()) {
                                graduadoList = response.body();

                                if (isTableEmpty(db, GraduadoTable.TABLE_NAME)) {
                                    for (Graduado graduado : graduadoList) {
                                        ContentValues values = new ContentValues();
                                        values.put(GraduadoTable.COLUMN_ID_GRADUADO, graduado.getId());
                                        values.put(GraduadoTable.COLUMN_ID_USER_GRADUADO, graduado.getUsuario().getId());
                                        values.put(GraduadoTable.COLUMN_USUARIO_GRADUADO, graduado.getUsuario().getNombreUsuario());
                                        values.put(GraduadoTable.COLUMN_PRIMER_NOMBRE, graduado.getUsuario().getPersona().getPrimerNombre());
                                        values.put(GraduadoTable.COLUMN_SEGUNDO_NOMBRE_GRADUADO, graduado.getUsuario().getPersona().getSegundoNombre());
                                        values.put(GraduadoTable.COLUMN_PRIMER_APELLIDO_GRADUADO, graduado.getUsuario().getPersona().getApellidoMaterno());
                                        values.put(GraduadoTable.COLUMN_TELEFONO_GRADUADO, graduado.getUsuario().getPersona().getTelefono());
                                        values.put(GraduadoTable.COLUMN_ESTADO_GRADUADO, graduado.getEstadoCivil());
                                        values.put(GraduadoTable.COLUMN_MAIL_PERSONAL, graduado.getEmailPersonal());

                                        System.out.println(graduado.getUsuario().getPersona().getPrimerNombre() + "GraduadoTable.COLUMN_PRIMER_NOMBRE VACIO");

                                        values.put(GraduadoTable.COLUMN_OFERTA_ID, ofertaLaboralid);

                                        db.insert(GraduadoTable.TABLE_NAME, null, values);
                                    }
                                } else {
                                    for (Graduado graduado : graduadoList) {
                                        // Construye la cláusula WHERE para buscar registros con los mismos valores
                                        String selection = GraduadoTable.COLUMN_MAIL_PERSONAL + " = ? AND " +
                                                GraduadoTable.COLUMN_PRIMER_NOMBRE + " = ? AND " +
                                                GraduadoTable.COLUMN_ID_GRADUADO + " = ?";
                                        String[] selectionArgs = {
                                                graduado.getEmailPersonal(),
                                                graduado.getUsuario().getPersona().getPrimerNombre(),
                                                String.valueOf(graduado.getId())
                                        };

                                        // Ejecuta la consulta para verificar si existe un registro con los mismos valores
                                        Cursor cursor = db.query(GraduadoTable.TABLE_NAME, null, selection, selectionArgs, null, null, null);

                                        // Si no hay registros duplicados, procede con la inserción
                                        if (cursor.getCount() == 0) {
                                            ContentValues values = new ContentValues();
                                            values.put(GraduadoTable.COLUMN_ID_GRADUADO, graduado.getId());
                                            values.put(GraduadoTable.COLUMN_ID_USER_GRADUADO, graduado.getUsuario().getId());
                                            values.put(GraduadoTable.COLUMN_USUARIO_GRADUADO, graduado.getUsuario().getNombreUsuario());
                                            values.put(GraduadoTable.COLUMN_PRIMER_NOMBRE, graduado.getUsuario().getPersona().getPrimerNombre());
                                            values.put(GraduadoTable.COLUMN_SEGUNDO_NOMBRE_GRADUADO, graduado.getUsuario().getPersona().getSegundoNombre());
                                            values.put(GraduadoTable.COLUMN_PRIMER_APELLIDO_GRADUADO, graduado.getUsuario().getPersona().getApellidoMaterno());
                                            values.put(GraduadoTable.COLUMN_TELEFONO_GRADUADO, graduado.getUsuario().getPersona().getTelefono());
                                            values.put(GraduadoTable.COLUMN_ESTADO_GRADUADO, graduado.getEstadoCivil());
                                            values.put(GraduadoTable.COLUMN_MAIL_PERSONAL, graduado.getEmailPersonal());
                                            values.put(GraduadoTable.COLUMN_OFERTA_ID, ofertaLaboralid);
                                            db.insert(GraduadoTable.TABLE_NAME, null, values);
                                        }

                                        cursor.close();
                                    }
                                }
                                postulantesAdapter = new PostulantesAdapter(getContext(), graduadoList);
                                recyclerView.setAdapter(postulantesAdapter);

                            } else {
                                System.out.println("No se encontraron graduados.");
                                Toast.makeText(getContext(), "No se encontraron ofertas", Toast.LENGTH_SHORT).show();

                            }
                        } catch (Exception e) {
                            System.out.println(e.getStackTrace() + "Error Urgente");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Graduado>> call, Throwable t) {
                        System.out.println("Error de conexión: " + t.getMessage());
                        Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
                        // Manejar el caso de fallo en la conexión
                        t.printStackTrace();
                    }
                });
            }else {
                // Si no hay conexión a Internet, recuperar los datos de la base de datos local
                DBHelper dbHelper = new DBHelper(getContext());
                SQLiteDatabase db = dbHelper.getReadableDatabase();

                String selection = GraduadoTable.COLUMN_OFERTA_ID + " = ?";
                String[] selectionArgs = { ofertaLaboralid };

                Cursor cursor = db.query(GraduadoTable.TABLE_NAME, null, selection, selectionArgs, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    graduadoList = new ArrayList<>();
                    do {
                        // Leer los datos del cursor y crear objetos EmpresaDTO
                        int idgraduadoIndex = cursor.getColumnIndex(GraduadoTable.COLUMN_ID_GRADUADO);
                        int idusergraduadoIndex = cursor.getColumnIndex(GraduadoTable.COLUMN_ID_USER_GRADUADO);
                        int usuarioIndex = cursor.getColumnIndex(GraduadoTable.COLUMN_USUARIO_GRADUADO);
                        int primernombreIndex = cursor.getColumnIndex(GraduadoTable.COLUMN_PRIMER_NOMBRE);
                        int segundonombreIndex = cursor.getColumnIndex(GraduadoTable.COLUMN_SEGUNDO_NOMBRE_GRADUADO);
                        int primerapellidoIndex = cursor.getColumnIndex(GraduadoTable.COLUMN_PRIMER_APELLIDO_GRADUADO);
                        int mailPersonalIndex = cursor.getColumnIndex(GraduadoTable.COLUMN_MAIL_PERSONAL);
                        int ofertaIDIndex = cursor.getColumnIndex(GraduadoTable.COLUMN_OFERTA_ID);
                        int estadoIndex = cursor.getColumnIndex(GraduadoTable.COLUMN_ESTADO_GRADUADO);


                        System.out.println(primernombreIndex + "GraduadoTable.COLUMN_PRIMER_NOMBRE OBTENER");



                        int id = cursor.getInt(idgraduadoIndex);
                        int graduadoid = cursor.getInt(idusergraduadoIndex);
                        String usuario = cursor.getString(usuarioIndex);
                        String segundonombre = cursor.getString(segundonombreIndex);
                        String apellido = cursor.getString(primerapellidoIndex);
                        String estado = cursor.getString(estadoIndex);
                        String primernombre = cursor.getString(primernombreIndex);
                        String mail = cursor.getString(mailPersonalIndex);
                        int ofertaid = cursor.getInt(ofertaIDIndex);

                        System.out.println(primernombre + "GraduadoTable.COLUMN_PRIMER_NOMBRE OBTENER");

                        Usuario user = new Usuario();
                        Persona persona = new Persona();
                        persona.setPrimerNombre(primernombre);
                        persona.setSegundoNombre(segundonombre);
                        persona.setApellidoMaterno(apellido);

                        user.setPersona(persona);
                        user.setId(graduadoid);
                        user.setNombreUsuario(usuario);


                        Graduado graduado = new Graduado(id , user,null, null, mail, estado, null,null); // Reemplaza esto con el constructor real de EmpresaDTO
                        graduadoList.add(graduado);
                    } while (cursor.moveToNext());

                    cursor.close();
                    dbHelper.close();

                    // Configurar el adaptador y mostrar los datos en el RecyclerView
                    postulantesAdapter = new PostulantesAdapter(getContext(), graduadoList);
                    recyclerView.setAdapter(postulantesAdapter);
                } else {
                    //loadingDialog.showFailureDialog("No tiene Conexion", "No tiene datos guardados localmente");
                }

            }
        }
        return view;
    }

    private void filter(String text) {
        List<Graduado> filteredList = new ArrayList<>();

        for (Graduado oferta : graduadoList) {
            // Aquí puedes ajustar la lógica de filtrado según tus necesidades
            if (oferta.getUsuario().getPersona().getSegundoNombre().toLowerCase().contains(text.toLowerCase())||
                    oferta.getUsuario().getPersona().getPrimerNombre().toLowerCase().contains(text.toLowerCase())||
                    oferta.getUsuario().getPersona().getApellidoPaterno().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(oferta);
            }
        }

        postulantesAdapter.filterList(filteredList);
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