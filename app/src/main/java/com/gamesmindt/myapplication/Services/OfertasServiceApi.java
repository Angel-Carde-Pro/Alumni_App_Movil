package com.gamesmindt.myapplication.Services;

import com.gamesmindt.myapplication.Model.Graduado;
import com.gamesmindt.myapplication.Model.LoginDTO;
import com.gamesmindt.myapplication.Model.OfertaLaboralDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.*;

public interface OfertasServiceApi {
    @GET("/ofertas-laborales/ofertas-sin-postular/{id}")
    Call<List<OfertaLaboralDTO>> getOfertaLaboralWithPostulateByGraduateId(@Path("id") int id, @Header("Authorization") String authToken);

    @GET("/{id}")
    Call<List<OfertaLaboralDTO>> getOfertaLaboralById(@Path("id") int id);

    @GET("/dto/{id}")
    Call<List<OfertaLaboralDTO>> getOfertaLaboralByIdToDTO(@Path("id") int id);

    @GET("/graduados/{id}")
    Call<List<Graduado>> getGraduadoByID(@Path("id") int id);
}
