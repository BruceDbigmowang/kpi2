package com.cx.kpi2.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Otherkpi")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class Otherkpi {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Column(name = "test_type")
    int testType;
    @Column(name = "test_content")
    int testContent;
    @Column(name = "test_detail")
    String testDetail;
    @Column(name = "score_max")
    int scoreMax;
    @Column(name = "test_level")
    String testLevel;
    @Column(name = "weight")
    BigDecimal weight;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTestType() {
        return testType;
    }

    public void setTestType(int testType) {
        this.testType = testType;
    }

    public int getTestContent() {
        return testContent;
    }

    public void setTestContent(int testContent) {
        this.testContent = testContent;
    }

    public String getTestDetail() {
        return testDetail;
    }

    public void setTestDetail(String testDetail) {
        this.testDetail = testDetail;
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

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }
}
