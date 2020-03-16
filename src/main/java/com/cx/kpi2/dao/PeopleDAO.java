package com.cx.kpi2.dao;

import com.cx.kpi2.pojo.People;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PeopleDAO extends JpaRepository<People , Integer> {
    People findByAccount(String account);
}
