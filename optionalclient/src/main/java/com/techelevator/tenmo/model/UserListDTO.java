package com.techelevator.tenmo.model;

public class UserListDTO {

    private String username;
    private int userId;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "UserListDTO{" +
                "username='" + username + '\'' +
                ", userId=" + userId +
                '}';
    }
}
