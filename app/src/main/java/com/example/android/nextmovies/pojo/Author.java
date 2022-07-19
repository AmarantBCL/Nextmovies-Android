package com.example.android.nextmovies.pojo;

import com.google.gson.annotations.SerializedName;

public class Author {
    @SerializedName("username")
    private String username;
    @SerializedName("rating")
    private Integer rating;

    public Author(String username, int rating) {
        this.username = username;
        this.rating = rating;
    }

    public String getUsername() {
        return username;
    }

    public Integer getRating() {
        return rating;
    }

    @Override
    public String toString() {
        return "Author{" +
                "username='" + username + '\'' +
                ", rating=" + rating +
                '}';
    }
}
