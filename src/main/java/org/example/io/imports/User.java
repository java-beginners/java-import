package org.example.io.imports;


import java.time.LocalDateTime;

public class User {
    @MyLength(val=11)
    private String id;
    @MyLength(val=10)
    private String username;
    @MyLength(val=31)
    private String email;
    @MyLength(val=20)
    private String phone;
    @MyLength(val=5)
    private String status;
    @MyLength(val=10)
    @MyFormat (dateFormat = "yyyy-MM-dd HH:mm:ss.S")
    private LocalDateTime createdDate;
    public User() {
    }

    public User(String id, String username, String email, String phone, String status, LocalDateTime createdDate) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.status = status;
        this.createdDate = createdDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
