package com.cx.kpi2.service;

import com.cx.kpi2.dao.BussinessDAO;
import com.cx.kpi2.pojo.Bussiness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BussinessService {
    @Autowired
    BussinessDAO bussinessDAO;

    public List<Bussiness> getAllBussiness(){
        List<Bussiness> bussinessList = bussinessDAO.findAll();
        return bussinessList;
    }

}
