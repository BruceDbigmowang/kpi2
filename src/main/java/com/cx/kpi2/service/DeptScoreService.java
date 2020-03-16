package com.cx.kpi2.service;

import com.cx.kpi2.dao.DeptDAO;
import com.cx.kpi2.dao.DeptScoreDAO;
import com.cx.kpi2.pojo.Dept;
import com.cx.kpi2.pojo.DeptScore;
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
}
