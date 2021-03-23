package com.cx.kpi2.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "KPI_Bussiness")
@JsonIgnoreProperties({"handler" , "hibernateLazyInitializer"})
public class BussinessDivision {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String bussinessName;

    String ftype;
    String gtype;
    String htype;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBussinessName() {
        return bussinessName;
    }

    public void setBussinessName(String bussinessName) {
        this.bussinessName = bussinessName;
    }

    public String getFtype() {
        return ftype;
    }

    public void setFtype(String ftype) {
        this.ftype = ftype;
    }

    public String getGtype() {
        return gtype;
    }

    public void setGtype(String gtype) {
        this.gtype = gtype;
    }

    public String getHtype() {
        return htype;
    }

    public void setHtype(String htype) {
        this.htype = htype;
    }
}
