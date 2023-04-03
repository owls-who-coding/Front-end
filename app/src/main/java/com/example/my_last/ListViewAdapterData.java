package com.example.my_last;

public class ListViewAdapterData {
    private String tag;
    private String nickname;
    private String title;
    private String upload_date;
    private String count;
    private String comment;

    public void setTag(String tag){this.tag = tag;}
    public void setNickname(String nickname){this.nickname = nickname;}
    public void setTitle(String title){this.title = title;}
    public void setUpload_date(String upload_date){this.upload_date = upload_date;}
    public void setCount(String count){this.count = count;}
    public void setComment(String comment){this.comment = comment;}

    public String getTag(){return this.tag;}
    public String getNickname(){return this.nickname;}
    public String getTitle(){return this.title;}
    public String getUpload_date(){return this.upload_date;}
    public String getCount(){return this.count;}
    public String getComment(){return this.comment;}

}
