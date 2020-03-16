package com.cx.kpi2.controller;

import com.cx.kpi2.pojo.Account;
import com.cx.kpi2.pojo.People;
import com.cx.kpi2.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class PageController {

    @Autowired
    AccountService accountService;

    @GetMapping("/")
    public String firstPage(){
        return "redirect:login";
    }

    @GetMapping("/login")
    public String toLogin(){
        return "publicPage/login";
    }

    @GetMapping("/index")
    public String tohome(){
        return "publicPage/index";
    }

    @GetMapping("/regard")
    public String toRegard(){
        return  "publicPage/Regard";
    }

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


    @GetMapping("/foreAllBussiness")
    public String toAllBussiness(){
        return "publicPage/AllBussiness";
    }

    @GetMapping("/foreOneBussiness")
    public String toOneBussiness(){
        return "publicPage/OneBussiness";
    }

    @GetMapping("/foreOneDept")
    public String toOneDept(){
        return "publicPage/OneDept";
    }

}
