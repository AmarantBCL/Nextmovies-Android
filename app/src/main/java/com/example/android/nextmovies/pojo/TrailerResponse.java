package com.example.android.nextmovies.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrailerResponse {
    @SerializedName("results")
    private List<Trailer> trailers;

    public TrailerResponse(List<Trailer> trailers) {
        this.trailers = trailers;
    }

    public List<Trailer> getTrailers() {
        return trailers;
    }

    @Override
    public String toString() {
        return "TrailerResponse{" +
                "trailers=" + trailers +
                '}';
    }
}
