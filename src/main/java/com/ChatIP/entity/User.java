package com.ChatIP.entity;

import javax.persistence.*;

@Entity
@Table (name = "user", catalog = "myshema")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column (name = "id")
    private long Id;

    @Column (name = "name")
    private String name;

    @Column (name = "password")
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name + " " + password;
    }
}
