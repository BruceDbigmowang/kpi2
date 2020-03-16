package com.cx.kpi2.dao;

import com.cx.kpi2.pojo.DeptTotalScore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeptTotalScoreDAO extends JpaRepository<DeptTotalScore , Integer> {
    List<DeptTotalScore> getByBussinessAndDeptAndYearMonth(String bussiness , String dept , String yearMonth);

    List<DeptTotalScore> getByBussinessAndYearMonth(String bussiness , String yearMonth);
}
