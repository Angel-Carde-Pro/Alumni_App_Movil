package com.gamesmindt.myapplication.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gamesmindt.myapplication.Model.EstadoPostulacion;
import com.gamesmindt.myapplication.Model.Postulacion;
import com.gamesmindt.myapplication.Model.PostulacionDTO;
import com.gamesmindt.myapplication.R;
import com.gamesmindt.myapplication.Services.PostulacionesServiceApi;
import com.gamesmindt.myapplication.activities.DetallePostulacionActivity;
import com.gamesmindt.myapplication.adapters.PostulacionesAdapter;
import com.gamesmindt.myapplication.configs.LoadingDialog;
import com.gamesmindt.myapplication.configs.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostulacionesFragment extends Fragment implements PostulacionesAdapter.OnVerMasClickListener {

    private RecyclerView recyclerView;
    private PostulacionesAdapter postulacionesAdapter;
    private SearchView searchView;
    List<Postulacion> filteredList = new ArrayList<>();
    SharedPreferences preferences;
    String usuario = "";
    String roles = "";
    String accessToken = "";
    String token = "";
    Integer usuarioId = -1;
    private List<Postulacion> postulacionList;
    PostulacionesServiceApi postulacionesServiceApi = RetrofitClient.getRetrofitInstance().create(PostulacionesServiceApi.class);
    private LoadingDialog loadingDialog;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        loadingDialog = new LoadingDialog(requireActivity());

        preferences = requireActivity().getSharedPreferences("user_data", MODE_PRIVATE);
        usuario = preferences.getString("username", "");
        roles = preferences.getString("userRoles", "");
        token = preferences.getString("accessToken", "");
        accessToken = preferences.getString("accessToken", "");
        usuarioId = preferences.getInt("usuarioId", -1);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View view = layoutInflater.inflate(R.layout.fragment_listar_postulaciones, viewGroup, false);
        recyclerView = view.findViewById(R.id.listPostulaciones);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ImageButton actualizarbtn = view.findViewById(R.id.updateBtn);
        actualizarbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CargarPostulaciones();
            }
        });

        //Preparar el buscador
        setupFilter(view);

        CargarPostulaciones();
        return view;
    }

    private void CargarPostulaciones() {
        loadingDialog.startLoagingDialog();
        System.out.println("usuarioID: " + usuarioId);
        // Llamar a la API para obtener las ofertas laborales
        Call<List<Postulacion>> call = postulacionesServiceApi.getAllPostulacionesByGraduadoId(usuarioId, "Bearer " + accessToken);
        call.enqueue(new Callback<List<Postulacion>>() {
            @Override
            public void onResponse(Call<List<Postulacion>> call, Response<List<Postulacion>> response) {
                loadingDialog.cerrarLoadingDialog();
                if (response.isSuccessful()) {
                    postulacionList = response.body();
                    if (postulacionList != null && !postulacionList.isEmpty()) {

                        postulacionesAdapter = new PostulacionesAdapter(getContext(), postulacionList);

                        postulacionesAdapter.setOnVerMasClickListener(PostulacionesFragment.this);

                        recyclerView.setAdapter(postulacionesAdapter);
                    } else {
                        Toast.makeText(getContext(), "No hay ninguna postulación registrada.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "No se encontraron ofertas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Postulacion>> call, Throwable t) {
                System.out.println(R.string.error_conexion + t.getMessage());
                loadingDialog.showFailureDialog(getString(R.string.error_conexion), "Error:" + t.getMessage());
                // Manejar el caso de fallo en la conexión
                t.printStackTrace();
            }
        });
    }

    private void setupFilter(View view) {
        //Preparar el buscador
        searchView = view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filteredList.clear();

                if (newText.isEmpty()) {
                    // Si el texto de búsqueda está vacío, mostrar la lista completa en su orden original
                    filteredList.addAll(postulacionList);
                } else {
                    // Filtrar la lista de postulaciones según el texto de búsqueda
                    for (Postulacion postulacion : postulacionList) {
                        if (postulacionMatchesFilter(newText, postulacion)) {
                            filteredList.add(postulacion);
                        }
                    }
                }

                // Notificar al adaptador del cambio en la lista filtrada
                postulacionesAdapter.filterList(filteredList);
                return true;
            }
        });
    }

    private boolean postulacionMatchesFilter(String searchText, Postulacion postulacion) {
        if (postulacion == null) {
            return false;
        }

        String estado = postulacion.getEstado() != null ? postulacion.getEstado().toLowerCase() : "";
        String salario = postulacion.getOfertaLaboral() != null ? String.valueOf(postulacion.getOfertaLaboral().getSalario()).toLowerCase() : "";
        String cargo = postulacion.getOfertaLaboral() != null && postulacion.getOfertaLaboral().getCargo() != null ? postulacion.getOfertaLaboral().getCargo().toLowerCase() : "";
        String nombreEmpresa = postulacion.getOfertaLaboral() != null && postulacion.getOfertaLaboral().getEmpresa() != null && postulacion.getOfertaLaboral().getEmpresa().getNombre() != null ? postulacion.getOfertaLaboral().getEmpresa().getNombre().toLowerCase() : "";

        return estado.contains(searchText.toLowerCase()) ||
                salario.contains(searchText.toLowerCase()) ||
                cargo.contains(searchText.toLowerCase()) ||
                nombreEmpresa.contains(searchText.toLowerCase());
    }


    @Override
    public void onVerMasClick(Integer id_postulacion) {
        System.out.println("id_postulacion = " + id_postulacion);
        Intent intent = new Intent(getActivity(), DetallePostulacionActivity.class);
        intent.putExtra("id_postulacion", id_postulacion);
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(Integer id_Oferta, String estado) {
        System.out.println("id_Oferta = " + id_Oferta);
        System.out.println("id_usuario = " + usuarioId);

        PostulacionDTO postulacionDTO = new PostulacionDTO();
        postulacionDTO.setGraduado(usuarioId);
        postulacionDTO.setOfertaLaboral(id_Oferta);

        if (estado.equals("CANCELADA_POR_GRADUADO")) {
            postulacionDTO.setEstado(EstadoPostulacion.APLICANDO.toString());
        } else {
            postulacionDTO.setEstado(EstadoPostulacion.CANCELADA_POR_GRADUADO.toString());
        }

        PostulacionesServiceApi postulacionesServiceApi = RetrofitClient.getRetrofitInstance().create(PostulacionesServiceApi.class);

        Call<PostulacionDTO> call = postulacionesServiceApi.updateStatePostulacion(id_Oferta, postulacionDTO, "Bearer " + token);

        loadingDialog.startLoagingDialog();
        call.enqueue(new Callback<PostulacionDTO>() {
            @Override
            public void onResponse(Call<PostulacionDTO> call, Response<PostulacionDTO> response) {
                if (estado.equals("CANCELADA_POR_GRADUADO")) {
                    loadingDialog.showSuccessDialog("Postulación Realizada", "¡La postulación fue realizada correctamente!");
                } else {
                    loadingDialog.showSuccessDialog("Postulación Cancelada", "¡La postulación fue cancelada correctamente!");
                }
                CargarPostulaciones();
            }

            @Override
            public void onFailure(Call<PostulacionDTO> call, Throwable t) {
                if (estado.equals("CANCELADA_POR_GRADUADO")) {
                    loadingDialog.showSuccessDialog("Postulación Realizada", "¡La postulación fue realizada correctamente!");
                } else {
                    loadingDialog.showSuccessDialog("Postulación Cancelada", "¡La postulación fue cancelada correctamente!");
                }
                System.out.println("Error de conexión = " + t);
//                loadingDialog.dismissDialog();
                CargarPostulaciones();
            }
        });
    }

    public void onDestroy() {
        super.onDestroy();
    }
}
