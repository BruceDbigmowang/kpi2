package com.cx.kpi2.service;

import com.cx.kpi2.dao.PresidentDAO;
import com.cx.kpi2.pojo.President;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PresidentService {
    @Autowired
    PresidentDAO presidentDAO;

    public boolean isPresident(String account){
        List<President> presidentList = presidentDAO.findByAccount(account);
        if(presidentList == null || presidentList.size() == 0){
            return false;
        }else{
            return true;
        }
    }

    public String getName(String account){
        List<President> presidentList = presidentDAO.findByAccount(account);
        return presidentList.get(0).getName();
    }

    public String findPresident(){
        List<President> presidentList = presidentDAO.findAll();
        if(presidentList != null && presidentList.size() != 0){
            return presidentList.get(0).getAccount();
        }else{
            return null;
        }
    }
}
