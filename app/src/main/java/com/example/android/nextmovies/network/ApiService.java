package com.example.android.nextmovies.network;

import com.example.android.nextmovies.pojo.CastResponse;
import com.example.android.nextmovies.pojo.MovieResponse;
import com.example.android.nextmovies.pojo.ReviewResponse;
import com.example.android.nextmovies.pojo.TrailerResponse;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("movie/{sort_method}?api_key=692a162b1d95c63d2cca4046b3e7f851")
    Single<MovieResponse> loadMovies(@Path("sort_method") String sort, @Query("page") int page,
                                     @Query("language") String lang);

    @GET("movie/{movie_id}/videos?api_key=692a162b1d95c63d2cca4046b3e7f851")
    Single<TrailerResponse> loadTrailers(@Path("movie_id") int id, @Query("language") String lang);

    @GET("movie/{movie_id}/reviews?api_key=692a162b1d95c63d2cca4046b3e7f851")
    Single<ReviewResponse> loadReviews(@Path("movie_id") int id, @Query("language") String lang);

    @GET("movie/{movie_id}/credits?api_key=692a162b1d95c63d2cca4046b3e7f851")
    Single<CastResponse> loadCast(@Path("movie_id") int id, @Query("language") String lang);

    @GET("search/movie?api_key=692a162b1d95c63d2cca4046b3e7f851")
    Single<MovieResponse> searchMovies(@Query("query") String query);
}
