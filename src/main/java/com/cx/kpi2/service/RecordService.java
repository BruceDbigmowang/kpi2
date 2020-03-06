package com.cx.kpi2.service;

import com.cx.kpi2.dao.RecordDAO;
import com.cx.kpi2.pojo.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecordService {
    @Autowired
    RecordDAO recordDAO;
    public List<Record> getAllRecord(String yearmonth){
        return recordDAO.getByYearmonth(yearmonth);
    }

    public List<Record> getRecordByBussiness(String bussiness , String yearmonth){
        return recordDAO.getByBussinessAndYearmonth(bussiness,yearmonth);
    }

    public Record getRecordByBussinessAndDept(String bussiness , String dept , String yearmonth){
        return recordDAO.getByBussinessAndDeptAndYearmonth(bussiness, dept , yearmonth);
    }
}
