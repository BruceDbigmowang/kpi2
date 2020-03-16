package com.cx.kpi2.controller;

import com.cx.kpi2.pojo.Account;
import com.cx.kpi2.pojo.People;
import com.cx.kpi2.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class IdentityController {
    @Autowired
    AccountService accountService;


    @PostMapping("/getIdentity")
    public String getUserIdentity(HttpSession session){
        People user = (People) session.getAttribute("user");
        String account = user.getAccount();
        String dept = accountService.getUserByAccount(account).getDept();
        if(dept.equals("人事")){
            return "HR";
        }else{
            return "other";
        }
    }

    @RequestMapping("/sureIdentity")
    public boolean canRegard(HttpSession session){
        People user = (People)session.getAttribute("user");
        String account = user.getAccount();
        Account man = accountService.getUserByAccount(account);
        if(null == man){
            return false;
        }else{
            return true;
        }
    }
}
