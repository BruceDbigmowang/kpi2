package com.cx.kpi2.controller;

import com.cx.kpi2.pojo.Account;
import com.cx.kpi2.service.AccountService;
import com.cx.kpi2.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpSession;

@RestController
public class AccountController {
    @Autowired
    AccountService accountService;

    /**
     * 登陆功能：
     * 前台将账号与密码封装为user的对象（此处仅供模拟使用）
     * 若有需要可将user拆分开返回后台
     * @param user
     * @param session
     * @return
     */
    @PostMapping("/forelogin")
    public Object login(@RequestBody Account user , HttpSession session){
        System.out.println("---执行到此处---");
        String account = user.getAccount();
        account = HtmlUtils.htmlEscape(account);

        Account man = accountService.login(account , user.getPassword());
        if(null == user){
            String message = "登陆异常，请联系管理员";
            return Result.fail(message);
        }else{
            session.setAttribute("user", man);
            return Result.success();
        }
    }

}
