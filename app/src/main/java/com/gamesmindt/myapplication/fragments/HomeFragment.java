package com.gamesmindt.myapplication.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gamesmindt.myapplication.Model.EstadoPostulacion;
import com.gamesmindt.myapplication.Model.OfertaLaboralDTO;
import com.gamesmindt.myapplication.Model.PostulacionDTO;
import com.gamesmindt.myapplication.R;
import com.gamesmindt.myapplication.Services.OfertasServiceApi;
import com.gamesmindt.myapplication.Services.PostulacionesServiceApi;
import com.gamesmindt.myapplication.Services.UserServiceApi;
import com.gamesmindt.myapplication.activities.DetalleOfertaActivity;
import com.gamesmindt.myapplication.adapters.OfertasAdapter;
import com.gamesmindt.myapplication.configs.LoadingDialog;
import com.gamesmindt.myapplication.configs.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements OfertasAdapter.OnClickListener {
    private UserServiceApi userServiceApi;
    private List<OfertaLaboralDTO> listaOfertas;
    private RecyclerView recyclerView;
    private OfertasAdapter ofertasAdapter;
    SharedPreferences preferences;
    LoadingDialog loadingDialog;

    String usuario = "";
    String roles = "";
    String token = "";
    Integer usuarioId = -1;
    Integer graduadoId = -1;
    View view;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        loadingDialog = new LoadingDialog(requireActivity());

        userServiceApi = RetrofitClient.getUserService();
        preferences = requireActivity().getSharedPreferences("user_data", MODE_PRIVATE);
        usuario = preferences.getString("username", "");
        roles = preferences.getString("userRoles", "");
        token = preferences.getString("accessToken", "");
        usuarioId = preferences.getInt("usuarioId", -1);
    }

    public interface OnMoreButtonClickListener {
        void onMoreButtonClicked();
    }

    private OnMoreButtonClickListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (OnMoreButtonClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnMoreButtonClickListener");
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        view = layoutInflater.inflate(R.layout.fragment_home, viewGroup, false);

        ImageView more_btn = view.findViewById(R.id.more_btn);

        more_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onMoreButtonClicked();
                }
            }
        });

        SharedPreferences preferences = requireContext().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        String username = preferences.getString("username", "");
        String userRoles = preferences.getString("userRoles", "");
        String url_imagen = preferences.getString("urlImagen", "");

        TextView welcomeText = view.findViewById(R.id.welcome_Text);
        TextView roleText = view.findViewById(R.id.role_name_text);
        ImageView perfilImg = view.findViewById(R.id.profile_imagePostuladop);

        welcomeText.setText(getString(R.string.welcome_user_name).replace("{user name}", username));
        roleText.setText(userRoles);

        Glide.with(getContext())
                .load(url_imagen)
                .into(perfilImg);

        CargarOfertas();
        return view;
    }

    private void CargarOfertas() {
        Integer idGraduado = preferences.getInt("usuarioId", -1);

        OfertasServiceApi ofertasServiceApi = RetrofitClient.getRetrofitInstance().create(OfertasServiceApi.class);
        loadingDialog.startLoagingDialog();
        // Llamar a la API para obtener las ofertas laborales
        Call<List<OfertaLaboralDTO>> call = ofertasServiceApi.getOfertaLaboralWithPostulateByGraduateId(idGraduado, "Bearer " + token);
        call.enqueue(new Callback<List<OfertaLaboralDTO>>() {
            @Override
            public void onResponse(Call<List<OfertaLaboralDTO>> call, Response<List<OfertaLaboralDTO>> response) {
                loadingDialog.cerrarLoadingDialog();
                if (response.isSuccessful()) {
                    listaOfertas = response.body();
                    if (listaOfertas != null && !listaOfertas.isEmpty()) {
                        List<OfertaLaboralDTO> latestOfertas;
                        if (listaOfertas.size() > 5) {
                            latestOfertas = listaOfertas.subList(0, 5); // Get the first 5 job offers
                        } else {
                            latestOfertas = listaOfertas; // If there are less than 5 job offers, use all of them
                        }

                        recyclerView = view.findViewById(R.id.recent_items);
//                        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
//                        recyclerView.setLayoutManager(layoutManager);

                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


                        // Initialize and set up the adapter with the latest job offers
                        ofertasAdapter = new OfertasAdapter(getContext(), latestOfertas);
                        ofertasAdapter.setOnClickListener(HomeFragment.this);
                        recyclerView.setAdapter(ofertasAdapter);
                    } else {
                        Toast.makeText(getContext(), "No hay ofertas laborales disponibles.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "No se encontraron ofertas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<OfertaLaboralDTO>> call, Throwable t) {
                System.out.println(R.string.error_conexion + t.getMessage());
                loadingDialog.showFailureDialog(getString(R.string.error_conexion), "Error:" + t.getMessage());
                // Manejar el caso de fallo en la conexión
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onVerMasClick(Integer id_Oferta) {
        Intent intent = new Intent(getActivity(), DetalleOfertaActivity.class);
        intent.putExtra("id_Oferta", id_Oferta);
        startActivity(intent);
    }

    @Override
    public void onAplicarClick(Integer id_Oferta) {

        PostulacionDTO postulacionDTO = new PostulacionDTO();
        postulacionDTO.setGraduado(usuarioId);
        postulacionDTO.setOfertaLaboral(id_Oferta);
        postulacionDTO.setEstado(String.valueOf(EstadoPostulacion.APLICANDO));

        PostulacionesServiceApi postulacionesServiceApi = RetrofitClient.getRetrofitInstance().create(PostulacionesServiceApi.class);

        Call<PostulacionDTO> call = postulacionesServiceApi.createPostulacion(postulacionDTO, "Bearer " + token);

        loadingDialog.startLoagingDialog();
        call.enqueue(new Callback<PostulacionDTO>() {
            @Override
            public void onResponse(Call<PostulacionDTO> call, Response<PostulacionDTO> response) {
                loadingDialog.showSimpleDialog("Postulación Realizada", "¡Te has postulado a esta oferta!");
                CargarOfertas();
            }

            @Override
            public void onFailure(Call<PostulacionDTO> call, Throwable t) {
//                loadingDialog.showFailureDialog("Error de conexión. Inténtalo de nuevo.");
                System.out.println("Error de conexión = " + call + t.getMessage());
                loadingDialog.showSimpleDialog("Postulación Realizada", "¡Te has postulado a esta oferta!");
                CargarOfertas();
            }
        });
    }

    public void onDestroy() {
        super.onDestroy();
    }
}
