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

import com.gamesmindt.myapplication.Model.Survey;
import com.gamesmindt.myapplication.R;
import com.gamesmindt.myapplication.Services.EncuestasServiceApi;
import com.gamesmindt.myapplication.activities.DetalleEncuestaActivity;
import com.gamesmindt.myapplication.adapters.EncuetasAdapter;
import com.gamesmindt.myapplication.configs.LoadingDialog;
import com.gamesmindt.myapplication.configs.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EncuestasFragment extends Fragment implements EncuetasAdapter.OnClickListener {

    private RecyclerView recyclerView;
    private EncuetasAdapter encuetasAdapter;
    private List<Survey> listaEncuestas;
    private SearchView searchView;
    SharedPreferences preferences;
    EncuestasServiceApi encuestasServiceApi = RetrofitClient.getRetrofitInstance().create(EncuestasServiceApi.class);
    private LoadingDialog loadingDialog;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        loadingDialog = new LoadingDialog(requireActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Preparar el reciclerView
        View view = inflater.inflate(R.layout.fragment_listar_encuestas, container, false);
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
        preferences = requireActivity().getSharedPreferences("user_data", MODE_PRIVATE);
        String token = preferences.getString("accessToken", "");
        loadingDialog.startLoagingDialog();

        Call<List<Survey>> call = encuestasServiceApi.getAllSurveysWithQuestionsAndOptions("Bearer " + token);
        call.enqueue(new Callback<List<Survey>>() {
            @Override
            public void onResponse(Call<List<Survey>> call, Response<List<Survey>> response) {
                loadingDialog.cerrarLoadingDialog();
                if (response.isSuccessful()) {
                    listaEncuestas = response.body();
                    System.out.println("listaEncuestas.size() = " + listaEncuestas.size());
                    if (listaEncuestas != null && !listaEncuestas.isEmpty()) {
                        List<Survey> filteredSurveys = new ArrayList<>();
                        for (Survey survey : listaEncuestas) {
                            if (survey.getEstado()) {
                                filteredSurveys.add(survey);
                            }
                        }
                        // Mostrar solo las encuestas con estado true en el RecyclerView
                        encuetasAdapter = new EncuetasAdapter(getContext(), filteredSurveys);
                        encuetasAdapter.setOnClickListener(EncuestasFragment.this);
                        recyclerView.setAdapter(encuetasAdapter);
                    } else {
                        Toast.makeText(getContext(), "No hay encuestas disponibles.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "No se encontraron encuestas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Survey>> call, Throwable t) {
                loadingDialog.showFailureDialog(getString(R.string.error_conexion), "Revise su conexión a internet.");
                t.printStackTrace();
            }
        });
    }

    private void setupFilter(View view) {
        List<Survey> filteredList = new ArrayList<>();
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
                    encuetasAdapter.filterList(listaEncuestas);
                    return true;
                }

                String lowercaseText = newText.toLowerCase();
                for (Survey oferta : listaEncuestas) {
                    if (ofertaMatchesFilter(lowercaseText, oferta)) {
                        filteredList.add(oferta);
                    }
                }
                encuetasAdapter.filterList(filteredList);
                return true;
            }
        });
    }

    private boolean ofertaMatchesFilter(String searchText, Survey encuesta) {
        if (encuesta == null) {
            return false;
        }
        String title = encuesta.getTitle() != null ? encuesta.getTitle().toLowerCase() : "";
        String desc = encuesta.getDescription() != null ? encuesta.getDescription().toLowerCase() : "";
        return (title.contains(searchText) ||
                desc.contains(searchText));
    }

    @Override
    public void onResponderClick(Integer id_Encuesta) {
        Intent intent = new Intent(getActivity(), DetalleEncuestaActivity.class);
        intent.putExtra("id_Encuesta", id_Encuesta);
        startActivity(intent);
    }

    public void onDestroy() {
        super.onDestroy();
    }
}
