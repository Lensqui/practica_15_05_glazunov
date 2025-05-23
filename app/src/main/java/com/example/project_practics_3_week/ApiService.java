package com.example.project_practics_3_week;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;

public interface ApiService {
    @GET("rest/v1/users?select=*")
    Call<List<User>> getUsers(
            @Header("apikey") String apiKey,
            @Header("Authorization") String authorization
    );
}
