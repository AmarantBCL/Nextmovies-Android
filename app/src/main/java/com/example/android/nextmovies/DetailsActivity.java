package com.example.android.nextmovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.RoomDatabase;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.nextmovies.adapters.ActorAdapter;
import com.example.android.nextmovies.adapters.ReviewAdapter;
import com.example.android.nextmovies.adapters.TrailerAdapter;
import com.example.android.nextmovies.architecture.DetailsViewModel;
import com.example.android.nextmovies.database.MovieDatabase;
import com.example.android.nextmovies.network.ApiFactory;
import com.example.android.nextmovies.pojo.Actor;
import com.example.android.nextmovies.pojo.Movie;
import com.example.android.nextmovies.pojo.Review;
import com.example.android.nextmovies.pojo.Trailer;
import com.example.android.nextmovies.utils.DateUtils;

import java.util.List;

import io.reactivex.rxjava3.schedulers.Schedulers;

public class DetailsActivity extends AppCompatActivity {
    private static final String EXTRA_MOVIE = "movie";

    private Movie movie;

    private RecyclerView recyclerViewCast, recyclerViewTrailers, recyclerViewReviews;
    private LinearLayout linearLayout;
    private ImageView imageViewPoster, imageViewStar;
    private TextView textViewTitle, textViewYear, textViewDesc;

    private DetailsViewModel viewModel;
    private ActorAdapter castAdapter;
    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        initViews();
        viewModel = new ViewModelProvider(this).get(DetailsViewModel.class);
        movie = (Movie) getIntent().getSerializableExtra(EXTRA_MOVIE);
        setMovieDetails();
        observeViewModel();
        setClickListeners();
    }

    private void setMovieDetails() {
        Glide.with(this)
                .load(ApiFactory.POSTER_URL + movie.getPosterPath())
                .placeholder(R.drawable.ic_poster_placeholder)
                .into(imageViewPoster);
        textViewTitle.setText(movie.getTitle());
        textViewYear.setText(DateUtils.convertToYear(movie.getReleaseDate()));
        textViewDesc.setText(movie.getOverview());
    }

    private void setClickListeners() {
        trailerAdapter.setOnTrailerClickListener(new TrailerAdapter.OnTrailerClickListener() {
            @Override
            public void onTrailerClick(Trailer trailer) {
                String url = ApiFactory.YOUTUBE_URL + trailer.getUrl();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
    }

    private void observeViewModel() {
        viewModel.loadCast(movie.getId());
        viewModel.getActors().observe(this, new Observer<List<Actor>>() {
            @Override
            public void onChanged(List<Actor> actors) {
                castAdapter.setActors(actors);
            }
        });
        viewModel.loadTrailers(movie.getId());
        viewModel.getTrailers().observe(this, new Observer<List<Trailer>>() {
            @Override
            public void onChanged(List<Trailer> trailers) {
                trailerAdapter.setTrailers(trailers);
            }
        });
        viewModel.loadReviews(movie.getId());
        viewModel.getReviews().observe(this, new Observer<List<Review>>() {
            @Override
            public void onChanged(List<Review> reviews) {
                reviewAdapter.setReviews(reviews);
            }
        });
        Drawable starOn = ContextCompat.getDrawable(this, android.R.drawable.btn_star_big_on);
        Drawable starOff = ContextCompat.getDrawable(this, android.R.drawable.btn_star_big_off);
        viewModel.getFavoriteMovie(movie.getId()).observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(Movie favoriteMovie) {
                if (favoriteMovie == null) {
                    imageViewStar.setImageDrawable(starOff);
                    imageViewStar.setOnClickListener(v -> viewModel.addFavoriteMovie(movie));
                } else {
                    imageViewStar.setImageDrawable(starOn);
                    imageViewStar.setOnClickListener(v -> viewModel.deleteFavoriteMovie(movie.getId()));
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static Intent newIntent(Context context, Movie movie) {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra(EXTRA_MOVIE, movie);
        return intent;
    }

    private void initViews() {
        recyclerViewCast = findViewById(R.id.recycler_view_cast);
        recyclerViewCast.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewTrailers = findViewById(R.id.recycler_view_trailers);
        recyclerViewReviews = findViewById(R.id.recycler_view_reviews);
        castAdapter = new ActorAdapter();
        recyclerViewCast.setAdapter(castAdapter);
        trailerAdapter = new TrailerAdapter();
        recyclerViewTrailers.setAdapter(trailerAdapter);
        reviewAdapter = new ReviewAdapter();
        recyclerViewReviews.setAdapter(reviewAdapter);
        linearLayout = findViewById(R.id.linear_layout);
        imageViewPoster = findViewById(R.id.img_big_poster);
        textViewTitle = findViewById(R.id.tv_title);
        textViewYear = findViewById(R.id.tv_year);
        textViewDesc = findViewById(R.id.tv_description);
        imageViewStar = findViewById(R.id.img_star);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}