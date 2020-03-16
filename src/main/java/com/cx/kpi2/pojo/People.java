package com.cx.kpi2.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "Account_VIEW")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class People {
    @Id
    @Column(name = "account")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    String account;

    @Column(name = "password")
    String password;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
