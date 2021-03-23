package com.cx.kpi2.service;

import com.cx.kpi2.dao.HrkpiDAO;
import com.cx.kpi2.dao.OtherkpiDAO;
import com.cx.kpi2.dao.RecordDAO;
import com.cx.kpi2.pojo.Hrkpi;
import com.cx.kpi2.pojo.Otherkpi;
import com.cx.kpi2.pojo.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class RecordService {
    @Autowired
    RecordDAO recordDAO;
    @Autowired
    HrkpiDAO hrkpiDAO;
    @Autowired
    OtherkpiDAO otherkpiDAO;

    public List<Record> getAllRecord(String yearmonth){
        return recordDAO.getByYearmonth(yearmonth);
    }

    public List<Record> getRecordByBussiness(String bussiness , String yearmonth){
        return recordDAO.getByBussinessAndYearmonth(bussiness,yearmonth);
    }

    public List<Record> getRecordByBussinessAndDept(String bussiness , String dept , String yearmonth){
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

    public boolean occurBig(String bussiness , String dept , String yearMonth){
        List<Record> recordList = recordDAO.getByBussinessAndDeptAndYearmonthAndScore(bussiness , dept , yearMonth , 0);
        int result = 0;
        for(int i = 0 ; i < recordList.size() ; i++){
            int kpiNo = recordList.get(i).getKpiNo();
            if(dept.equals("人事")){
                Hrkpi  hrkpi = hrkpiDAO.getOne(kpiNo);
                if(hrkpi.getTestLevel().equals("S")){
                    result = result+1;
                }
            }else{
                List<Otherkpi> otherkpiList = otherkpiDAO.findById(kpiNo);
                if(otherkpiList != null && otherkpiList.size() != 0){
                    if(otherkpiList.get(0).getTestLevel().equals("S")){
                        result = result+1;
                    }
                }
            }
        }
        if(result == 0){
            return false;
        }else{
            return true;
        }
    }
}
