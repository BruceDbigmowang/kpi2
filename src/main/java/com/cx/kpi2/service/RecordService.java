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
    public void writeRecord(Record record){
        recordDAO.save(record);
    }

    public Record getScore(String bussiness , String dept , int kpiNo , String yearMonth){
        List<Record> recordList = recordDAO.getByBussinessAndDeptAndKpiNoAndYearmonth(bussiness,dept,kpiNo,yearMonth);
        if(recordList.size() == 0){
            Record record = new Record();
            record.setScore(0);
            return record;
        }else{
            Record record = recordList.get(0);
            return record;
        }
    }
}
