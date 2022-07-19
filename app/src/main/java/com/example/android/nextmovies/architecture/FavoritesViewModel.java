package com.example.android.nextmovies.architecture;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.android.nextmovies.database.MovieDao;
import com.example.android.nextmovies.database.MovieDatabase;
import com.example.android.nextmovies.pojo.Movie;

import java.util.List;

import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavoritesViewModel extends AndroidViewModel {
    private final MovieDao movieDao;

    public FavoritesViewModel(@NonNull Application application) {
        super(application);
        movieDao = MovieDatabase.getInstance(application).getMovieDao();
    }

    public LiveData<List<Movie>> getFavorites() {
        return movieDao.getAllFavorites();
    }
}
