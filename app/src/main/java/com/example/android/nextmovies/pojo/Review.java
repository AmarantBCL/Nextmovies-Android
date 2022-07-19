package com.example.android.nextmovies.pojo;

import com.google.gson.annotations.SerializedName;

public class Review {
    @SerializedName("author_details")
    private Author author;
    @SerializedName("content")
    private String content;

    public Review(Author author, String content) {
        this.author = author;
        this.content = content;
    }

    public Author getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "Review{" +
                "author=" + author +
                ", content='" + content + '\'' +
                '}';
    }
}
