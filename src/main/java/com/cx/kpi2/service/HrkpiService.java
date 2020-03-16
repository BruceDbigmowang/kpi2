package com.cx.kpi2.service;

import com.cx.kpi2.dao.HrkpiDAO;
import com.cx.kpi2.pojo.Hrkpi;
import com.cx.kpi2.pojo.Otherkpi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HrkpiService {
    @Autowired
    HrkpiDAO hrkpiDAO;

    public List<Hrkpi> getByBussiness(String bussiness){
        return hrkpiDAO.getByBussiness(bussiness);
    }

    public Hrkpi getById(int id){
        return hrkpiDAO.getOne(id);
    }

}
