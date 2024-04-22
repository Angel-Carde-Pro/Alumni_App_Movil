package com.gamesmindt.myapplication.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
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
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.gamesmindt.myapplication.Model.EstadoPostulacion;
import com.gamesmindt.myapplication.Model.OfertaDetalle;
import com.gamesmindt.myapplication.Model.PostulacionDTO;
import com.gamesmindt.myapplication.R;
import com.gamesmindt.myapplication.Services.DetalleOfertasServiceApi;
import com.gamesmindt.myapplication.Services.PostulacionesServiceApi;
import com.gamesmindt.myapplication.configs.LoadingDialog;
import com.gamesmindt.myapplication.configs.RetrofitClient;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleOfertaActivity extends AppCompatActivity {

    DetalleOfertasServiceApi ofertasServiceApi = RetrofitClient.getRetrofitInstance().create(DetalleOfertasServiceApi.class);
    PostulacionesServiceApi postulacionesServiceApi = RetrofitClient.getRetrofitInstance().create(PostulacionesServiceApi.class);
    private OfertaDetalle ofertaDetalle;
    SharedPreferences preferences;
    String usuario = "";
    String roles = "";
    String accessToken = "";
    Integer usuarioId = -1;
    LoadingDialog loadingDialog = new LoadingDialog(DetalleOfertaActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_oferta);
        Window window = getWindow();

        preferences = DetalleOfertaActivity.this.getSharedPreferences("user_data", MODE_PRIVATE);
        usuario = preferences.getString("username", "");
        roles = preferences.getString("userRoles", "");
        accessToken = preferences.getString("accessToken", "");
        usuarioId = preferences.getInt("usuarioId", -1);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.black));

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Integer id_Oferta = intent.getIntExtra("id_Oferta", -1);
        loadingDialog.startLoagingDialog();

        // Llamar a la API para obtener las ofertas laborales
        Call<OfertaDetalle> call = ofertasServiceApi.getOfertaLaboralById(id_Oferta, "Bearer " + accessToken);
        call.enqueue(new Callback<OfertaDetalle>() {
            @Override
            public void onResponse(Call<OfertaDetalle> call, Response<OfertaDetalle> response) {
                if (response.isSuccessful()) {
                    loadingDialog.cerrarLoadingDialog();
                    ofertaDetalle = response.body();

                    setEdiTextValue(ofertaDetalle.getEmpresa().getNombre(), findViewById(R.id.empresaNombre), findViewById(R.id.title1TL));
                    setEdiTextValue(ofertaDetalle.getEmpresa().getUbicacion(), findViewById(R.id.ubicacion), findViewById(R.id.title2TL));
                    setEdiTextValue(ofertaDetalle.getAreaConocimiento(), findViewById(R.id.areaConocimiento), findViewById(R.id.title3TL));
                    setEdiTextValue(ofertaDetalle.getCargo(), findViewById(R.id.cargo), findViewById(R.id.title4TL));
                    setEdiTextValue(ofertaDetalle.getExperiencia(), findViewById(R.id.experiencia), findViewById(R.id.title5TL));

                    parseFechas(ofertaDetalle.getFechaPublicacion(), findViewById(R.id.fechaPublicacion), findViewById(R.id.title6TL));
                    parseFechas(ofertaDetalle.getFechaApertura(), findViewById(R.id.fechaApertura), findViewById(R.id.title7TL));
                    parseFechas(ofertaDetalle.getFechaCierre(), findViewById(R.id.fechaCierre), findViewById(R.id.title8TL));

                    setEdiTextValue(ofertaDetalle.getTiempo(), findViewById(R.id.tiempo), findViewById(R.id.title9TL));
                    setEdiTextValue(String.valueOf(ofertaDetalle.getSalario()), findViewById(R.id.salario), findViewById(R.id.title10TL));

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

                    // Obtener referencia al botón "Postular"
                    Button btnPostular = findViewById(R.id.btnPostular);
                    // Agregar un OnClickListener al botón

                    System.out.println("id_Oferta = " + ofertaDetalle.getId());
                    System.out.println("id_usuario = " + usuarioId);
                    btnPostular.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PostulacionDTO postulacionDTO = new PostulacionDTO();
                            postulacionDTO.setGraduado(usuarioId);
                            postulacionDTO.setOfertaLaboral(ofertaDetalle.getId());
                            postulacionDTO.setEstado(EstadoPostulacion.APLICANDO.toString());

                            Call<PostulacionDTO> call = postulacionesServiceApi.createPostulacion(postulacionDTO, "Bearer " + accessToken);

                            loadingDialog.startLoagingDialog();
                            call.enqueue(new Callback<PostulacionDTO>() {
                                @Override
                                public void onResponse(Call<PostulacionDTO> call, Response<PostulacionDTO> response) {
                                    loadingDialog.cerrarLoadingDialog();
                                    Toast.makeText(DetalleOfertaActivity.this, "¡Te has postulado a esta oferta!", Toast.LENGTH_SHORT).show();

                                    finish();
                                    Intent intent = new Intent(DetalleOfertaActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }

                                @Override
                                public void onFailure(Call<PostulacionDTO> call, Throwable t) {
                                    loadingDialog.cerrarLoadingDialog();
//                                    loadingDialog.showFailureDialog("Error de conexión. Inténtalo de nuevo.");
                                    System.out.println("Error de conexión = " + t);
                                    Toast.makeText(DetalleOfertaActivity.this, "¡Te has postulado a esta oferta!", Toast.LENGTH_SHORT).show();

                                    finish();
                                    Intent intent = new Intent(DetalleOfertaActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            });
                        }
                    });
                } else {
                    loadingDialog.showFailureDialog("Oops...", "Fallo al intentar obtener los datos.");
                }
            }

            @Override
            public void onFailure(Call<OfertaDetalle> call, Throwable t) {
                System.out.println(R.string.error_conexion + t.getMessage());
                loadingDialog.showFailureDialog(getString(R.string.error_conexion), "Revise su conexión a internet.");
                t.printStackTrace();
            }
        });
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

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
