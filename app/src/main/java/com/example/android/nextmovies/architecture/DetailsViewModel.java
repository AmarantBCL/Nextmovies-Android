package com.example.android.nextmovies.architecture;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.android.nextmovies.database.MovieDao;
import com.example.android.nextmovies.database.MovieDatabase;
import com.example.android.nextmovies.network.ApiFactory;
import com.example.android.nextmovies.pojo.Actor;
import com.example.android.nextmovies.pojo.CastResponse;
import com.example.android.nextmovies.pojo.Movie;
import com.example.android.nextmovies.pojo.Review;
import com.example.android.nextmovies.pojo.ReviewResponse;
import com.example.android.nextmovies.pojo.Trailer;
import com.example.android.nextmovies.pojo.TrailerResponse;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DetailsViewModel extends AndroidViewModel {
    private static final String TAG = "DetailsViewModel";

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final MutableLiveData<List<Actor>> actors = new MutableLiveData<>();
    private final MutableLiveData<List<Trailer>> trailers = new MutableLiveData<>();
    private final MutableLiveData<List<Review>> reviews = new MutableLiveData<>();
    private final MovieDao movieDao;

    public DetailsViewModel(@NonNull Application application) {
        super(application);
        movieDao = MovieDatabase.getInstance(application).getMovieDao();
    }

    public LiveData<List<Actor>> getActors() {
        return actors;
    }

    public LiveData<List<Trailer>> getTrailers() {
        return trailers;
    }

    public LiveData<List<Review>> getReviews() {
        return reviews;
    }

    public LiveData<Movie> getFavoriteMovie(int id) {
        return movieDao.getFavorite(id);
    }

    public void addFavoriteMovie(Movie movie) {
        Disposable disposable = movieDao.addFavorite(movie)
                .subscribeOn(Schedulers.io())
                .subscribe();
        compositeDisposable.add(disposable);
    }

    public void deleteFavoriteMovie(int id) {
        Disposable disposable = movieDao.deleteFavorite(id)
                .subscribeOn(Schedulers.io())
                .subscribe();
        compositeDisposable.add(disposable);
    }

    public void loadCast(int id) {
        Disposable disposable = ApiFactory.apiService.loadCast(id, ApiFactory.LANG)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<CastResponse, List<Actor>>() {
                    @Override
                    public List<Actor> apply(CastResponse castResponse) throws Throwable {
                        return castResponse.getActors();
                    }
                })
                .subscribe(new Consumer<List<Actor>>() {
                    @Override
                    public void accept(List<Actor> actorList) throws Throwable {
                        actors.setValue(actorList);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d(TAG, throwable.toString());
                    }
                });
        compositeDisposable.add(disposable);
    }

    public void loadTrailers(int id) {
        Disposable disposable = ApiFactory.apiService.loadTrailers(id, ApiFactory.LANG)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<TrailerResponse, List<Trailer>>() {
                    @Override
                    public List<Trailer> apply(TrailerResponse trailerResponse) throws Throwable {
                        return trailerResponse.getTrailers();
                    }
                })
                .subscribe(new Consumer<List<Trailer>>() {
                    @Override
                    public void accept(List<Trailer> trailerList) throws Throwable {
                        trailers.setValue(trailerList);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d(TAG, throwable.toString());
                    }
                });
        compositeDisposable.add(disposable);
    }

    public void loadReviews(int id) {
        Disposable disposable = ApiFactory.apiService.loadReviews(id, ApiFactory.LANG)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<ReviewResponse, List<Review>>() {
                    @Override
                    public List<Review> apply(ReviewResponse reviewResponse) throws Throwable {
                        return reviewResponse.getReviews();
                    }
                })
                .subscribe(new Consumer<List<Review>>() {
                    @Override
                    public void accept(List<Review> reviewList) throws Throwable {
                        reviews.setValue(reviewList);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d(TAG, throwable.toString());
                    }
                });
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
