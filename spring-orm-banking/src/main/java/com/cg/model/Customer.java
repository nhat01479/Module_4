package com.cg.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "customers")
public class Customer {
    @Id //set primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) //set id auto_increment
    private Long id;
    @Column(name = "full_name", nullable = false)
    private String fullName;
    @Column(nullable = false, unique = true)
    private String email;
    private String phone;
    private String address;
    private Double balance = 0.0;
    private Date createAt = new Date();
    private int delete = 0;

    public Customer() {
    }

    public Customer(Long id, String fullName, String email, String phone, String address, Double balance) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public int getDelete() {
        return delete;
    }

    public void setDelete(int delete) {
        this.delete = delete;
    }
}
