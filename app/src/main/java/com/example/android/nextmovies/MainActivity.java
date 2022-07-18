package com.example.android.nextmovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.android.nextmovies.architecture.MainViewModel;
import com.example.android.nextmovies.network.ApiFactory;
import com.example.android.nextmovies.pojo.Movie;
import com.example.android.nextmovies.pojo.MovieResponse;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewModel.getMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                Toast.makeText(MainActivity.this, movies.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        viewModel.loadMovies();
    }
}