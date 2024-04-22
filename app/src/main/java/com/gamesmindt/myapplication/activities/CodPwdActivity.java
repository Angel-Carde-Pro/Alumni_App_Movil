package com.gamesmindt.myapplication.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.gamesmindt.myapplication.Model.MailRequest;
import com.gamesmindt.myapplication.R;
import com.gamesmindt.myapplication.Services.GraduadoServiceApi;
import com.gamesmindt.myapplication.Services.RecoverPassServiceApi;
import com.gamesmindt.myapplication.configs.LoadingDialog;
import com.gamesmindt.myapplication.configs.RetrofitClient;

import java.sql.SQLOutput;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CodPwdActivity extends AppCompatActivity {
    LoadingDialog loadingDialog = new LoadingDialog(CodPwdActivity.this);
    Button btnSendCod;
    SharedPreferences preferences;
    TextView iniciarSesionTxt;
    EditText emailTxt;
    String accessToken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_pwd);
        RecoverPassServiceApi recoverPassServiceApi = RetrofitClient.getRetrofitInstance().create(RecoverPassServiceApi.class);
        LoadingDialog loadingDialog = new LoadingDialog(CodPwdActivity.this);

        preferences = CodPwdActivity.this.getSharedPreferences("user_data", MODE_PRIVATE);
        accessToken = preferences.getString("accessToken", "");

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#0078ff"));

        btnSendCod = findViewById(R.id.btnSendCod);
        iniciarSesionTxt = findViewById(R.id.iniciarSesionTxt);
        emailTxt = findViewById(R.id.UsernameMail);

        iniciarSesionTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnSendCod.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(emailTxt.getText())) {
                    MailRequest mailRequest = new MailRequest();
                    mailRequest.setName(emailTxt.getText().toString());
                    mailRequest.setTo(emailTxt.getText().toString());
                    mailRequest.setFrom("infoalumni@gmail.com");
                    mailRequest.setSubject("Petición de cambio de contraseña");
                    mailRequest.setCaseEmail("reset-password");

                    Call<String> call = recoverPassServiceApi.sendTokenMail(mailRequest, "Bearer " + accessToken);
                    loadingDialog.startLoagingDialog();
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(CodPwdActivity.this, "Se ha enviado un correo a su dirección de correo electrónico para restablecer su contraseña", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                loadingDialog.showFailureDialog("Opps...", "El correo que ingresó ya contiene un token de recuperación activo.");
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            t.getStackTrace();
                            System.out.println(t.getStackTrace());
                            loadingDialog.showFailureDialog("Error de conexión", "Revise su conexión a internet.");
                        }
                    });
                }
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