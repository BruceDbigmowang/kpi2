package com.cx.kpi2.service;

import com.cx.kpi2.dao.DeptTotalScoreDAO;
import com.cx.kpi2.pojo.DeptScore;
import com.cx.kpi2.pojo.DeptTotalScore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeptTotalService {
    @Autowired
    DeptTotalScoreDAO deptTotalScoreDAO;

    public void saveOne(DeptTotalScore deptTotalScore){
        deptTotalScoreDAO.save(deptTotalScore);
    }


}
