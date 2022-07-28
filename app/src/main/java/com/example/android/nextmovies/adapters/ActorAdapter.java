package com.example.android.nextmovies.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android.nextmovies.R;
import com.example.android.nextmovies.network.ApiFactory;
import com.example.android.nextmovies.pojo.Actor;

import java.util.ArrayList;
import java.util.List;

public class ActorAdapter extends RecyclerView.Adapter<ActorAdapter.ActorViewHolder> {
    private List<Actor> actors = new ArrayList<>();

    public void setActors(List<Actor> actors) {
        for (Actor actor : actors) {
            if (actor.getDepartment().equals("Acting")) {
                this.actors = actors;
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ActorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.actor_list_item, parent, false);
        return new ActorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActorViewHolder holder, int position) {
        Actor actor = actors.get(position);
        int placeholder = actor.getGender() == 0 || actor.getGender() == 2
                ? R.drawable.ic_placeholder_male : R.drawable.ic_placeholder_female;
        Glide.with(holder.itemView)
                .load(ApiFactory.POSTER_URL + actor.getPhotoUrl())
                .placeholder(placeholder)
                .into(holder.imageViewActor);
        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(holder.itemView.getContext(), actor.getName(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return actors.size();
    }

    class ActorViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageViewActor;

        public ActorViewHolder(@NonNull View view) {
            super(view);
            imageViewActor = view.findViewById(R.id.img_actor);
        }
    }
}
