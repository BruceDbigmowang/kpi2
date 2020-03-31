package com.cx.kpi2.service;

import com.cx.kpi2.dao.BussinessLeaderDAO;
import com.cx.kpi2.pojo.BussinessLeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BussinessLeaderService {
    @Autowired
    BussinessLeaderDAO bussinessLeaderDAO;

    public boolean isBussinessLeader(String account){
        List<BussinessLeader> bussinessLeaderList = bussinessLeaderDAO.findByAccount(account);
        if(bussinessLeaderList == null || bussinessLeaderList.size() == 0){
            return false;
        }else{
            return true;
        }
    }

    public String getName(String account){
        List<BussinessLeader> bussinessLeaderList = bussinessLeaderDAO.findByAccount(account);
        return bussinessLeaderList.get(0).getName();
    }

    public String getBussiness(String account){
        List<BussinessLeader> bussinessLeaderList = bussinessLeaderDAO.findByAccount(account);
        return bussinessLeaderList.get(0).getBussiness();
    }

    public List<BussinessLeader> findAll(){
        return bussinessLeaderDAO.findAll();
    }
}
