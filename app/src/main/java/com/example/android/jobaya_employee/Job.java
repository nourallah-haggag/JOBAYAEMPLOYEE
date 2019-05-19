package com.example.android.jobaya_employee;

public class Job {
    String title;
    String description;
    String duration;
    String category;
    String age;
    String gender;
    String mail;
    String ID;
    String skills;
    String exp;
    String lang;
    String empMail;

    public Job(String title , String description , String category , String duration , String age , String ID ,String gender , String skills , String exp , String lang , String empMail)
    {

        this.title = title;
        this.description = description;
        this.category = category;
        this.duration = duration;
        this.age = age;
        this.gender = gender;
        this.ID = ID;
        this.skills = skills;
        this.exp = exp;
        this.lang = lang;
        this.empMail =  empMail;

    }

}
