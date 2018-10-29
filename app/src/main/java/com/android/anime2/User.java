package com.android.anime2;

public class User {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private int id;
    private String username;
    private String email;

    private String avatar;
    public User(int id, String username, String email) {
        this.id = id;
        this.email = email;
        this.username = username;
    }
}
