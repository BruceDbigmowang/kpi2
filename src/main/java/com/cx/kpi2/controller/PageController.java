package com.cx.kpi2.controller;

import com.cx.kpi2.pojo.Account;
import com.cx.kpi2.pojo.People;
import com.cx.kpi2.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class PageController {

    @Autowired
    AccountService accountService;

    /**
     * 登录页
     * @return
     */
    @GetMapping("/")
    public String firstPage(){
        return "redirect:login";
    }

    /**
     * 登录页
     * @return
     */
    @GetMapping("/login")
    public String toLogin(){
        return "publicPage/login";
    }

    /**
     * 登录后 HOME页
     * @return
     */
    @GetMapping("/index")
    public String tohome(){
        return "publicPage/index";
    }

    /**
     * 点击进入到评分界面中
     * @return
     */
    @GetMapping("/regard")
    public String toRegard(){
        return  "publicPage/Regard";
    }

    @GetMapping("/regardNew")
    public String toRegardNew(){
        return  "publicPage/RegardSecond";
    }

    /**
     * 根据所属的部门加载不同的数据
     * 特殊：
     * 人事还需要根据所属的事业部加载不同的数据
     * @param session
     * @return
     */
    @RequestMapping("/getFirstLevel")
    public String getTestCategoryByDept(HttpSession session){
        People user = (People) session.getAttribute("user");
        String account = user.getAccount();
        Account man = accountService.getUserByAccount(account);
        /**
         * 判断登录的账号的所属部门
         * 人事与其它部门不同
         * 人事的各个事业部也有区别
         */
        String dept = man.getDept();
        if(dept.equals("人事")){
            return "redirect:hrKpiModel";
        }else{
            return "redirect:otherFirstLevel";
        }

    }

    /**
     * 跳转至 每月所有事业部所有KPI统计的界面
     * @return
     */
    @GetMapping("/foreAllBussiness")
    public String toAllBussiness(){
        return "publicPage/AllBussiness";
    }

    /**
     * 跳转至 每个月某一事业部的KPI考核结果的界面
     * @return
     */
    @GetMapping("/foreOneBussiness")
    public String toOneBussiness(){
        return "publicPage/OneBussiness";
    }

    /**
     * 跳转至 事业部下具体某部门KPI考核得分
     * @return
     */
    @GetMapping("/foreOneDept")
    public String toOneDept(){
        return "publicPage/OneDept";
    }

    /**
     * 跳转至 某事业部所有部门明细
     * @return
     */
    @GetMapping("/foreAllDetail")
    public String toAllDetail(){
        return "publicPage/AllDetail";
    }

    /**
     * 跳转至 所有事业部年度报表
     * @return
     */
    @GetMapping("/foreAnnualStatement")
    public String toAnnualStatment(){
        return "publicPage/AnnualStatement";
    }

    /**
     * 跳转至 集团各部门KPI考核
     * @return
     */
    @GetMapping("/foreGroupRegard")
    public String toGroupRegard(){
        return "publicPage/GroupRegard";
    }

    /**
     * 跳转至 集团各部门KPI考核结果
     * @return
     */
    @GetMapping("/foreGroupResult")
    public String toAllResult(){
        return "publicPage/AllDeptResult";
    }

    /**
     * 跳转至 集团各部门KPI考核明细
     * @return
     */
    @GetMapping("/forOneDeptResult")
    public String toOneDeptResult(){
        return "publicPage/OneDeptResult";
    }

    /**
     * 跳转至 集团各部门KPI考核结果
     * @return
     */
    @GetMapping("/foreAllDeptAnnualResult")
    public String toAnnualResult(){
        return "publicPage/AllDeptAnnualResult";
    }

    @GetMapping("/foreBussinessSecond")
    public String toBussinessSecond(){
        return "publicPage/AllBussinessSecond";
    }

    @GetMapping("/foreOneBussinessSecond")
    public String toOneBussinessSecond(){
        return "publicPage/OneBussinessSecond";
    }

    @GetMapping("/foreOneDeptSecond")
    public String toOneDeptSecond(){
        return "publicPage/OneDeptSecond";
    }

    @GetMapping("/foreAnnualStatementSecond")
    public String toAnnualStatmentSecond(){
        return "publicPage/AnnualStatementSecond";
    }

}
