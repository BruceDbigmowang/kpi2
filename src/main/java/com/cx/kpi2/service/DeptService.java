package com.cx.kpi2.service;

import com.cx.kpi2.dao.DeptDAO;
import com.cx.kpi2.pojo.Dept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeptService {
    @Autowired
    DeptDAO deptDAO;

    public List<String> getAllDept(){
        List<Dept> deptList = deptDAO.findAll();
        List<String> depts = new ArrayList<>();
        for(int i = 0 ; i < deptList.size() ; i++){
            depts.add(deptList.get(i).getDeptName());
        }
        return depts;
    }
}
