package com.cx.kpi2.dao;

import com.cx.kpi2.pojo.President;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PresidentDAO extends JpaRepository<President , Integer> {
    List<President> findByAccount(String account);
}
