package com.student.management;

public class Student {

    private int    id;
    private String name;
    private String email;
    private String phone;

    public Student(String name, String email, String phone) {
        this.name  = name;
        this.email = email;
        this.phone = phone;
    }

    public Student(int id, String name, String email, String phone) {
        this.id    = id;
        this.name  = name;
        this.email = email;
        this.phone = phone;
    }

    public int    getId()    { return id; }
    public String getName()  { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }

    public void setId(int id)       { this.id    = id; }
    public void setName(String n)   { this.name  = n; }
    public void setEmail(String e)  { this.email = e; }
    public void setPhone(String p)  { this.phone = p; }
}
