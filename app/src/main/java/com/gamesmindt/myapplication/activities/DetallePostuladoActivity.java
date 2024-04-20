package com.gamesmindt.myapplication.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.gamesmindt.myapplication.Model.Graduado;
import com.gamesmindt.myapplication.Model.GraduadoDTO;
import com.gamesmindt.myapplication.Model.Persona;
import com.gamesmindt.myapplication.Model.Usuario;
import com.gamesmindt.myapplication.R;
import com.gamesmindt.myapplication.Services.GraduadoServiceApi;
import com.gamesmindt.myapplication.Services.UserServiceApi;
import com.gamesmindt.myapplication.adapters.PostulantesAdapter;
import com.gamesmindt.myapplication.bd.DBHelper;
import com.gamesmindt.myapplication.bd.GraduadoTable;
import com.gamesmindt.myapplication.configs.LoadingDialog;
import com.gamesmindt.myapplication.configs.RetrofitClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetallePostuladoActivity extends AppCompatActivity {

    GraduadoServiceApi graduadoServiceApi = RetrofitClient.getRetrofitInstance().create(GraduadoServiceApi.class);
    UserServiceApi userServiceApi = RetrofitClient.getRetrofitInstance().create(UserServiceApi.class);
    private Usuario graduado;
    private GraduadoDTO graduadoDTO;
    Window window = null;
    SharedPreferences preferences;
    String usuario = "";
    String roles = "";
    String accessToken = "";

    Long usuarioId = 1L;
    LoadingDialog loadingDialog = new LoadingDialog(DetallePostuladoActivity.this);
    private TextView usuario_txt, nombre_txt, nombre2_txt, apellido_txt, telefono_txt, estado_txt;
    private ImageView postulado_img;

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userinfo);
        window = getWindow();


        preferences = DetallePostuladoActivity.this.getSharedPreferences("user_data", MODE_PRIVATE);
        usuario = preferences.getString("username", "");
        roles = preferences.getString("userRoles", "");
        accessToken = preferences.getString("accessToken", "");
        String usuarioIdString = preferences.getString("idpostulante", "1"); // Obtener como cadena
        Long usuarioId = Long.parseLong(usuarioIdString); // Convertir a Long

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.black));

        usuario_txt = findViewById(R.id.Text_UsuarioInfo);
        nombre_txt = findViewById(R.id.Text_NombreInfo);
        nombre2_txt = findViewById(R.id.Text_NombreSegundoInfo);
        apellido_txt = findViewById(R.id.Text_ApellidoInfo);
        telefono_txt = findViewById(R.id.Text_TelefonoInfo);
        estado_txt = findViewById(R.id.Text_EstadoInfo);
        button = findViewById(R.id.verpdf_txt);

        postulado_img = findViewById(R.id.profile_imagePostuladopInfo);


        Intent intent = getIntent();
        Long id_Oferta = intent.getLongExtra("id_Oferta", -1);

        if (DBHelper.hayConexionInternet(DetallePostuladoActivity.this)) {

            // Llamar a la API para obtener las ofertas laborales
        Call<GraduadoDTO> call = graduadoServiceApi.getGraduadoDTOById(usuarioId, "Bearer " + accessToken);
        call.enqueue(new Callback<GraduadoDTO>() {
            @Override
            public void onResponse(Call<GraduadoDTO> call, Response<GraduadoDTO> response) {
                if (response.isSuccessful()) {
                    graduadoDTO = response.body();


                    Call<Usuario> callback = userServiceApi.getUserByUsername(graduadoDTO.getUsuario(), "Bearer " + accessToken);
                    callback.enqueue(new Callback<Usuario>() {
                        @Override
                        public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                            if (response.isSuccessful()) {
                                graduado = response.body();
                                usuario_txt.setText(graduado.getNombreUsuario());
                                nombre_txt.setText(graduado.getPersona().getPrimerNombre());
                                nombre2_txt.setText(graduado.getPersona().getSegundoNombre());
                                apellido_txt.setText(graduado.getPersona().getApellidoMaterno());
                                telefono_txt.setText(graduado.getPersona().getTelefono());
                                estado_txt.setText(graduadoDTO.getEstadoCivil());

                                String fotoBase64 = graduado.getUrlImagen();

                                if (fotoBase64 != null && !fotoBase64.isEmpty()) {
                                    Glide.with(DetallePostuladoActivity.this)
                                            .load(fotoBase64)
                                            .into(postulado_img);
                                } else {
                                    Glide.with(DetallePostuladoActivity.this)
                                            .load(R.drawable.empresas_img)
                                            .into(postulado_img);
                                }
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        openPDF(DetallePostuladoActivity.this, graduadoDTO.getUrlPdf());
                                    }
                                });

                            } else {
                                loadingDialog.showFailureDialog("Opps...", "No se encontro la informacion del graduado");
                            }
                        }

                        @Override
                        public void onFailure(Call<Usuario> call, Throwable t) {
                            System.out.println(R.string.error_conexion + t.getMessage());
                            Toast.makeText(DetallePostuladoActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                            t.printStackTrace();
                        }
                    });
/*
                    // Obtener referencia al botón "Postular"
                    Button btnPostular = findViewById(R.id.btnPostular);
                    // Agregar un OnClickListener al botón
                    btnPostular.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PostulacionDTO postulacionDTO = new PostulacionDTO();
                            postulacionDTO.setGraduado(usuarioId);
                           // postulacionDTO.setOfertaLaboral(ofertaDetalle.getId());
                            postulacionDTO.setEstado(String.valueOf(EstadoPostulacion.APLICANDO));

                            Call<PostulacionDTO> call = postulacionesServiceApi.createPostulacion(postulacionDTO, "Bearer " + accessToken);

                            loadingDialog.startLoagingDialog();
                            call.enqueue(new Callback<PostulacionDTO>() {
                                @Override
                                public void onResponse(Call<PostulacionDTO> call, Response<PostulacionDTO> response) {
                                    loadingDialog.dismissDialog();

                                    if (response.isSuccessful()) {
                                        loadingDialog.showSuccessDialog("¡Te has postulado a esta oferta!");
                                    } else {
                                        loadingDialog.dismissDialog();
                                        loadingDialog.showFailureDialog("Error al postularse. Inténtalo de nuevo.");
                                    }
                                }

                                @Override
                                public void onFailure(Call<PostulacionDTO> call, Throwable t) {
                                    loadingDialog.dismissDialog();
                                    loadingDialog.showFailureDialog("Error de conexión. Inténtalo de nuevo.");
                                }
                            });
                        }
                    });*/
                } else {
                    loadingDialog.showFailureDialog("Opps...", "No se encontro la informacion del graduado");
                }
            }

            @Override
            public void onFailure(Call<GraduadoDTO> call, Throwable t) {
                System.out.println(R.string.error_conexion + t.getMessage());
                Toast.makeText(DetallePostuladoActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }else{
            // Si no hay conexión a Internet, recuperar los datos de la base de datos local
            DBHelper dbHelper = new DBHelper(DetallePostuladoActivity.this);
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            String selection = GraduadoTable.COLUMN_USUARIO_GRADUADO + " = ?";
            String[] selectionArgs = { usuario };

            Cursor cursor = db.query(GraduadoTable.TABLE_NAME, null, selection, selectionArgs, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {                    // Leer los datos del cursor y crear objetos EmpresaDTO
                    int idusergraduadoIndex = cursor.getColumnIndex(GraduadoTable.COLUMN_ID_USER_GRADUADO);
                    int primernombreIndex = cursor.getColumnIndex(GraduadoTable.COLUMN_PRIMER_NOMBRE);
                    int segundonombreIndex = cursor.getColumnIndex(GraduadoTable.COLUMN_SEGUNDO_NOMBRE_GRADUADO);
                    int primerapellidoIndex = cursor.getColumnIndex(GraduadoTable.COLUMN_PRIMER_APELLIDO_GRADUADO);
                    int mailPersonalIndex = cursor.getColumnIndex(GraduadoTable.COLUMN_MAIL_PERSONAL);
                    int telefonoIndex = cursor.getColumnIndex(GraduadoTable.COLUMN_TELEFONO_GRADUADO);
                    int ofertaIDIndex = cursor.getColumnIndex(GraduadoTable.COLUMN_OFERTA_ID);
                    int usuarioIndex = cursor.getColumnIndex(GraduadoTable.COLUMN_USUARIO_GRADUADO);
                    int estadoIndex = cursor.getColumnIndex(GraduadoTable.COLUMN_ESTADO_GRADUADO);


                    System.out.println(primernombreIndex + "GraduadoTable.COLUMN_PRIMER_NOMBRE OBTENER");



                    int graduadoid = cursor.getInt(idusergraduadoIndex);
                    String usuario = cursor.getString(usuarioIndex);
                    String primernombre = cursor.getString(primernombreIndex);
                    String segundonombre = cursor.getString(segundonombreIndex);
                    String primerapellido = cursor.getString(primerapellidoIndex);
                    String mail = cursor.getString(mailPersonalIndex);
                    String telefono = cursor.getString(telefonoIndex);
                    String estadomatri = cursor.getString(estadoIndex);
                    int ofertaid = cursor.getInt(ofertaIDIndex);
                System.out.println(primerapellido+primernombre + "GRADUADOOOOOOOOOOOs");

                usuario_txt.setText("YOOOOOOOOO");
                nombre_txt.setText(primernombre);
                nombre2_txt.setText(segundonombre);
                apellido_txt.setText(primerapellido);
                telefono_txt.setText(telefono);
                estado_txt.setText(estadomatri);

                } while (cursor.moveToNext());

                cursor.close();

            } else {
                loadingDialog.showFailureDialog("No tiene Conexion", "No tiene datos guardados localmente");
            }
        }
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void openPDF(Context context, String pdfUrl) {
       try{
           // Crear una intención para ver el PDF
           Intent intent = new Intent(Intent.ACTION_VIEW);
           intent.setDataAndType(Uri.parse(pdfUrl), "application/pdf");
           intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

           // Verificar si hay una aplicación disponible para manejar la intención
           if (intent.resolveActivity(context.getPackageManager()) != null) {
               // Hay una aplicación disponible, lanzar la intención
               context.startActivity(intent);
           } else {
               // No hay ninguna aplicación disponible para manejar la intención
               // Aquí puedes mostrar un mensaje de error o proporcionar una alternativa
           }
       }catch (NullPointerException e){
           loadingDialog.showFailureDialog("Opps...", "El Postulante seleccionado no tiene un CV");
       }

    }
}
