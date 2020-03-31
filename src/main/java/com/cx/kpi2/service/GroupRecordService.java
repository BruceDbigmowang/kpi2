package com.cx.kpi2.service;

import com.cx.kpi2.dao.GroupRecordDAO;
import com.cx.kpi2.pojo.GroupRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class GroupRecordService {
    @Autowired
    GroupRecordDAO groupRecordDAO;

    public boolean completRegard(String account , String yearMonth){
        List<GroupRecord> groupRecordList = groupRecordDAO.findByAccountAndYearMonth(account , yearMonth);
        if(groupRecordList == null || groupRecordList.size() == 0){
            return false;
        }else{
            return true;
        }
    }

    public void save(GroupRecord groupRecord){
        groupRecordDAO.save(groupRecord);
    }

    public List<String> getAllRecordTime(){
        List<GroupRecord> groupRecordList = groupRecordDAO.findAll();
        List<String> recordTimeList = new ArrayList<>();
        if(groupRecordList!=null &&groupRecordList.size()!= 0){

            for (int i = 0 ; i < groupRecordList.size() ; i++){
                String recordTime = groupRecordList.get(i).getYearMonth();
                if(!recordTimeList.contains(recordTime)){
                    recordTimeList.add(recordTime.split("_")[0]);
                }
            }

        }else{
            Calendar date = Calendar.getInstance();
            String year = String.valueOf(date.get(Calendar.YEAR));
            recordTimeList.add(year);
        }
        return recordTimeList;
    }

    public List<String> getAllRecordYearMOnth(){
        List<GroupRecord> groupRecordList = groupRecordDAO.findAll();
        List<String> recordTimeList = new ArrayList<>();
        if(groupRecordList!=null &&groupRecordList.size()!= 0){

            for (int i = 0 ; i < groupRecordList.size() ; i++){
                String recordTime = groupRecordList.get(i).getYearMonth();
                if(!recordTimeList.contains(recordTime)){
                    recordTimeList.add(recordTime);
                }
            }

        }else{
            Calendar date = Calendar.getInstance();
            String year = String.valueOf(date.get(Calendar.YEAR));
            String month = String.valueOf(date.get(Calendar.MONTH));
            recordTimeList.add(year+"_"+month);
        }
        return recordTimeList;
    }
}
