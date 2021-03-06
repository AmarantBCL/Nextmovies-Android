package com.example.android.nextmovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.android.nextmovies.adapters.MovieAdapter;
import com.example.android.nextmovies.architecture.FavoritesViewModel;
import com.example.android.nextmovies.pojo.Movie;

import java.util.List;

public class FavoritesActivity extends AppCompatActivity {
    private RecyclerView recyclerViewFavs;
    private MovieAdapter adapter;
    private FavoritesViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        initViews();
        viewModel = new ViewModelProvider(this).get(FavoritesViewModel.class);
        observeViewModel();
        setClickListeners();
    }

    private void setClickListeners() {
        adapter.setOnItemClickListener(new MovieAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Movie movie) {
                Intent intent = DetailsActivity.newIntent(FavoritesActivity.this, movie);
                startActivity(intent);
            }
        });
    }

    private void observeViewModel() {
        viewModel.getFavorites().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                adapter.setMovies(movies);
            }
        });
    }

    private void initViews() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerViewFavs = findViewById(R.id.recycler_view_favs);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerViewFavs.setLayoutManager(new GridLayoutManager(this, 4));
        } else {
            recyclerViewFavs.setLayoutManager(new GridLayoutManager(this, 2));
        }
        adapter = new MovieAdapter();
        recyclerViewFavs.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, FavoritesActivity.class);
    }
}