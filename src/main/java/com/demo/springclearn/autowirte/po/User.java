package com.demo.springclearn.autowirte.po;

import org.springframework.beans.factory.annotation.Autowired;

public class User {

    private String name;

    @Autowired
    private Address address;

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public Address getAddress() {
        return address;
    }

    public User setAddress(Address address) {
        this.address = address;
        return this;
    }
}