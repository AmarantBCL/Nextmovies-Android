package com.example.android.nextmovies.architecture;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.android.nextmovies.network.ApiFactory;
import com.example.android.nextmovies.pojo.Movie;
import com.example.android.nextmovies.pojo.MovieResponse;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainViewModel extends AndroidViewModel {
    private static final String LOG_TAG = "MainViewModel";
    private static final String SORT_POPULAR = "popular";
    private static final String SORT_TOP_RATED = "top_rated";

    private final MutableLiveData<List<Movie>> movies = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> isSearching = new MutableLiveData<>(false);
    private final MutableLiveData<String> sort = new MutableLiveData<>(SORT_POPULAR);
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private int page = 1;
    private String searchQuery;
    private boolean reachedEnd = false;

    public MainViewModel(@NonNull Application application) {
        super(application);
        loadMovies();
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<Boolean> getIsSearching() {
        return isSearching;
    }

    public LiveData<String> getSort() {
        return sort;
    }

    public void switchSorting(boolean isChecked) {
        clearMovies();
        sort.setValue(isChecked ? SORT_TOP_RATED : SORT_POPULAR);
    }

    public void loadMovies() {
        Boolean loading = isLoading.getValue();
        if (loading != null && loading) return;
        if (isSearching.getValue() == true) {
            searchMovie(searchQuery);
//            Log.d("SDFHDSL", page + "");
            return;
        }
        Disposable disposable = ApiFactory.apiService.loadMovies(sort.getValue(), page, ApiFactory.LANG)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Throwable {
                        isLoading.setValue(true);
                    }
                })
                .doAfterTerminate(new Action() {
                    @Override
                    public void run() throws Throwable {
                        isLoading.setValue(false);
                    }
                })
                .subscribe(new Consumer<MovieResponse>() {
                    @Override
                    public void accept(MovieResponse movieResponse) throws Throwable {
                        List<Movie> loadedMovies = movies.getValue();
                        if (loadedMovies != null) {
                            loadedMovies.addAll(movieResponse.getMovies());
                            movies.setValue(loadedMovies);
                        } else {
                            movies.setValue(movieResponse.getMovies());
                        }
                        page++;
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d(LOG_TAG, throwable.toString());
                    }
                });
        compositeDisposable.add(disposable);
    }

    public void preStartSearch() {
        isSearching.setValue(true);
        clearMovies();
    }

    public void startSearch(String name) {
        preStartSearch();
        searchQuery = name;
        searchMovie(name);
    }

    public void stopSearch() {
        isSearching.setValue(false);
        clearMovies();
        loadMovies();
    }

    public void searchMovie(String name) {
        if (reachedEnd) return;
        Disposable disposable = ApiFactory.apiService.searchMovies(name, page, ApiFactory.LANG)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Throwable {
                        isLoading.setValue(true);
                    }
                })
                .doAfterTerminate(new Action() {
                    @Override
                    public void run() throws Throwable {
                        isLoading.setValue(false);
                    }
                })
                .subscribe(new Consumer<MovieResponse>() {
                    @Override
                    public void accept(MovieResponse movieResponse) throws Throwable {
                        List<Movie> loadedMovies = movies.getValue();
                        if (loadedMovies != null) {
                            if (movieResponse.getMovies().size() == 0) {
                                reachedEnd = true;
                            }
                            loadedMovies.addAll(movieResponse.getMovies());
                            movies.setValue(loadedMovies);
                        } else {
                            movies.setValue(movieResponse.getMovies());
                        }
                        page++;
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d(LOG_TAG, throwable.toString());
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void clearMovies() {
        page = 1;
        reachedEnd = false;
        movies.setValue(new ArrayList<>());
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
