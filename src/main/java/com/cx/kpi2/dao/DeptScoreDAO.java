package com.cx.kpi2.dao;

import com.cx.kpi2.pojo.DeptScore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeptScoreDAO extends JpaRepository<DeptScore, Integer> {
    List<DeptScore> getByBussinessAndDeptAndYearMonth(String bussiness , String dept , String yearMonth);

    List<DeptScore>getByBussinessAndYearMonth(String bussiness , String yearMonth);
}
