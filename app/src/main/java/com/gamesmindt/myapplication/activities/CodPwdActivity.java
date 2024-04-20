package com.gamesmindt.myapplication.activities;

import android.content.Intent;
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
    TextView iniciarSesionTxt;

    EditText emailTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_pwd);
        RecoverPassServiceApi recoverPassServiceApi = RetrofitClient.getRetrofitInstance().create(RecoverPassServiceApi.class);
        LoadingDialog loadingDialog = new LoadingDialog(CodPwdActivity.this);



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
                if(!TextUtils.isEmpty(emailTxt.getText())){
                    MailRequest mailRequest = new MailRequest();
                    mailRequest.setTo(emailTxt.getText().toString());
                    System.out.println("CORREO: " + emailTxt.getText().toString());
                    Call<String> call =  recoverPassServiceApi.sendTokenMail(mailRequest);
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            int status = response.code();
                            if (status == 409){
                                System.out.println("SI FUNCIONA");
                                finish();
                                loadingDialog.showFailureDialog("Recuperacion", "Al correo electronico se le envio un codigo de recuperacion");
                                Intent recoverIntent = new Intent(CodPwdActivity.this, SendCodActivity.class);
                                startActivity(recoverIntent);
                            }else {
                                System.out.println("NO FUNCIONA PERO SI");
                                loadingDialog.showFailureDialog("Whopps!...", "No se encontro el email");
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            t.getStackTrace();
                            System.out.println(t.getStackTrace());
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