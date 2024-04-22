package com.gamesmindt.myapplication.Services;

import com.gamesmindt.myapplication.Model.MailRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface RecoverPassServiceApi {
    @POST("/mail/recovery-password")
    Call<String> sendTokenMail(@Body MailRequest request, @Header("Authorization") String authToken);
}
