package com.cx.kpi2.service;

import com.cx.kpi2.dao.AccountDAO;
import com.cx.kpi2.pojo.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    @Autowired
    AccountDAO accountDAO;


    public  Account getUserByAccount(String account){
        return accountDAO.getByAccount(account);
    }

}
