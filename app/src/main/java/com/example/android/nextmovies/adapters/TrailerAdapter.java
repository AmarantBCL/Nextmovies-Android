package com.example.android.nextmovies.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.nextmovies.R;
import com.example.android.nextmovies.pojo.Trailer;

import java.util.ArrayList;
import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {
    private List<Trailer> trailers = new ArrayList<>();
    private OnTrailerClickListener listener;

    public interface OnTrailerClickListener {
        void onTrailerClick(Trailer trailer);
    }

    public void setTrailers(List<Trailer> trailers) {
        this.trailers = trailers;
        notifyDataSetChanged();
    }

    public void setOnTrailerClickListener(OnTrailerClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_list_item, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        Trailer trailer = trailers.get(position);
        holder.textViewName.setText(trailer.getName());
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTrailerClick(trailer);
            }
        });
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    static class TrailerViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewName;

        public TrailerViewHolder(@NonNull View view) {
            super(view);
            textViewName = view.findViewById(R.id.tv_trailer_name);
        }
    }
}
