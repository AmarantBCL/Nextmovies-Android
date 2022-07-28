package com.example.android.nextmovies.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CastResponse {
    @SerializedName("cast")
    private List<Actor> actors;

    public CastResponse(List<Actor> actors) {
        this.actors = actors;
    }

    public List<Actor> getActors() {
        return actors;
    }

    @Override
    public String toString() {
        return "CastResponse{" +
                "actors=" + actors +
                '}';
    }
}
