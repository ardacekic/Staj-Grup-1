package com.example.said.projecttwo;

public class User {
    String userName;
    int countOfGps;

    public User(String userName, int countOfGps) {
        this.userName = userName;
        this.countOfGps = countOfGps;
    }

    public String getUserName() {
        return userName;
    }

    public int getCountOfGps() {
        return countOfGps;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setCountOfGps(int countOfGps) {
        this.countOfGps = countOfGps;
    }
}
