package com.cx.kpi2.dao;

import com.cx.kpi2.pojo.Record;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecordDAO extends JpaRepository<Record , Integer> {
    List<Record> getByYearmonth(String yearmonth);
    List<Record> getByBussinessAndYearmonth(String bussiness , String yearmonth);
    Record getByBussinessAndDeptAndYearmonth(String bussiness , String dept , String yearmonth);
}
