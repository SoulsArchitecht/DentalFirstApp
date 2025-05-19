package ru.sshibko.dentalfirstapp.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.sshibko.dentalfirstapp.model.SearchResponse;

public interface ApiService {

    @GET("search")
    Call<SearchResponse> searchProducts(@Query("query") String query);
}
