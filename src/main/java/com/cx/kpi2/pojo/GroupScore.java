package com.cx.kpi2.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "GroupScore")
@JsonIgnoreProperties({"handler" , "hibernateLazyInitializer"})
public class GroupScore {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "dept")
    String dept;
    @Column(name = "kpiId")
    int kpiNo;
    @Column(name = "scorer")
    String scorer;
    @Column(name = "score")
    BigDecimal score;
    @Column(name = "yearMonth")
    String yearMonth;

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

    public int getKpiNo() {
        return kpiNo;
    }

    public void setKpiNo(int kpiNo) {
        this.kpiNo = kpiNo;
    }

    public String getScorer() {
        return scorer;
    }

    public void setScorer(String scorer) {
        this.scorer = scorer;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }
}
