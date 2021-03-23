package com.cx.kpi2.controller.mark;

import com.cx.kpi2.pojo.Account;
import com.cx.kpi2.pojo.People;
import com.cx.kpi2.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class MarkInsertController {

    @Autowired
    ScoreService scoreService;

    @RequestMapping(value = "writeScore" , method = RequestMethod.POST)
    public String toWriteScore(String bussiness , int[] kpiNos , int[] scores , HttpSession session){
        People user = (People)session.getAttribute("user");
        Account account = scoreService.findDeptInfo(user.getAccount());
        return scoreService.writeScoreData(bussiness , account.getDept() , kpiNos , scores);
    }
}
