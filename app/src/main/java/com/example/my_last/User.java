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


    // userNumber 필드에 대한 getter 메서드
    public int getUserNumber() {
        return userNumber;
    }

    // userNumber 필드에 대한 setter 메서드
    public void setUserNumber(int userNumber) {
        this.userNumber = userNumber;
    }

}