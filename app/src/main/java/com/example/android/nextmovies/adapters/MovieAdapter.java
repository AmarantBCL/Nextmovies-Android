package com.example.android.nextmovies.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android.nextmovies.R;
import com.example.android.nextmovies.network.ApiFactory;
import com.example.android.nextmovies.pojo.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private List<Movie> movies = new ArrayList<>();

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        Glide.with(holder.itemView)
                .load(ApiFactory.POSTER_URL + movie.getPosterPath())
                .into(holder.imageViewPoster);
        holder.textViewRating.setText(String.valueOf(movie.getAverageVote()));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageViewPoster;
        private final TextView textViewRating;

        public MovieViewHolder(@NonNull View view) {
            super(view);
            imageViewPoster = view.findViewById(R.id.img_poster);
            textViewRating = view.findViewById(R.id.tv_rating);
        }
    }
}
