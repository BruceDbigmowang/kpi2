package com.cx.kpi2.service;

import com.cx.kpi2.dao.DeptDAO;
import com.cx.kpi2.dao.HrkpiDAO;
import com.cx.kpi2.dao.OtherkpiDAO;
import com.cx.kpi2.dao.RecordDAO;
import com.cx.kpi2.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DetailService {
    @Autowired
    DeptDAO deptDAO;
    @Autowired
    RecordDAO recordDAO;
    @Autowired
    OtherkpiDAO otherkpiDAO;
    @Autowired
    HrkpiDAO hrkpiDAO;

    public List<Detail> getAllDetail(String bussiness , String yearMonth){
        List<Detail> detailList = new ArrayList<>();
        List<Dept> deptList = deptDAO.findAll();
        for(int i = 0 ; i < deptList.size() ; i++){
            String dept = deptList.get(i).getDeptName();
            if(dept.equals("人事")){
                List<Record> recordList = recordDAO.getByBussinessAndDeptAndYearmonth(bussiness , dept , yearMonth);
                for(int j = 0 ; j < recordList.size() ; j++){
                        Record record = recordList.get(j);
                        List<Hrkpi> hrkpiList = hrkpiDAO.findById(record.getKpiNo());
                        if(hrkpiList.size() != 0){
                            String kpiDetail = hrkpiList.get(0).getTestDetail();
                            String testLevel = hrkpiList.get(0).getTestLevel();
                            Detail detail = new Detail();
                            detail.setDept("集团"+dept);
                            detail.setDetail(kpiDetail);
                            detail.setScore(record.getScore());
                            detail.setTestLevel(testLevel);
                            detailList.add(detail);

                        }

                }
            }else{
                List<Record> recordList = recordDAO.getByBussinessAndDeptAndYearmonth(bussiness , dept , yearMonth);
                for(int j = 0 ; j < recordList.size() ; j++){
                    Record record = recordList.get(j);
                    List<Otherkpi> otherkpiList = otherkpiDAO.findById(record.getKpiNo());
                    if(otherkpiList != null){
                        String kpiDetail = otherkpiList.get(0).getTestDetail();
                        String testLevel = otherkpiList.get(0).getTestLevel();
                        Detail detail = new Detail();
                        detail.setDept("集团"+dept);
                        detail.setDetail(kpiDetail);
                        detail.setScore(record.getScore());
                        detail.setTestLevel(testLevel);
                        detailList.add(detail);
                    }

                }

            }

        }
        return detailList;
    }
}
