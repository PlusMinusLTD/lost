package com.PlusMinusLLC.lost.Meet;

import java.io.Serializable;

public class User implements Serializable {
   public String fullName;
    public String email;
    public String token;
    public String status;
    public String photo;
    public String UID;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public User(String fullName, String email, String token, String status, String photo, String UID) {
        this.fullName = fullName;
        this.email = email;
        this.token = token;
        this.status = status;
        this.photo = photo;
        this.UID = UID;
    }
}

