package com.gamesmindt.myapplication.Services;

import com.gamesmindt.myapplication.Model.Carrera;
import com.gamesmindt.myapplication.Model.OfertaDetalle;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface CarreraServiceApi {
    @GET("/carreras")
    Call<List<Carrera>> getCarreras(@Header("Authorization") String authToken);
}
