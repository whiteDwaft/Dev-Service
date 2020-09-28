package com.whitedwaft.devservice.dao;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


public class MiniUserDao {
    private String email;
    private String password;
    private String role;

//    public MiniUserDao(String email, String password, String role) {
//        this.email = email;
//        this.password = password;
//        this.role = role;
//    }
//
//    public MiniUserDao(String email, String password) {
//        this.email = email;
//        this.password = password;
//    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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
}
