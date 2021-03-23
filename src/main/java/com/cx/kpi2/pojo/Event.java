package com.cx.kpi2.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "KPI_Event")
@JsonIgnoreProperties({"handler" , "hibernateLazyInitializer"})
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String bussiness;
    String dept;
    String checkLevel;
    String yearMonth;
    int deduct;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBussiness() {
        return bussiness;
    }

    public void setBussiness(String bussiness) {
        this.bussiness = bussiness;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getCheckLevel() {
        return checkLevel;
    }

    public void setCheckLevel(String checkLevel) {
        this.checkLevel = checkLevel;
    }

    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }

    public int getDeduct() {
        return deduct;
    }

    public void setDeduct(int deduct) {
        this.deduct = deduct;
    }
}
