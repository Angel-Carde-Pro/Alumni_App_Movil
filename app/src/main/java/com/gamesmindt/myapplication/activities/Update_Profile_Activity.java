package com.gamesmindt.myapplication.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.gamesmindt.myapplication.Model.EstadoPostulacion;
import com.gamesmindt.myapplication.Model.OfertaDetalle;
import com.gamesmindt.myapplication.Model.Persona;
import com.gamesmindt.myapplication.Model.PersonaDTO;
import com.gamesmindt.myapplication.Model.PostulacionDTO;
import com.gamesmindt.myapplication.Model.Usuario;
import com.gamesmindt.myapplication.Model.UsuarioDTP;
import com.gamesmindt.myapplication.R;
import com.gamesmindt.myapplication.Services.DetalleOfertasServiceApi;
import com.gamesmindt.myapplication.Services.PostulacionesServiceApi;
import com.gamesmindt.myapplication.Services.UserServiceApi;
import com.gamesmindt.myapplication.configs.LoadingDialog;
import com.gamesmindt.myapplication.configs.RetrofitClient;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Update_Profile_Activity extends AppCompatActivity {
    UserServiceApi userServiceApi = RetrofitClient.getRetrofitInstance().create(UserServiceApi.class);
    Usuario user;
    Window window = null;
    SharedPreferences preferences;
    String usuario = "";
    String roles = "";
    String accessToken = "";
    Integer usuarioId = -1;
    LoadingDialog loadingDialog = new LoadingDialog(Update_Profile_Activity.this);

    private TextView usuario_txt, nombre_txt, nombre2_txt, apellido_txt, apellido2, telefono_txt;
    //private TextInputEditText ;
    private ImageView user_img;
    private Button buttonAct;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_profile);
        calendar = Calendar.getInstance();

        window = getWindow();

        preferences = Update_Profile_Activity.this.getSharedPreferences("user_data", MODE_PRIVATE);
        usuario = preferences.getString("username", "");
        roles = preferences.getString("userRoles", "");
        accessToken = preferences.getString("accessToken", "");
        usuarioId = preferences.getInt("usuarioId", -1);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.black));

        usuario_txt = findViewById(R.id.Text_Usuario);
        nombre_txt = findViewById(R.id.Text_Nombre);
        nombre2_txt = findViewById(R.id.Text_NombreSegundo);
        apellido_txt = findViewById(R.id.Text_Apellido);
        apellido2 = findViewById(R.id.Text_Seg_Apellido);
        telefono_txt = findViewById(R.id.Text_Telefono);
        user_img = findViewById(R.id.profile_imagePostuladop);
        buttonAct = findViewById(R.id.actualizar_txt);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loadingDialog.startLoagingDialog();

        Call<Usuario> callback = userServiceApi.getUserByUsername(usuario, "Bearer " + accessToken);
        callback.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                loadingDialog.cerrarLoadingDialog();
                if (response.isSuccessful()) {
                    user = response.body();
                    usuario_txt.setText(user.getNombreUsuario());
                    nombre_txt.setText(user.getPersona().getPrimerNombre());
                    nombre2_txt.setText(user.getPersona().getSegundoNombre());
                    apellido_txt.setText(user.getPersona().getApellidoMaterno());
                    apellido2.setText(user.getPersona().getApellidoPaterno());
                    telefono_txt.setText(user.getPersona().getTelefono());
                    String fotoBase64 = user.getUrlImagen();

                    if (fotoBase64 != null && !fotoBase64.isEmpty()) {
                        Glide.with(Update_Profile_Activity.this)
                                .load(fotoBase64)
                                .into(user_img);
                    } else {
                        Glide.with(Update_Profile_Activity.this)
                                .load(R.drawable.empresas_img)
                                .into(user_img);
                    }

                    buttonAct.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Obtener los nuevos datos del usuario desde los EditText
                            String nuevoPrimerNombre = nombre_txt.getText().toString();
                            String nuevoSegundoNombre = nombre2_txt.getText().toString();
                            String nuevoApellidoMaterno = apellido_txt.getText().toString();
                            String nuevoApellidoPaterno = apellido2.getText().toString();
                            String nuevoTelefono = telefono_txt.getText().toString();


                            Persona personaActualizada = user.getPersona();
                            PersonaDTO personaDTO = new PersonaDTO();
                            System.out.println(user.getPersona().getId() + "" + user.getPersona().getTelefono() + "AAAAAAAAAAAAAA");
                            personaDTO.setCedula(user.getPersona().getCedula());
                            personaDTO.setPrimerNombre(nuevoPrimerNombre);
                            personaDTO.setSegundoNombre(nuevoSegundoNombre);
                            personaDTO.setApellidoMaterno(nuevoApellidoMaterno);
                            personaDTO.setApellidoPaterno(nuevoApellidoPaterno);
                            personaDTO.setTelefono(nuevoTelefono);
                            personaDTO.setSexo(user.getPersona().getSexo());
                            String dateString = user.getPersona().getFechaNacimiento().toString();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);

                            try {
                                Date date = dateFormat.parse(dateString);
                                LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                                String malevodo = localDate.format(DateTimeFormatter.ofPattern("YYYY-MM-dd"));
                                personaDTO.setFechaNacimiento(malevodo);

                                System.out.println(malevodo + "FECHA LOCAL DATE");
                            } catch (ParseException e) {
                                // Maneja la excepción si la cadena no está en el formato esperado
                                e.printStackTrace();
                            }

                            /*Date date = personaActualizada.getFechaNacimiento();
                            String dateString = date.toString();
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy");
                            LocalDate fechaNacimiento = LocalDate.parse(dateString, formatter);
                            personaDTO.setFechaNacimiento(fechaNacimiento);*/
                            loadingDialog.startLoagingDialog();
                            System.out.println(user.getPersona().getFechaNacimiento() + "AAAAAAAAAAAAAAAAFECHA");
                            userServiceApi.updatePerson(user.getPersona().getId(), personaDTO, "Bearer " + accessToken)
                                    .enqueue(new Callback<Void>() {
                                        @Override
                                        public void onResponse(Call<Void> call, Response<Void> response) {
                                            if (response.isSuccessful()) {
                                                // Actualización exitosa, maneja cualquier lógica adicional aquí
                                                // Por ejemplo, mostrar un mensaje de éxito
                                                loadingDialog.showSuccessDialog("¡Pefil Actualizado!", "Usuario actualizado correctamente");
                                            } else {
                                                // Manejar el caso en que la actualización falla
                                                // Aquí puedes mostrar un mensaje de error o cualquier otro manejo de error necesario
                                                Toast.makeText(Update_Profile_Activity.this, "Error al actualizar usuario", Toast.LENGTH_SHORT).show();
                                                loadingDialog.showFailureDialog(getString(R.string.error_conexion), "Error al actualizar usuario");
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Void> call, Throwable t) {
                                            // Manejar el caso en que ocurra un error de red u otro error
                                            // Aquí puedes mostrar un mensaje de error o cualquier otro manejo de error necesario
                                            Toast.makeText(Update_Profile_Activity.this, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    });

                } else {
                    loadingDialog.showFailureDialog("Opps...", "No se encontro la informacion del graduado");
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                System.out.println(R.string.error_conexion + t.getMessage());
                System.out.println(t.getStackTrace() + "YPPPPPPPPPPPPPP");
                loadingDialog.showFailureDialog(getString(R.string.error_conexion), "Revisa tu conexión a internet.");
                t.printStackTrace();
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
