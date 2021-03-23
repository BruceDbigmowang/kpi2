package com.cx.kpi2.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "KPI_Record2")
@JsonIgnoreProperties({"handler" , "hibernateLazyInitializer"})
public class KpiRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String bussiness;
    String dept;
    int score;
    String yearMonth;
    int kpiNo;
    String checkLevel;

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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }

    public int getKpiNo() {
        return kpiNo;
    }

    public void setKpiNo(int kpiNo) {
        this.kpiNo = kpiNo;
    }

    public String getCheckLevel() {
        return checkLevel;
    }

    public void setCheckLevel(String checkLevel) {
        this.checkLevel = checkLevel;
    }
}
