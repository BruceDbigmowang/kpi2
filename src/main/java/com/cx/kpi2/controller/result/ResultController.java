package com.cx.kpi2.controller.result;

import com.cx.kpi2.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class ResultController {

    @Autowired
    ScoreService scoreService;

    /**
     * 加载所有有记录的时间
     */
    @RequestMapping(value = "loadRecordTime" , method = RequestMethod.GET)
    public List<String> findRecord(){
        return scoreService.loadAllRecordTime();
    }

    /**
     * 加载某个月 所有事业部的考核得分
     */
    @RequestMapping(value = "loadAllScoreData" , method = RequestMethod.GET)
    public Map<String , Object> getAllData(String yearMonth){
        return scoreService.loadAllScore(yearMonth);
    }

    /**
     * 加载某个月 某一事业部 所有部门的考核的分
     */
    @RequestMapping(value = "loadScoreByBussiness" , method = RequestMethod.GET)
    public Map<String , Object> getDataByBussiness(String bussiness , String yearMonth){
        return scoreService.loadAllScoreByBussiness(bussiness , yearMonth);
    }

    /**
     * 事业部 + 部门 + 年月 查询某一部门对某一事业部的考核明细
     */
    @RequestMapping(value = "getCheckDetailResult" , method = RequestMethod.GET)
    public Map<String , Object> getDataByBussinessAndDept(String bussiness , String dept , String yearMonth){
        return scoreService.findRegardDetail(bussiness , dept , yearMonth);
    }

    /**
     * 根据年份 获取一年中所有事业部的KPI考核得分
     */
    @RequestMapping(value = "getDataByYear" , method = RequestMethod.GET)
    public Map<String , Object> findAllDataByYear(String year){
        return scoreService.findAllDataByYear(year);
    }

}
