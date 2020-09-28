package com.whitedwaft.devservice.dao;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Set;

@Document(collection = "Auth_Users")
public class UserDao {
    public UserDao(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserDao() {

    }

    @Id
    private String id;
    private String email;
    private String password;

    @DBRef
    private Set<RoleDao> role;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Set<RoleDao> getRole() {
        return role;
    }

    public void setRole(Set<RoleDao> role) {
        this.role = role;
    }
}
