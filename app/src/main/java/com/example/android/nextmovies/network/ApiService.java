package com.example.android.nextmovies.network;

import com.example.android.nextmovies.pojo.MovieResponse;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("movie/popular?api_key=692a162b1d95c63d2cca4046b3e7f851&language=ru-RU")
    Single<MovieResponse> loadMovies(@Query("page") int page);
}
