package com.cx.kpi2.service;

import com.cx.kpi2.dao.BussinessDAO;
import com.cx.kpi2.dao.CompletKpiDAO;
import com.cx.kpi2.pojo.Bussiness;
import com.cx.kpi2.pojo.CompleteKpi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CompletKpiService {
    @Autowired
    CompletKpiDAO completKpiDAO;
    @Autowired
    BussinessDAO bussinessDAO;

    public List<String> getRecordTime(){
        List<CompleteKpi> completeKpiList = completKpiDAO.findAll();
        System.out.println(completeKpiList);
        List<String> recordTime = new ArrayList<>();
        for(int i = 0 ; i < completeKpiList.size() ; i++){
            String year_month = completeKpiList.get(i).getYearMonth();
            if(!recordTime.contains(year_month)){
                recordTime.add(year_month);
            }
        }
        System.out.println(recordTime);
        return recordTime;
    }

    public List<String> getRecordTimeByBussiness(String bussiness){
        List<CompleteKpi> completeKpiList = completKpiDAO.findByBussiness(bussiness);
        List<String> recordTime = new ArrayList<>();
        for(int i = 0 ; i < completeKpiList.size() ; i++){
            String year_month = completeKpiList.get(i).getYearMonth();
            if(!recordTime.contains(year_month)){
                recordTime.add(year_month);
            }
        }
        return recordTime;
    }

    /**
     * 验证该月所有事业部该部门考核人已经完成考核打分
     * @param dept
     * @param yearMonth
     * @return
     */
    public boolean completAllBussiness(String dept , String yearMonth){
        List<CompleteKpi> completeKpiList = completKpiDAO.findByDeptAndYearMonth(dept , yearMonth);
        List<Bussiness> bussinessList = bussinessDAO.findAll();
        if(completeKpiList.size() == bussinessList.size()){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 判断 某事业部下的某部门是否完成考核
     * 完成考核  返回提示消息
     * 未完成考核  返回null；并进行考核打分
     * @param bussiness
     * @param dept
     * @param yearMonth
     * @return
     */
    public String completOneBussiness(String bussiness , String dept , String yearMonth){
        List<CompleteKpi> completeKpiList = completKpiDAO.findByBussinessAndDeptAndYearMonth(bussiness , dept , yearMonth);
        if(completeKpiList.size() == 0){
            return null;
        }else{
            return bussiness+"已完成考核";
        }
    }

    public boolean completKpi(String bussiness , String dept , String yearMonth){
        dept = dept.substring(2);
        List<CompleteKpi> completeKpiList = completKpiDAO.findByBussinessAndDeptAndYearMonth(bussiness , dept , yearMonth);
        if(completeKpiList.size() == 0){

            return false;
        }else{

            return true;
        }
    }

    public void writeRecord(String bussiness , String dept , String yearMonth){
        CompleteKpi completeKpi = new CompleteKpi();
        completeKpi.setBussiness(bussiness);
        completeKpi.setDept(dept);
        completeKpi.setYearMonth(yearMonth);
        completKpiDAO.save(completeKpi);
    }

    public boolean submitedRegard(String bussiness , String dept , String yearMonth){
        List<CompleteKpi> completeKpiList = completKpiDAO.findByBussinessAndDeptAndYearMonth(bussiness , dept , yearMonth);
        System.out.println(completeKpiList.size());
        if(completeKpiList.size() == 0){
            return false;
        }else{
            return true;
        }
    }
}
