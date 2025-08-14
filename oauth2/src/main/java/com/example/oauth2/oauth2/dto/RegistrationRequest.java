package com.example.oauth2.oauth2.dto;

import java.io.Serializable;

public class RegistrationRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    public String username;
    public String password;
    public String name;
    public String email;
    public String phone;
    public String address;

    /**
     * Constructor por defecto, requerido para la deserialización.
     */
    public RegistrationRequest() {
    }

    /**
     * Constructor con todos los campos.
     */
    public RegistrationRequest(String username, String password, String name, String email, String phone, String address) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    // --- Getters y Setters ---

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}