package com.cx.kpi2.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "KPI_CheckDetail")
@JsonIgnoreProperties({"handler" , "hibernateLazyInitializer"})
public class CheckDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String dept;
    String title;
    String mainContent;
    String details;
    int maxScore;
    String checkLevel;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMainContent() {
        return mainContent;
    }

    public void setMainContent(String mainContent) {
        this.mainContent = mainContent;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    public String getCheckLevel() {
        return checkLevel;
    }

    public void setCheckLevel(String checkLevel) {
        this.checkLevel = checkLevel;
    }
}
