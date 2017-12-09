package com.tody.datamodel;

/**
 * Created by Olha Dovhal on 20/05/2017.
 */
public class DeptAdmin {
    private int id;
    private String name;
    private String password;
    private String userType = "DA";

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }
}
