package com.gamesmindt.myapplication.Services;

import com.gamesmindt.myapplication.Model.LoginDTO;
import com.gamesmindt.myapplication.Model.Persona;
import com.gamesmindt.myapplication.Model.PersonaDTO;
import com.gamesmindt.myapplication.Model.UsuarioDTO;
import com.gamesmindt.myapplication.Model.Usuario;
import com.gamesmindt.myapplication.Model.UsuarioDTP;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.*;

public interface UserServiceApi {
    @POST("auth/login")
    Call<LoginResponse> getLoginInformation(@Body LoginDTO loginDTO);

    @GET("/usuarios")
    Call<List<Usuario>> getUsers();

    @GET("/usuarios/{id}")
    Call<Usuario> getUserById(@Path("id") int id);

    @GET("/usuarios/by-username/{username}")
    Call<Usuario> getUsuarioByUsername(@Path("username") String username, @Header("Authorization") String authToken
    );

    @GET("/usuarios/resumen/{id}")
    Call<UsuarioDTO> getUserDTOById(@Path("id") long id);

    @POST("/users")
    Call<Usuario> createPerson(@Body Usuario usuario);

    @PUT("/usuarios/{id}")
    Call<Void> updateUser(@Path("id") int id, @Body Usuario usuario, @Header("Authorization") String authToken);

    @PUT("/usuarios/photo/{id}/{route}")
    Call<Void> updateUserPhoto(@Path("id") int id, @Path("route") String route);

    @GET("/usuarios/by-username/{username}")
    Call<Usuario> getUserByUsername(@Path("username") String username, @Header("Authorization") String authToken);

    @GET("/usuarios/by-username/{username}")
    Call<UsuarioDTP> getUserDTOByUsername(@Path("username") String username, @Header("Authorization") String authToken);

    @PUT("/personas/{id}")
    Call<Void> updatePerson(@Path("id") int id, @Body PersonaDTO persona, @Header("Authorization") String authToken);

}
