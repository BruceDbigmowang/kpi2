package com.cx.kpi2.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "HR_KPI2")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class Hrkpi {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Column(name = "bussiness")
    String bussiness;
    @Column(name = "test_content")
    String testContent;
    @Column(name = "test_detail")
    String testDetail;
    @Column(name = "weight")
    BigDecimal weight;
    @Column(name = "score_max")
    int scoreMax;
    @Column(name = "test_level")
    String testLevel;

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

    public String getTestContent() {
        return testContent;
    }

    public void setTestContent(String testContent) {
        this.testContent = testContent;
    }

    public String getTestDetail() {
        return testDetail;
    }

    public void setTestDetail(String testDetail) {
        this.testDetail = testDetail;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public int getScoreMax() {
        return scoreMax;
    }

    public void setScoreMax(int scoreMax) {
        this.scoreMax = scoreMax;
    }

    public String getTestLevel() {
        return testLevel;
    }

    public void setTestLevel(String testLevel) {
        this.testLevel = testLevel;
    }
}
