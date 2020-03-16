package com.cx.kpi2.dao;

import com.cx.kpi2.pojo.CompleteKpi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompletKpiDAO extends JpaRepository<CompleteKpi , Integer> {

    List<CompleteKpi> findByBussiness(String bussiness);

    List<CompleteKpi> findByDeptAndYearMonth(String dept , String yearMonth);

    List<CompleteKpi> findByBussinessAndDeptAndYearMonth(String bussiness , String dept , String yearMonth);
}
