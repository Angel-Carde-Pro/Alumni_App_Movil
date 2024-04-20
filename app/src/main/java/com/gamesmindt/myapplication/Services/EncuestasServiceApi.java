package com.gamesmindt.myapplication.Services;

import com.gamesmindt.myapplication.Model.Survey;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface EncuestasServiceApi {
    @GET("/api/surveys/withQuestionsAndOptions")
    Call<List<Survey>> getAllSurveysWithQuestionsAndOptions(@Header("Authorization") String authToken);
    @GET("/api/surveys/{id}")
    Call<Survey> getSurveyById(@Path("id") int surveyId, @Header("Authorization") String authToken);
}
