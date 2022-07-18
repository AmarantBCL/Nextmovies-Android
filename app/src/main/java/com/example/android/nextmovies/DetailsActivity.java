package com.example.android.nextmovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.nextmovies.architecture.DetailsViewModel;
import com.example.android.nextmovies.network.ApiFactory;
import com.example.android.nextmovies.pojo.Movie;
import com.example.android.nextmovies.pojo.Trailer;
import com.example.android.nextmovies.pojo.TrailerResponse;
import com.example.android.nextmovies.utils.DateUtils;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DetailsActivity extends AppCompatActivity {
    private static final String EXTRA_MOVIE = "movie";

    private ImageView imageViewPoster;
    private TextView textViewTitle;
    private TextView textViewYear;
    private TextView textViewDesc;

    private DetailsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        initViews();
        viewModel = new ViewModelProvider(this).get(DetailsViewModel.class);
        Movie movie = (Movie) getIntent().getSerializableExtra(EXTRA_MOVIE);
        Glide.with(this)
                .load(ApiFactory.POSTER_URL + movie.getPosterPath())
                .into(imageViewPoster);
        textViewTitle.setText(movie.getTitle());
        textViewYear.setText(DateUtils.convertToYear(movie.getReleaseDate()));
        textViewDesc.setText(movie.getOverview());
        viewModel.loadTrailers(0);
        viewModel.getTrailers().observe(this, new Observer<List<Trailer>>() {
            @Override
            public void onChanged(List<Trailer> trailers) {

            }
        });
    }

    public static Intent newIntent(Context context, Movie movie) {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra(EXTRA_MOVIE, movie);
        return intent;
    }

    private void initViews() {
        imageViewPoster = findViewById(R.id.img_big_poster);
        textViewTitle = findViewById(R.id.tv_title);
        textViewYear = findViewById(R.id.tv_year);
        textViewDesc = findViewById(R.id.tv_description);
    }
}