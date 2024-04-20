package com.gamesmindt.myapplication.Services;

import com.gamesmindt.myapplication.Model.GraduadoDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GraduadoServiceApi {
    @GET("/graduados/{id}")
    Call<GraduadoDTO> getGraduadoDTOById(@Path("id") Long id, @Header("Authorization") String authToken);

    @GET("/graduados")
    Call<List<GraduadoDTO>> searchGraduadosByUsuario(@Query("usuario") String usuario, @Header("Authorization") String authToken);
}
