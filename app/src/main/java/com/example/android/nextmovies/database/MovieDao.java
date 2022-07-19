package com.example.android.nextmovies.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.android.nextmovies.pojo.Movie;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM favorites")
    LiveData<List<Movie>> getAllFavorites();

    @Query("SELECT * FROM favorites WHERE id = :id")
    LiveData<Movie> getFavorite(int id);

    @Insert
    Completable addFavorite(Movie movie);

    @Query("DELETE FROM favorites WHERE id = :id")
    Completable deleteFavorite(int id);
}
