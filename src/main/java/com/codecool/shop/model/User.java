package com.codecool.shop.model;

public class User {
    private int id;
    private String login;
    private String email;


    public User(){

    }

    public User(String login, String email) {
        this.login = login;
        this.email = email;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
