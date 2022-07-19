package com.example.android.nextmovies.network;

import com.example.android.nextmovies.pojo.MovieResponse;
import com.example.android.nextmovies.pojo.ReviewResponse;
import com.example.android.nextmovies.pojo.TrailerResponse;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("movie/popular?api_key=692a162b1d95c63d2cca4046b3e7f851&language=ru-RU")
    Single<MovieResponse> loadMovies(@Query("page") int page);

    @GET("movie/{movie_id}/videos?api_key=692a162b1d95c63d2cca4046b3e7f851&language=ru-RU")
    Single<TrailerResponse> loadTrailers(@Path("movie_id") int id);

    @GET("movie/{movie_id}/reviews?api_key=692a162b1d95c63d2cca4046b3e7f851")
    Single<ReviewResponse> loadReviews(@Path("movie_id") int id);
}
