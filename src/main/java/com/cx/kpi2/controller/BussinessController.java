package com.cx.kpi2.controller;

import com.cx.kpi2.pojo.Bussiness;
import com.cx.kpi2.service.BussinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BussinessController {
    @Autowired
    BussinessService bussinessService;

    @RequestMapping("/getAllBussiness")
    public List<Bussiness> Bussiness(){
        return bussinessService.getAllBussiness();
    }
}
