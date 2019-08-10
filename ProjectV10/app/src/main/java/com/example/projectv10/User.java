package com.example.projectv10;

public class User {
    String username;
    int countOfGps;

    public User(String username, int countOfGps) {
        this.username = username;
        this.countOfGps = countOfGps;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getCountOfGps() {
        return countOfGps;
    }

    public void setCountOfGps(int countOfGps) {
        this.countOfGps = countOfGps;
    }
}