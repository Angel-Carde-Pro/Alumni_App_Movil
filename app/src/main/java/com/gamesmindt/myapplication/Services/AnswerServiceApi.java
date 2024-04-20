package com.gamesmindt.myapplication.Services;

import com.gamesmindt.myapplication.Model.AnswerSearchDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AnswerServiceApi {
    @POST("/api/answer/save")
    Call<AnswerSearchDTO> saveAnswer(@Body AnswerSearchDTO answerSearchDTO, @Header("Authorization") String authToken);
}
