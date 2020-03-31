package com.cx.kpi2.service;

import com.cx.kpi2.dao.LeaderDAO;
import com.cx.kpi2.pojo.Leader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaderService {
    @Autowired
    LeaderDAO leaderDAO;

    public boolean isLeader(String account){
        List<Leader> leaderList = leaderDAO.findByAccount(account);
        if(leaderList==null || leaderList.size() == 0){
            return false;
        }else{
            return true;
        }
    }

    public boolean isLeaderToDept(String account , String dept){
        List<Leader> leaderList = leaderDAO.findByAccountAndDept(account , dept);
        if(leaderList==null || leaderList.size() == 0){
            return false;
        }else{
            return true;
        }
    }

    public String getDept(String account){
        List<Leader> leaderList = leaderDAO.findByAccount(account);
        return leaderList.get(0).getDept();
    }

    public String getName(String account){
        List<Leader> leaderList = leaderDAO.findByAccount(account);
        return leaderList.get(0).getName();
    }

    public String findByDept(String dept){
        List<Leader> leaderList = leaderDAO.findByDept(dept);
        if(leaderList != null && leaderList.size() != 0){
            return leaderList.get(0).getAccount();
        }else{
            return null;
        }
    }
}
