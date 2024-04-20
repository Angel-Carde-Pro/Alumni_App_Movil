package com.gamesmindt.myapplication.Services;

import com.gamesmindt.myapplication.Model.OfertaDetalle;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface DetalleOfertasServiceApi {
    @GET("/ofertas-laborales/{id}")
    Call<OfertaDetalle> getOfertaLaboralById(@Path("id") int id, @Header("Authorization") String authToken);
}
