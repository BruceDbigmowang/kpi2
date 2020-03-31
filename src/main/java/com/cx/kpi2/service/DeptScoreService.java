package com.cx.kpi2.service;

import com.cx.kpi2.dao.*;
import com.cx.kpi2.pojo.Dept;
import com.cx.kpi2.pojo.DeptScore;
import com.cx.kpi2.pojo.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class DeptScoreService {
    @Autowired
    DeptScoreDAO deptScoreDAO;
    @Autowired
    DeptDAO deptDAO;
    @Autowired
    RecordDAO recordDAO;
    @Autowired
    HrkpiDAO hrkpiDAO;
    @Autowired
    OtherkpiDAO otherkpiDAO;

    public void saveOne(DeptScore deptScore){
        deptScoreDAO.save(deptScore);
    }

    public List<DeptScore> getAMonth(String bussiness , String dept , String yearMonth){
        return deptScoreDAO.getByBussinessAndDeptAndYearMonth(bussiness , dept , yearMonth);
    }

    /**
     * 获取某事业部各个部门评分的考核的分以及权重
     * @param bussiness
     * @param yearMonth
     * @return
     */
    public List<DeptScore> getAllDept(String bussiness , String yearMonth){
        List<Dept> deptList = deptDAO.findAll();
        List<DeptScore> deptScoreList = new ArrayList<>();
        for(int i = 0 ; i < deptList.size() ; i++){
            String dept = deptList.get(i).getDeptName();
            List<DeptScore> deptScores = deptScoreDAO.getByBussinessAndDeptAndYearMonth(bussiness , dept , yearMonth);
            if(deptScores.size() == 0){
                DeptScore deptScore = new DeptScore();
                deptScore.setWeight(new BigDecimal(0));
                deptScore.setScore(new BigDecimal(0));
                deptScoreList.add(deptScore);
            }else{
                deptScoreList.add(deptScores.get(0));
            }
        }
        return deptScoreList;
    }

    List<DeptScore> getAllDetail(String bussiness , String dept, String yearMonth){
        return deptScoreDAO.getByBussinessAndDeptAndYearMonth(bussiness,dept,yearMonth);
    }

    public boolean happendImport(String bussiness , String dept , String yearMonth ){
        if(dept.equals("行政")||dept.equals("质量")||dept.equals("审计")||dept.equals("人事")){
            List<Record> recordList = recordDAO.getByBussinessAndDeptAndYearmonthAndScore(bussiness , dept , yearMonth , 0);
            if(recordList == null || recordList.size() == 0){
                return false;
            }else{
                for(int i = 0 ; i < recordList.size() ; i++){
                    int kpiNo = recordList.get(i).getKpiNo();
                    if(dept.equals("人事")){
                        String level = hrkpiDAO.findById(kpiNo).get(0).getTestLevel();
                        if(level.equals("S")){
                            return true;
                        }
                    }else{
                        String level = otherkpiDAO.findById(kpiNo).get(0).getTestLevel();
                        if(level.equals("S")){
                            return true;
                        }
                    }
                }
                return false;
            }
        }else{
            return false;
        }
    }
}
