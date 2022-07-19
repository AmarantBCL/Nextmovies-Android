package com.example.android.nextmovies.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.nextmovies.R;
import com.example.android.nextmovies.pojo.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private List<Review> reviews = new ArrayList<>();

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_list_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviews.get(position);
        holder.textViewAuthor.setText(review.getAuthor().getUsername());
        holder.textViewContent.setText(review.getContent());
        Integer rating = review.getAuthor().getRating();
        int color = android.R.color.holo_orange_dark;
        if (rating != null) {
            if (rating > 7) {
                color = android.R.color.holo_green_light;
            } else if (rating < 5) {
                color = android.R.color.holo_red_light;
            }
        }
        holder.linearLayout.setBackgroundColor(
                ContextCompat.getColor(holder.itemView.getContext(),
                color)
        );
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout linearLayout;
        private final TextView textViewAuthor, textViewContent;

        public ReviewViewHolder(@NonNull View view) {
            super(view);
            linearLayout = view.findViewById(R.id.linear_layout);
            textViewAuthor = view.findViewById(R.id.tv_author);
            textViewContent = view.findViewById(R.id.tv_content);
        }
    }
}
