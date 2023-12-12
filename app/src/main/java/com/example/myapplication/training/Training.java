package com.example.myapplication.training;

public class Training {
    private long id;
    private String name;
    private int time;
    private String desc;
    private String image;
    private long author;
    public Training(long id, String name, int time, String desc, String image, long author){
        this.id = id;
        this.name = name;
        this.time = time;
        this.desc = desc;
        this.image = image;
        this.author = author;
    }
    public long getId(){
        return id;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
    public void setTime(int time){
        this.time = time;
    }
    public int getTime(){
        return time;
    }
    public void setDesc(String desc){
        this.desc = desc;
    }
    public String getDesc(){
        return desc;
    }
    public void setImage(String image){
        this.image = image;
    }
    public String getImage(){
        return image;
    }
    public void setAuthor(long author){
        this.author = author;
    }
    public long getAuthor(){
        return author;
    }
}