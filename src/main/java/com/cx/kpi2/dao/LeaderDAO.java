package com.cx.kpi2.dao;

import com.cx.kpi2.pojo.Leader;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaderDAO extends JpaRepository<Leader , Integer> {
    List<Leader> findByAccount(String account);
    List<Leader> findByAccountAndDept(String account , String dept);
    List<Leader> findByDept(String dept);
}
