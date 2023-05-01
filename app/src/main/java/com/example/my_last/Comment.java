package com.example.my_last;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Comment {
    @SerializedName("comment_number")
    private int commentNumber;
    @SerializedName("post_number")
    private int postNumber;
    @SerializedName("before_comment")
    private Integer beforeComment; // Nullable
    @SerializedName("user_number")
    private int userNumber;
    @SerializedName("comment_body_path")
    private String commentBodyPath;

    @SerializedName("user")
    private User user;

    private String content;



    // Getters and setters
    public int getCommentNumber() {
        return commentNumber;
    }

    public void setCommentNumber(int commentNumber) {
        this.commentNumber = commentNumber;
    }

    public int getPostNumber() {
        return postNumber;
    }

    public void setPostNumber(int postNumber) {
        this.postNumber = postNumber;
    }

    public Integer getBeforeComment() {
        return beforeComment;
    }

    public void setBeforeComment(Integer beforeComment) {
        this.beforeComment = beforeComment;
    }

    public int getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(int userNumber) {
        this.userNumber = userNumber;
    }

    public String getCommentBodyPath() {
        return commentBodyPath;
    }

    public void setCommentBodyPath(String commentBodyPath) {
        this.commentBodyPath = commentBodyPath;
    }
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // content 필드에 대한 getter 메서드
    public String getContent() {
        return content;
    }

    // content 필드에 대한 setter 메서드
    public void setContent(String content) {
        this.content = content;
    }

    private ArrayList<Comment> replies;

    public ArrayList<Comment> getReplies() {
        return replies;
    }

    public void setReplies(ArrayList<Comment> replies) {
        this.replies = replies;
    }


    public void setBeforeComment(int beforeComment) {
        this.beforeComment = beforeComment;
    }

}