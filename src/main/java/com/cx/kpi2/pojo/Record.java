package com.cx.kpi2.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "Record")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class Record {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Column(name = "bussiness")
    String bussiness;
    @Column(name = "dept")
    String dept;
    @Column(name = "score")
    int score;
    @Column(name = "year_month")
    String yearmonth;
    @Column(name = "KpiNo")
    String kpiNo;

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

    public String getYearmonth() {
        return yearmonth;
    }

    public void setYearmonth(String yearmonth) {
        this.yearmonth = yearmonth;
    }

    public String getKpiNo() {
        return kpiNo;
    }

    public void setKpiNo(String kpiNo) {
        this.kpiNo = kpiNo;
    }
}
