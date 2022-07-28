package com.example.android.nextmovies.pojo;

import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

public class Actor {
    @PrimaryKey
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("character")
    private String character;
    @SerializedName("profile_path")
    private String photoUrl;
    @SerializedName("gender")
    private int gender;
    @SerializedName("known_for_department")
    private String department;

    public Actor(int id, String name, String character, String photoUrl, int gender, String department) {
        this.id = id;
        this.name = name;
        this.character = character;
        this.photoUrl = photoUrl;
        this.gender = gender;
        this.department = department;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCharacter() {
        return character;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public int getGender() {
        return gender;
    }

    public String getDepartment() {
        return department;
    }

    @Override
    public String toString() {
        return "Actor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", character='" + character + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", gender=" + gender +
                ", department=" + department +
                '}';
    }
}
