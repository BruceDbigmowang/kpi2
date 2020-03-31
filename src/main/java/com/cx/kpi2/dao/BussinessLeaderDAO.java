package com.cx.kpi2.dao;

import com.cx.kpi2.pojo.BussinessLeader;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BussinessLeaderDAO extends JpaRepository<BussinessLeader , Integer> {
    List<BussinessLeader> findByAccount(String account);
}
