package com.example.hnust_app;

public class Course {
    public int id; // 数据库里的唯一ID
    public String courseName;
    public String time;
    public String location;
    public String className;

    //用于从数据库读取带ID的数据
    public Course(int id, String courseName, String time, String location, String className) {
        this.id = id;
        this.courseName = courseName;
        this.time = time;
        this.location = location;
        this.className = className;
    }

    //用于添加新课程（没有ID）
    public Course(String courseName, String time, String location, String className) {
        this.courseName = courseName;
        this.time = time;
        this.location = location;
        this.className = className;
    }
}