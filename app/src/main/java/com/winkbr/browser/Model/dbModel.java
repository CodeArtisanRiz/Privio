package com.winkbr.browser.Model;

public class dbModel {
    int id;
    String name,age;

    public dbModel(int id, String name, String age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }
    public dbModel(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}

