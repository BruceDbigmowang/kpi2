package com.cx.kpi2.dao;

import com.cx.kpi2.pojo.GroupLeader;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupLeaderDAO extends JpaRepository<GroupLeader , Integer> {
    List<GroupLeader> findByAccount(String account);
}
