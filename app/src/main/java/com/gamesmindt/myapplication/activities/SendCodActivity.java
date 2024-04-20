package com.gamesmindt.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.gamesmindt.myapplication.R;
import com.gamesmindt.myapplication.configs.LoadingDialog;

public class SendCodActivity extends AppCompatActivity {
    LoadingDialog loadingDialog = new LoadingDialog(SendCodActivity.this);
    Button btnVerificar;
    TextView volverAEnviartext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_cod);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnVerificar = findViewById(R.id.btnVerificar);
        volverAEnviartext = findViewById(R.id.volverAEnviartext);

        volverAEnviartext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SendCodActivity.this, "El código fue enviado nuevamente.", Toast.LENGTH_SHORT).show();
            }
        });
        btnVerificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent recoverIntent = new Intent(SendCodActivity.this, ResetPwdCodActivity.class);
                startActivity(recoverIntent);
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