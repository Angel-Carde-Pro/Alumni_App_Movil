package com.gamesmindt.myapplication.Services;

import com.gamesmindt.myapplication.Model.Postulacion;
import com.gamesmindt.myapplication.Model.PostulacionDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.*;

public interface PostulacionesServiceApi {
    @GET("/postulaciones/all")
    Call<List<Postulacion>> getAllPostulaciones();

    @GET("/postulaciones/all-postulations-by-graduado/{id}")
    Call<List<Postulacion>> getAllPostulacionesByGraduadoId(@Path("id") int id, @Header("Authorization") String authToken);

    @GET("/postulaciones/all-by-oferta-laboral/{id}")
    Call<List<Postulacion>> getAllPostulacionesByOfertaLaboralId(@Path("id") int id, @Header("Authorization") String authToken);

    @GET("/postulaciones/by-id/{id}")
    Call<Postulacion> getPostulacionById(@Path("id") int id, @Header("Authorization") String authToken);

    @GET("/postulaciones/count")
    Call<Integer> countPostulaciones();

    @GET("/postulaciones/count-by-date/{date}")
    Call<Integer> countByDate(@Path("date") String date, @Header("Authorization") String authToken);

    @POST("/postulaciones")
    Call<PostulacionDTO> createPostulacion(@Body PostulacionDTO postulacion, @Header("Authorization") String authToken);

    @PUT("/postulaciones/update/{id}")
    Call<PostulacionDTO> updatePostulacion(@Path("id") int id, @Body Postulacion postulacion, @Header("Authorization") String authToken);

    @PUT("/postulaciones/update-estado/{id}")
    Call<PostulacionDTO> updateStatePostulacion(@Path("id") int id, @Body PostulacionDTO postulacion, @Header("Authorization") String authToken);

    @DELETE("/postulaciones/delete/{id}")
    Call<Postulacion> deletePostulacion(@Path("id") int id, @Header("Authorization") String authToken);
}
