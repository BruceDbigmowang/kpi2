package com.cx.kpi2.service;

import com.cx.kpi2.dao.GroupKPIDAO;
import com.cx.kpi2.pojo.GroupKPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupKPIService {
    @Autowired
    GroupKPIDAO groupKPIDAO;

    public List<GroupKPI> findByLevel(String level){
        return  groupKPIDAO.findByLevel(level);
    }

    public List<GroupKPI> findByLevelAndDept(String level , String dept){
        return groupKPIDAO.findByLevelAndAndDept(level , dept);
    }

    public GroupKPI getOne(int id){
        return groupKPIDAO.getOne(id);
    }

    public List<GroupKPI> getByDept(String dept){
        return groupKPIDAO.findByDept(dept);
    }
}
