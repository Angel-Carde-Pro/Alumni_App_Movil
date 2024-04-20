package com.gamesmindt.myapplication.configs;

import com.gamesmindt.myapplication.Services.UserServiceApi;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;
    //    private static final String BASE_URL = "http://alumnibacked23-env.eba-gemair7x.us-east-1.elasticbeanstalk.com";
    private static final String BASE_URL = "http://alumnibacked23-env-43.eba-gemair7x.us-east-1.elasticbeanstalk.com/";
//    private static final String BASE_URL = "http://192.168.18.61:8080/";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            // Configurar el interceptor para agregar el token de acceso
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY); // Esto es para ver los logs de las solicitudes y respuestas

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(loggingInterceptor); // Agregar el interceptor de logueo
            // Agrega tu interceptor de autorización aquí si tienes uno

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(httpClient.build()) // Establecer el cliente OkHttpClient con el interceptor
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static UserServiceApi getUserService() {
        UserServiceApi userServiceApi = getRetrofitInstance().create(UserServiceApi.class);
        return userServiceApi;
    }
}