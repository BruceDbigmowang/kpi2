package com.cx.kpi2.service;

import com.cx.kpi2.dao.PeopleDAO;
import com.cx.kpi2.pojo.People;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PeopleService {
    @Autowired
    PeopleDAO peopleDAO;

    public People getByAccount(String account){
        return peopleDAO.findByAccount(account);
    }

}
