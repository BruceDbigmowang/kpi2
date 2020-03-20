package com.cx.kpi2.dao;

import com.cx.kpi2.pojo.Hrkpi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HrkpiDAO extends JpaRepository<Hrkpi , Integer> {
    List<Hrkpi> getByBussiness(String bussiness);
    List<Hrkpi> findById(int id);
}
