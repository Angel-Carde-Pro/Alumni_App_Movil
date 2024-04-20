package com.gamesmindt.myapplication.Services;

import com.gamesmindt.myapplication.Model.Empresa;
import com.gamesmindt.myapplication.Model.EmpresaDTO;
import com.gamesmindt.myapplication.Model.Empresario;
import com.gamesmindt.myapplication.Model.Graduado;
import com.gamesmindt.myapplication.Model.OfertaDetalle;
import com.gamesmindt.myapplication.Model.OfertaLaboralDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface EmpresarioServiceApi {
    @GET("/empresas/by-usuario/{nombreUsuario}")
    Call<List<EmpresaDTO>> getEmpresaByUsuario(@Path("nombreUsuario")String usuario, @Header("Authorization") String authToken);

    @GET("/ofertas-laborales/empresa/{nombreEmpresa}")
    Call<List<OfertaDetalle>> getOfertasByEmpresa(@Path("nombreEmpresa") String empresa, @Header("Authorization") String authToken);

    @GET("/ofertas-laborales/graduados/{ofertaId}")
    Call<List<Graduado>> getGraduadosByOfertaId(@Path("ofertaId") String ofertaId, @Header("Authorization") String authToken);

}
