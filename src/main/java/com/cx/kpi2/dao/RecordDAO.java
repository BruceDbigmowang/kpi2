package com.cx.kpi2.dao;

import com.cx.kpi2.pojo.Record;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecordDAO extends JpaRepository<Record , Integer> {
    List<Record> getByYearmonth(String yearmonth);
    List<Record> getByBussinessAndYearmonth(String bussiness , String yearmonth);
    List<Record> getByBussinessAndDeptAndYearmonth(String bussiness , String dept , String yearmonth);
    List<Record> getByBussinessAndDeptAndKpiNoAndYearmonth(String bussiness , String dept , int kpiNo , String yearMonth);
    List<Record> getByBussinessAndScoreAndYearmonth(String bussiness , int score , String yearMonth);
    List<Record> getByBussinessAndDeptAndYearmonthAndScore(String bussiness , String dept , String yearMonth , int score);
}
