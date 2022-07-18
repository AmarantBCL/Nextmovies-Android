package com.example.android.nextmovies.network;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiFactory {
    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static final String TRAILERS_URL = "https://api.themoviedb.org/3/movie/616037/";
    public static final String POSTER_URL = "https://image.tmdb.org/t/p/w500/";

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build();

    public static final ApiService apiService = retrofit.create(ApiService.class);
}
