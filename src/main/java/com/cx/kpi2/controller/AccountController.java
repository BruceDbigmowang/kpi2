package com.cx.kpi2.controller;

import com.cx.kpi2.dao.PeopleDAO;
import com.cx.kpi2.pojo.Account;
import com.cx.kpi2.pojo.People;
import com.cx.kpi2.service.AccountService;
import com.cx.kpi2.util.MD5;
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
    @Autowired
    PeopleDAO peopleDAO;

    /**
     * 登陆功能：
     * 前台将账号与密码封装为user的对象（此处仅供模拟使用）
     * 若有需要可将user拆分开返回后台
     * @param user
     * @param session
     * @return
     */
    @PostMapping("/forelogin")
    public Object login(@RequestBody People user , HttpSession session){

        String account = user.getAccount();
        account = HtmlUtils.htmlEscape(account);
        People man = peopleDAO.findByAccount(account);
        if(man == null){
            String message = "此账号无法登录该系统";
            return Result.fail(message);
        }else{
            String password = MD5.getMd5(user.getPassword());
            System.out.println(password);
            System.out.println(man.getPassword());
            if(!password.equals(man.getPassword())){
                String message = "密码错误";
                return Result.fail(message);
            }else{
                session.setAttribute("user", man);
                return Result.success();
            }
        }
    }

}
