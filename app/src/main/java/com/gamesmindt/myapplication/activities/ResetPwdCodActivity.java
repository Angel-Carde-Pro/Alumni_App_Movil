package com.gamesmindt.myapplication.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gamesmindt.myapplication.R;
import com.gamesmindt.myapplication.configs.LoadingDialog;

public class ResetPwdCodActivity extends AppCompatActivity {
    LoadingDialog loadingDialog = new LoadingDialog(ResetPwdCodActivity.this);
    Button btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_restablecer);

        btnReset = findViewById(R.id.btnReset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Toast.makeText(ResetPwdCodActivity.this, "Su contraseña fue restablecida correctamente.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}