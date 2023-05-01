package com.example.my_last;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("user_number")
    private int userNumber;
    @SerializedName("id")
    private String id;
    @SerializedName("password")
    private String password;
    @SerializedName("name")
    private String name;
    @SerializedName("age")
    private int age;
    @SerializedName("dog_name")
    private String dogName;

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getid() {
        return userNumber;
    }

    public void setid(int id) {
        this.userNumber = id;
    }
}