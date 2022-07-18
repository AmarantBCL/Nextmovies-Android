package com.example.android.nextmovies.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android.nextmovies.R;
import com.example.android.nextmovies.network.ApiFactory;
import com.example.android.nextmovies.pojo.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private List<Movie> movies = new ArrayList<>();
    private OnItemClickListener itemClickListener;
    private OnReachEndListener reachEndListener;

    public interface OnItemClickListener {
        void onItemClick(Movie movie);
    }

    public interface OnReachEndListener {
        void onReachEnd();
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setOnReachEndListener(OnReachEndListener reachEndListener) {
        this.reachEndListener = reachEndListener;
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
        double rating = movie.getAverageVote();
        int backgroundId;
        if (rating > 7) {
            backgroundId = R.drawable.circle_green;
        } else if (rating > 5) {
            backgroundId = R.drawable.circle_orange;
        } else {
            backgroundId = R.drawable.circle_red;
        }
        holder.textViewRating.setText(String.valueOf(rating));
        holder.textViewRating.setBackground(ContextCompat.getDrawable(
                holder.itemView.getContext(), backgroundId));
        if (position >= movies.size() - 6 && reachEndListener != null) {
            reachEndListener.onReachEnd();
        }
        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(movie);
            }
        });
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
