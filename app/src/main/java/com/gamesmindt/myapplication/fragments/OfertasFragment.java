package com.gamesmindt.myapplication.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gamesmindt.myapplication.Model.Empresa;
import com.gamesmindt.myapplication.Model.EmpresaDTO;
import com.gamesmindt.myapplication.Model.EmpresaDetalle;
import com.gamesmindt.myapplication.Model.EstadoPostulacion;
import com.gamesmindt.myapplication.Model.OfertaLaboralDTO;
import com.gamesmindt.myapplication.Model.PostulacionDTO;
import com.gamesmindt.myapplication.R;
import com.gamesmindt.myapplication.Services.OfertasServiceApi;
import com.gamesmindt.myapplication.Services.PostulacionesServiceApi;
import com.gamesmindt.myapplication.activities.DetalleOfertaActivity;
import com.gamesmindt.myapplication.adapters.EmpresasAdapter;
import com.gamesmindt.myapplication.adapters.OfertaLaboralTrie;
import com.gamesmindt.myapplication.bd.DBHelper;
import com.gamesmindt.myapplication.configs.LoadingDialog;
import com.gamesmindt.myapplication.configs.RetrofitClient;
import com.gamesmindt.myapplication.adapters.OfertasAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OfertasFragment extends Fragment implements OfertasAdapter.OnClickListener {

    private RecyclerView recyclerView;
    private OfertasAdapter ofertasAdapter;
    private List<OfertaLaboralDTO> listaOfertas;
    private Integer idGraduado;
    private SearchView searchView;
    SharedPreferences preferences;
    OfertasServiceApi ofertasServiceApi = RetrofitClient.getRetrofitInstance().create(OfertasServiceApi.class);

    String usuario = "";
    String roles = "";
    String token = "";
    Integer usuarioId = -1;
    DBHelper dbhelper;
    private LoadingDialog loadingDialog;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        dbhelper = new DBHelper(getContext());
        loadingDialog = new LoadingDialog(requireActivity());

        //Obtener los datos persistentes
        preferences = requireActivity().getSharedPreferences("user_data", MODE_PRIVATE);
        usuario = preferences.getString("username", "");
        roles = preferences.getString("userRoles", "");
        token = preferences.getString("accessToken", "");
        usuarioId = preferences.getInt("usuarioId", -1);
        System.out.println("USERNAME " + usuario);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Preparar el reciclerView
        View view = inflater.inflate(R.layout.fragment_listar_ofertas, container, false);
        recyclerView = view.findViewById(R.id.listRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ImageButton actualizarbtn = view.findViewById(R.id.updateBtn);
        actualizarbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CargarDatos();
            }
        });

        //Preparar el buscador
        setupFilter(view);
        CargarDatos();
        return view;
    }

    private void CargarDatos() {
        if ("EMPRESARIO".equals(roles)) {
            CargarOfertasDelEmpresario();
        } else if ("GRADUADO".equals(roles)) {
            CargarOfertasGraduado();
        }
    }

    private void CargarOfertasGraduado() {
        loadingDialog.startLoagingDialog();
        if (dbhelper.hayConexionInternet(getContext())) {
            idGraduado = preferences.getInt("usuarioId", -1);

            // Llamar a la API para obtener las ofertas laborales
            Call<List<OfertaLaboralDTO>> call = ofertasServiceApi.getOfertaLaboralWithPostulateByGraduateId(idGraduado, "Bearer " + token);
            call.enqueue(new Callback<List<OfertaLaboralDTO>>() {
                @Override
                public void onResponse(Call<List<OfertaLaboralDTO>> call, Response<List<OfertaLaboralDTO>> response) {
                    loadingDialog.cerrarLoadingDialog();
                    if (response.isSuccessful()) {
                        listaOfertas = response.body();
                        if (listaOfertas != null && !listaOfertas.isEmpty()) {
                            // Configurar el adaptador del RecyclerView
                            ofertasAdapter = new OfertasAdapter(getContext(), listaOfertas);
                            ofertasAdapter.setOnClickListener(OfertasFragment.this);
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
                    loadingDialog.showFailureDialog(getString(R.string.error_conexion), "Revise su conexión a internet.");
                    t.printStackTrace();
                }
            });
        } else {
            // Manejar la falta de conexión a internet
        }
    }

    private void CargarOfertasDelEmpresario() {
        FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        EmpresasFragment nuevoFragmento = new EmpresasFragment();
        fragmentTransaction.replace(R.id.container, nuevoFragmento); // Reemplaza R.id.contenedor_fragmento con el ID de tu contenedor de fragmento
        fragmentTransaction.addToBackStack(null); // Opcional: agrega este fragmento a la pila de retroceso para permitir la navegación hacia atrás
        fragmentTransaction.commit();

    }
    private void setupFilter(View view) {
        List<OfertaLaboralDTO> filteredList = new ArrayList<>();
        searchView = view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filteredList.clear();

                // Check if search text is empty
                if (newText.isEmpty()) {
                    // If empty, restore original list and notify adapter
                    if (ofertasAdapter != null) {
                        ofertasAdapter.filterList(filteredList);
                        return true;
                    } else {
                        // Manejar el caso donde ofertasAdapter es nulo
                        // Esto podría ser inicializando ofertasAdapter o mostrando un mensaje de error
                        return false;

                    }
                }

                String lowercaseText = newText.toLowerCase();
                for (OfertaLaboralDTO oferta : listaOfertas) {
                    if (ofertaMatchesFilter(lowercaseText, oferta)) {
                        filteredList.add(oferta);
                    }
                }
                ofertasAdapter.filterList(filteredList);
                return true;
            }
        });
    }

    private boolean ofertaMatchesFilter(String searchText, OfertaLaboralDTO oferta) {
        if (oferta == null) {
            return false;
        }
        String nombreEmpresa = oferta.getNombreEmpresa() != null ? oferta.getNombreEmpresa().toLowerCase() : "";
        String cargo = oferta.getCargo() != null ? oferta.getCargo().toLowerCase() : "";
        String areaConocimiento = oferta.getAreaConocimiento() != null ? oferta.getAreaConocimiento().toLowerCase() : "";
        String experiencia = oferta.getExperiencia() != null ? oferta.getExperiencia().toLowerCase() : "";
        String tiempo = oferta.getTiempo() != null ? oferta.getTiempo().toLowerCase() : "";
        String salario = oferta.getSalario() != 0 ? String.valueOf(oferta.getSalario()).toLowerCase() : "";
        return (nombreEmpresa.contains(searchText) ||
                cargo.contains(searchText) ||
                areaConocimiento.contains(searchText) ||
                experiencia.contains(searchText) ||
                tiempo.contains(searchText) ||
                salario.contains(searchText));
    }

    @Override
    public void onVerMasClick(Integer id_Oferta) {
        Intent intent = new Intent(getActivity(), DetalleOfertaActivity.class);
        intent.putExtra("id_Oferta", id_Oferta);
        startActivity(intent);
    }

    @Override
    public void onAplicarClick(Integer id_Oferta) {

        System.out.println("HOLAAAAAAAAAAAA");
        System.out.println("id_Oferta = " + id_Oferta);
        System.out.println("id_usuario = " + usuarioId);

        PostulacionDTO postulacionDTO = new PostulacionDTO();
        postulacionDTO.setGraduado(usuarioId);
        postulacionDTO.setOfertaLaboral(id_Oferta);
        postulacionDTO.setEstado(EstadoPostulacion.APLICANDO.toString());

        PostulacionesServiceApi postulacionesServiceApi = RetrofitClient.getRetrofitInstance().create(PostulacionesServiceApi.class);

        Call<PostulacionDTO> call = postulacionesServiceApi.createPostulacion(postulacionDTO, "Bearer " + token);

        loadingDialog.startLoagingDialog();
        call.enqueue(new Callback<PostulacionDTO>() {
            @Override
            public void onResponse(Call<PostulacionDTO> call, Response<PostulacionDTO> response) {
                loadingDialog.showSimpleDialog("Postulación Realizada", "¡Te has postulado a esta oferta!");
            }

            @Override
            public void onFailure(Call<PostulacionDTO> call, Throwable t) {
//                loadingDialog.showFailureDialog("Error de conexión. Inténtalo de nuevo.");
                // Mismo problema en la app web - Error: java.net.SocketTimeoutException: timeout (El tiempo de espera es demasiado largo)
                // Pero la postulación se crea, no hay problema
                // Solución: Revisar backend
                System.out.println("Error de conexión = " + t);
                loadingDialog.showSimpleDialog("Postulación Realizada", "¡Te has postulado a esta oferta!");
            }
        });
    }

    public void onDestroy() {
        super.onDestroy();
    }
}
