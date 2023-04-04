package com.example.my_last;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Post {


    @SerializedName("post_number")
    private int postNumber;
    @SerializedName("user_number")
    private int userNumber;
    @SerializedName("post_body_path")
    private String postBodyPath;
    @SerializedName("image_path")
    private String imagePath;
    @SerializedName("disease_number")
    private int diseaseNumber;
    @SerializedName("comment_count")
    private int commentCount;
    @SerializedName("title")
    private String title;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;

    public int getPostNumber() {
        return postNumber;
    }

    public void setPostNumber(int postNumber) {
        this.postNumber = postNumber;
    }

    public int getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(int userNumber) {
        this.userNumber = userNumber;
    }

    public String getPostBodyPath() {
        return postBodyPath;
    }

    public void setPostBodyPath(String postBodyPath) {
        this.postBodyPath = postBodyPath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getDiseaseNumber() {
        return diseaseNumber;
    }

    public void setDiseaseNumber(int diseaseNumber) {
        this.diseaseNumber = diseaseNumber;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }


    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }


}