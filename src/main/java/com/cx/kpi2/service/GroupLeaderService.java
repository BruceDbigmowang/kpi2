package com.cx.kpi2.service;

import com.cx.kpi2.dao.GroupLeaderDAO;
import com.cx.kpi2.pojo.GroupLeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupLeaderService {
    @Autowired
    GroupLeaderDAO groupLeaderDAO;

    public boolean isGroupLeader(String account){
        List<GroupLeader> groupLeaderList = groupLeaderDAO.findByAccount(account);
        if(groupLeaderList==null || groupLeaderList.size() == 0){
            return false;
        }else{
            return true;
        }
    }

    public String getName(String account){
        List<GroupLeader> groupLeaderList = groupLeaderDAO.findByAccount(account);
        return groupLeaderList.get(0).getName();
    }

    public List<GroupLeader> getAll(){
        List<GroupLeader> groupLeaderList = groupLeaderDAO.findAll();
        return groupLeaderList;
    }
}
