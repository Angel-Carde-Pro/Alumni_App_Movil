package com.gamesmindt.myapplication.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.gamesmindt.myapplication.Model.OfertaDetalle;
import com.gamesmindt.myapplication.Model.Postulacion;
import com.gamesmindt.myapplication.R;
import com.gamesmindt.myapplication.Services.DetalleOfertasServiceApi;
import com.gamesmindt.myapplication.configs.LoadingDialog;
import com.gamesmindt.myapplication.configs.RetrofitClient;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetallePostulacionActivity extends AppCompatActivity {

    DetalleOfertasServiceApi ofertasServiceApi = RetrofitClient.getRetrofitInstance().create(DetalleOfertasServiceApi.class);
    private OfertaDetalle ofertaDetalle;
    Window window = null;
    SharedPreferences preferences;
    String roles = "";
    String accessToken = "";
    Integer usuarioId = -1;
    LoadingDialog loadingDialog = new LoadingDialog(DetallePostulacionActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_postulacion);
        window = getWindow();

        preferences = DetallePostulacionActivity.this.getSharedPreferences("user_data", MODE_PRIVATE);
        roles = preferences.getString("userRoles", "");
        accessToken = preferences.getString("accessToken", "");
        usuarioId = preferences.getInt("usuarioId", -1);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.black));

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CargarDatos();
    }

    private void CargarDatos() {
        loadingDialog.startLoagingDialog();

        Intent intent = getIntent();
        Integer id_postulacion = intent.getIntExtra("id_postulacion", -1);
        System.out.println("id_postulacion = " + id_postulacion);

        // Llamar a la API para obtener las ofertas laborales
        Call<OfertaDetalle> call = ofertasServiceApi.getOfertaLaboralById(id_postulacion, "Bearer " + accessToken);
        call.enqueue(new Callback<OfertaDetalle>() {
            @Override
            public void onResponse(Call<OfertaDetalle> call, Response<OfertaDetalle> response) {
                loadingDialog.cerrarLoadingDialog();
                if (response.isSuccessful()) {
                    ofertaDetalle = response.body();

                    setEdiTextValue(ofertaDetalle.getEmpresa().getNombre(), findViewById(R.id.empresaNombre), findViewById(R.id.title_1));
                    setEdiTextValue(ofertaDetalle.getEmpresa().getUbicacion(), findViewById(R.id.ubicacion), findViewById(R.id.title_2));
                    setEdiTextValue(ofertaDetalle.getAreaConocimiento(), findViewById(R.id.areaConocimiento), findViewById(R.id.title_3));
                    setEdiTextValue(ofertaDetalle.getCargo(), findViewById(R.id.cargo), findViewById(R.id.title_4));
                    setEdiTextValue(ofertaDetalle.getExperiencia(), findViewById(R.id.experiencia), findViewById(R.id.title_5));

                    parseFechas(ofertaDetalle.getFechaPublicacion(), findViewById(R.id.fechaPublicacion), findViewById(R.id.title_6));
                    parseFechas(ofertaDetalle.getFechaApertura(), findViewById(R.id.fechaApertura), findViewById(R.id.title_7));
                    parseFechas(ofertaDetalle.getFechaCierre(), findViewById(R.id.fechaCierre), findViewById(R.id.title_8));

                    setEdiTextValue(ofertaDetalle.getTiempo(), findViewById(R.id.tiempo), findViewById(R.id.title_9));
                    setEdiTextValue(String.valueOf(ofertaDetalle.getSalario()), findViewById(R.id.salario), findViewById(R.id.title_10));

                    String fotoBase64 = ofertaDetalle.getFotoPortada();

                    SubsamplingScaleImageView empresaImg = (SubsamplingScaleImageView) findViewById(R.id.empresaImg);

                    if (fotoBase64 != null && !fotoBase64.isEmpty()) {
                        try {
                            String base64EncodedData = fotoBase64.substring(fotoBase64.indexOf(",") + 1);

                            byte[] decodedBytes = android.util.Base64.decode(base64EncodedData, android.util.Base64.DEFAULT);

                            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

                            empresaImg.setImage(ImageSource.bitmap(decodedBitmap));
                            empresaImg.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_INSIDE);
                        } catch (IllegalArgumentException e) {
                            empresaImg.setImage(ImageSource.resource(R.drawable.empresas_img));
                        }
                    } else {
                        empresaImg.setImage(ImageSource.resource(R.drawable.empresas_img));
                    }
                } else {
                    loadingDialog.showFailureDialog("Opps...", "Fallo al intentar obtener los datos.");
                }
            }

            @Override
            public void onFailure(Call<OfertaDetalle> call, Throwable t) {
                System.out.println(R.string.error_conexion + t.getMessage());
                Toast.makeText(DetallePostulacionActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private void setEdiTextValue(String value, EditText textView, TextInputLayout titleView) {
        if (value != null) {
            textView.setText(value);
            titleView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
            titleView.setVisibility(View.GONE);
        }
    }

    private void parseFechas(String fecha, EditText textView, TextInputLayout titleView) {
        if (fecha != null) {
            SimpleDateFormat formatoFechaEntrada = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
            try {
                Date fechaDate = formatoFechaEntrada.parse(fecha);
                SimpleDateFormat formatoSalida = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
                String fechaFormateada = formatoSalida.format(fechaDate);
                textView.setText(fechaFormateada);
                textView.setVisibility(View.VISIBLE);
                titleView.setVisibility(View.VISIBLE);
            } catch (ParseException e) {
                e.printStackTrace();
                textView.setVisibility(View.GONE);
                titleView.setVisibility(View.GONE);
            }
        } else {
            textView.setVisibility(View.GONE);
            titleView.setVisibility(View.GONE);
        }
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
