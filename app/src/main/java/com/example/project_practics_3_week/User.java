package com.example.project_practics_3_week;

import java.io.Serializable;

public class User implements Serializable {
    private String id;
    private String name;
    private String FirstName;
    private String email;
    private String password;
    private String telephone_number;
    private String addres_home;
    private String cart_oplats;
    private String avatar_url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelephone_number() {
        return telephone_number;
    }

    public void setTelephone_number(String telephone_number) {
        this.telephone_number = telephone_number;
    }

    public String getAddres_home() {
        return addres_home;
    }

    public void setAddres_home(String addres_home) {
        this.addres_home = addres_home;
    }

    public String getCart_oplats() {
        return cart_oplats;
    }

    public void setCart_oplats(String cart_oplats) {
        this.cart_oplats = cart_oplats;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }
}
