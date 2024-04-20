package com.gamesmindt.myapplication.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.gamesmindt.myapplication.Model.EstadoOferta;
import com.gamesmindt.myapplication.Model.EstadoPostulacion;
import com.gamesmindt.myapplication.Model.OfertaDetalle;
import com.gamesmindt.myapplication.Model.PostulacionDTO;
import com.gamesmindt.myapplication.R;
import com.gamesmindt.myapplication.Services.DetalleOfertasServiceApi;
import com.gamesmindt.myapplication.Services.PostulacionesServiceApi;
import com.gamesmindt.myapplication.configs.LoadingDialog;
import com.gamesmindt.myapplication.configs.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetallePostulaciónActivity extends AppCompatActivity {

    DetalleOfertasServiceApi ofertasServiceApi = RetrofitClient.getRetrofitInstance().create(DetalleOfertasServiceApi.class);
    PostulacionesServiceApi postulacionesServiceApi = RetrofitClient.getRetrofitInstance().create(PostulacionesServiceApi.class);
    private OfertaDetalle ofertaDetalle;
    Window window = null;
    SharedPreferences preferences;
    String roles = "";
    String accessToken = "";
    Integer usuarioId = -1;
    LoadingDialog loadingDialog = new LoadingDialog(DetallePostulaciónActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_postulacion);
        window = getWindow();

        preferences = DetallePostulaciónActivity.this.getSharedPreferences("user_data", MODE_PRIVATE);
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

                    EditText empresaNombreEditText = findViewById(R.id.empresaNombre);
                    EditText ubicacionEditText = findViewById(R.id.ubicacion);
                    EditText cargoEditText = findViewById(R.id.cargo);
                    EditText areaConocimientoEditText = findViewById(R.id.areaConocimiento);
                    EditText experienciaEditText = findViewById(R.id.experiencia);
                    EditText salarioEditText = findViewById(R.id.salario);
                    EditText fechaPublicacionEditText = findViewById(R.id.fechaPublicacion);
                    EditText fechaCierreEditText = findViewById(R.id.fechaCierre);
                    EditText fechaAperturaEditText = findViewById(R.id.fechaApertura);
                    EditText tiempoEditText = findViewById(R.id.tiempo);
                    ImageView empresaImg = findViewById(R.id.empresaImg);

                    empresaNombreEditText.setText(ofertaDetalle.getEmpresa().getNombre());
                    ubicacionEditText.setText(ofertaDetalle.getEmpresa().getUbicacion());
                    areaConocimientoEditText.setText(ofertaDetalle.getAreaConocimiento());
                    cargoEditText.setText(ofertaDetalle.getCargo());
                    experienciaEditText.setText(ofertaDetalle.getExperiencia());
                    salarioEditText.setText(String.valueOf(ofertaDetalle.getSalario()));
                    tiempoEditText.setText(String.valueOf(ofertaDetalle.getTiempo()));

                    fechaPublicacionEditText.setText(ofertaDetalle.getFechaPublicacion());
                    fechaCierreEditText.setText(ofertaDetalle.getFechaCierre());
                    fechaAperturaEditText.setText(ofertaDetalle.getFechaApertura());

                    String fotoBase64 = ofertaDetalle.getFotoPortada();

                    if (fotoBase64 != null && !fotoBase64.isEmpty()) {
                        Glide.with(DetallePostulaciónActivity.this)
                                .load(fotoBase64)
                                .into(empresaImg);
                    } else {
                        Glide.with(DetallePostulaciónActivity.this)
                                .load(R.drawable.empresas_img)
                                .into(empresaImg);
                    }

                    // Obtener referencia al botón "Postular"
                    Button btnPostular = findViewById(R.id.btnCancelarPos);

                    if (ofertaDetalle.getEstado() == EstadoOferta.CANCELADA_POR_GRADUADO) {
                        btnPostular.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#35bc1e")));
                        btnPostular.setText("POSTULAR");
                    } else {
                        btnPostular.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF2E2E")));
                        btnPostular.setText("CANCELAR");
                    }

                    // Agregar un OnClickListener al botón
                    btnPostular.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PostulacionDTO postulacionDTO = new PostulacionDTO();
                            postulacionDTO.setGraduado(usuarioId);
                            postulacionDTO.setOfertaLaboral(ofertaDetalle.getId());

                            if (ofertaDetalle.getEstado() == EstadoOferta.CANCELADA_POR_GRADUADO) {
                                postulacionDTO.setEstado(EstadoPostulacion.APLICANDO.toString());
                            } else {
                                postulacionDTO.setEstado(EstadoPostulacion.CANCELADA_POR_GRADUADO.toString());
                            }

                            postulacionDTO.setEstado(EstadoPostulacion.CANCELADA_POR_GRADUADO.toString());

                            Call<PostulacionDTO> call = postulacionesServiceApi.updateStatePostulacion(id_postulacion, postulacionDTO, "Bearer " + accessToken);

                            loadingDialog.startLoagingDialog();
                            call.enqueue(new Callback<PostulacionDTO>() {
                                @Override
                                public void onResponse(Call<PostulacionDTO> call, Response<PostulacionDTO> response) {

                                    if (ofertaDetalle.getEstado() == EstadoOferta.CANCELADA) {
                                        Toast.makeText(DetallePostulaciónActivity.this, "Postulación Realizada.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(DetallePostulaciónActivity.this, "Postulación Cancelada.", Toast.LENGTH_SHORT).show();
                                    }
                                    finish();
                                }

                                @Override
                                public void onFailure(Call<PostulacionDTO> call, Throwable t) {
                                    // Mismo problema en la app web - Error: java.net.SocketTimeoutException: timeout (El tiempo de espera es demasiado largo)
                                    // Pero la postulación se crea, no hay problema
                                    // Solución: Revisar backend

                                    if (ofertaDetalle.getEstado() == EstadoOferta.CANCELADA) {
                                        Toast.makeText(DetallePostulaciónActivity.this, "Postulación Realizada.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(DetallePostulaciónActivity.this, "Postulación Cancelada.", Toast.LENGTH_SHORT).show();
                                    }
                                    System.out.println("Error de conexión = " + t);
                                    finish();
                                }
                            });
                        }
                    });
                } else {
                    loadingDialog.showFailureDialog("Opps...", "Fallo al intentar obtener los datos.");
                }
            }

            @Override
            public void onFailure(Call<OfertaDetalle> call, Throwable t) {
                System.out.println(R.string.error_conexion + t.getMessage());
                Toast.makeText(DetallePostulaciónActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
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
