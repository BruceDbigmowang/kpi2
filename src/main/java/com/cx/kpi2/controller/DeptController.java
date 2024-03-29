package com.cx.kpi2.controller;

import com.cx.kpi2.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DeptController {
    @Autowired
    DeptService deptService;

    /**
     * KPI考核结果查询中，表格是拼接显示出来的
     * 用于显示所有部门
     * @return
     */
    @RequestMapping("/getAllDept")
    public List<String> getDepts(){
        return deptService.getAllDept();
    }
}
