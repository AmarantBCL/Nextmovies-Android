package com.example.android.nextmovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.nextmovies.adapters.ReviewAdapter;
import com.example.android.nextmovies.adapters.TrailerAdapter;
import com.example.android.nextmovies.architecture.DetailsViewModel;
import com.example.android.nextmovies.network.ApiFactory;
import com.example.android.nextmovies.pojo.Movie;
import com.example.android.nextmovies.pojo.Review;
import com.example.android.nextmovies.pojo.Trailer;
import com.example.android.nextmovies.utils.DateUtils;

import java.util.List;

public class DetailsActivity extends AppCompatActivity {
    private static final String EXTRA_MOVIE = "movie";

    private RecyclerView recyclerViewTrailers, recyclerViewReviews;
    private LinearLayout linearLayout;
    private ImageView imageViewPoster;
    private TextView textViewTitle, textViewYear, textViewDesc;

    private DetailsViewModel viewModel;
    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        initViews();
        trailerAdapter = new TrailerAdapter();
        recyclerViewTrailers.setAdapter(trailerAdapter);
        reviewAdapter = new ReviewAdapter();
        recyclerViewReviews.setAdapter(reviewAdapter);
        viewModel = new ViewModelProvider(this).get(DetailsViewModel.class);
        Movie movie = (Movie) getIntent().getSerializableExtra(EXTRA_MOVIE);
        Glide.with(this)
                .load(ApiFactory.POSTER_URL + movie.getPosterPath())
                .placeholder(R.drawable.ic_poster_placeholder)
                .into(imageViewPoster);
        textViewTitle.setText(movie.getTitle());
        textViewYear.setText(DateUtils.convertToYear(movie.getReleaseDate()));
        textViewDesc.setText(movie.getOverview());
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

    public static Intent newIntent(Context context, Movie movie) {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra(EXTRA_MOVIE, movie);
        return intent;
    }

    private void initViews() {
        recyclerViewTrailers = findViewById(R.id.recycler_view_trailers);
        recyclerViewReviews = findViewById(R.id.recycler_view_reviews);
        linearLayout = findViewById(R.id.linear_layout);
        imageViewPoster = findViewById(R.id.img_big_poster);
        textViewTitle = findViewById(R.id.tv_title);
        textViewYear = findViewById(R.id.tv_year);
        textViewDesc = findViewById(R.id.tv_description);
    }
}