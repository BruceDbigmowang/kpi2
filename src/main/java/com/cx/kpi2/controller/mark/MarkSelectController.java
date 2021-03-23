package com.cx.kpi2.controller.mark;

import com.cx.kpi2.pojo.*;
import com.cx.kpi2.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class MarkSelectController {
    @Autowired
    ScoreService scoreService;



    /**
     * 根据登录的账号判断需要对哪些事业部进行打分
     */
    @RequestMapping(value = "findCheckBussiness" , method = RequestMethod.GET)
    public List<BussinessWeight> findBussinessByDept(HttpSession session){
        People user = (People)session.getAttribute("user");
        Account account = scoreService.findDeptInfo(user.getAccount());
        return scoreService.loadBussiness(account);
    }

    /**
     * 根据事业部性质以及部门查询所有的考核明细
     */
    @RequestMapping(value = "findCheckDetail" , method = RequestMethod.GET)
    public List<CheckDetail> findDetailsByAccount(String bussiness , HttpSession session){
        People user = (People)session.getAttribute("user");
        Account account = scoreService.findDeptInfo(user.getAccount());
        return scoreService.getAllDetail(account , bussiness);
    }
}
