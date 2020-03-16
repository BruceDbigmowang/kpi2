package com.cx.kpi2.service;

import com.cx.kpi2.dao.WeightDAO;
import com.cx.kpi2.pojo.Weight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeightService {
    @Autowired
    WeightDAO weightDAO;

    public Weight getByBussinessAndDept(String bussiness , String dept){
        List<Weight> weightList = weightDAO.getByBussinessAndDept(bussiness , dept);
        if(weightList.size() == 1){
            return weightList.get(0);
        }else{
            return null;
        }
    }
}
