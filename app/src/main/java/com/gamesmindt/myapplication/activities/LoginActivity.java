package com.gamesmindt.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.gamesmindt.myapplication.Model.LoginDTO;
import com.gamesmindt.myapplication.Model.Persona;
import com.gamesmindt.myapplication.Model.Usuario;
import com.gamesmindt.myapplication.R;
import com.gamesmindt.myapplication.Services.LoginResponse;
import com.gamesmindt.myapplication.Services.UserServiceApi;
import com.gamesmindt.myapplication.configs.LoadingDialog;
import com.gamesmindt.myapplication.configs.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {
    EditText username, password;
    TextView recover;
    Button btnLogin;
    LoadingDialog loadingDialog = new LoadingDialog(LoginActivity.this);
    private boolean passwordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.Username);
        password = findViewById(R.id.editTextTextPassword);
        btnLogin = findViewById(R.id.btnLogin);
        recover = findViewById(R.id.recover_btn);
        ImageButton showPasswordButton = findViewById(R.id.showPasswordButton);

        showPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordVisible) {
                    // Si la contraseña es visible, ocultarla
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    showPasswordButton.setImageResource(R.drawable.eye_close);
                } else {
                    // Si la contraseña está oculta, hacerla visible
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    showPasswordButton.setImageResource(R.drawable.eye_open);
                }
                // Cambiar el estado de la contraseña
                passwordVisible = !passwordVisible;
            }
        });

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#0078ff"));
        recover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent recoverIntent = new Intent(LoginActivity.this, CodPwdActivity.class);
                startActivity(recoverIntent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(username.getText().toString()) || TextUtils.isEmpty(password.getText().toString())) {
//                    Toast.makeText(LoginActivity.this, "Username / password son necesarios.", Toast.LENGTH_LONG).show();
                    loadingDialog.warningDialog("Datos Faltantes", "Por favor, ingresa el nombre de usuario y la contraseña.");
                } else {
                    loadingDialog.startLoagingDialog();
                    LoginDTO loginDTO = new LoginDTO();
                    loginDTO.setPassword(password.getText().toString());
                    loginDTO.setUsername(username.getText().toString());

                    Call<LoginResponse> loginResponseCall = RetrofitClient.getUserService().getLoginInformation(loginDTO);
                    loginResponseCall.enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            if (response.isSuccessful()) {
                                ObtenerDatosDelUsuario(response);
                            } else {
                                loadingDialog.showFailureDialog("Oops...", "Inicio de sesión fallido. Por favor, revise sus credenciales");
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, "throwable" + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            loadingDialog.showFailureDialog(getString(R.string.error_conexion), "Error en la petición, revise su conexión a internet.");
                        }
                    });
                }
            }
        });
    }

    private void ObtenerDatosDelUsuario(Response<LoginResponse> response) {
        // Llamar a la API para obtener la imagen de perfil
        String accessToken = response.body().getAccessToken();

        String username = response.body().getUsername();
        // Obtener una instancia de Retrofit con el interceptor
        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        // Obtener el servicio UserServiceApi
        UserServiceApi userServiceApi = retrofit.create(UserServiceApi.class);
        // Llamar a la API para obtener la información del usuario
        Call<Usuario> call = userServiceApi.getUsuarioByUsername(username, "Bearer " + accessToken);
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> responseNewData) {
                if (response.isSuccessful() && response.body() != null) {
                    loadingDialog.cerrarLoadingDialog();

                    SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();

                    Persona persona = responseNewData.body().getPersona();
                    String cedulaUser = persona.getCedula();

                    editor.putString("accessToken", response.body().getAccessToken());
                    editor.putString("tokenType", response.body().getTokenType());
                    editor.putInt("usuarioId", response.body().getUsuario_id());
                    editor.putString("username", response.body().getUsername());
                    editor.putString("urlImagen", responseNewData.body().getUrlImagen());
                    editor.putInt("id_persona", persona.getId());
                    editor.putString("cedulaUser", cedulaUser);

                    List<LoginResponse.Authority> authorities = response.body().getAuthorities();
                    if (authorities != null && !authorities.isEmpty()) {
                        StringBuilder rolesBuilder = new StringBuilder();
                        for (LoginResponse.Authority authority : authorities) {
                            rolesBuilder.append(authority.getAuthority()).append(", ");
                        }
                        String roles = rolesBuilder.toString().trim();
                        if (roles.endsWith(",")) {
                            roles = roles.substring(0, roles.length() - 1); // Elimina la última coma
                        }
                        editor.putString("userRoles", roles);
                    }

                    editor.apply();
                    Toast.makeText(LoginActivity.this, "¡Bienvenido!", Toast.LENGTH_LONG).show();
                    finish();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    loadingDialog.showFailureDialog("Error de Conexión!", "Error en la respuesta de la API");
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                loadingDialog.showFailureDialog("Error de Conexión!", "Error en la respuesta de la API: " + t.getMessage());
            }
        });

    }
}