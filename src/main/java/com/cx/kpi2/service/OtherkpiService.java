package com.cx.kpi2.service;

import com.cx.kpi2.dao.OtherkpiDAO;
import com.cx.kpi2.pojo.Otherkpi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OtherkpiService {
    @Autowired
    OtherkpiDAO otherkpiDAO;
    public List<Otherkpi> getKpiByContent(String content){
        return otherkpiDAO.findByTestContent(content);
    }
}
