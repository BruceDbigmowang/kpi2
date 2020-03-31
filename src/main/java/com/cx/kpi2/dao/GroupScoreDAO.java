package com.cx.kpi2.dao;

import com.cx.kpi2.pojo.GroupScore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface GroupScoreDAO extends JpaRepository<GroupScore , Integer> {
    List<GroupScore> findByDeptAndYearMonth(String dept , String yearMonth);
    List<GroupScore> findByKpiNoAndYearMonth(int kpiNo , String yearMonth);
    List<GroupScore> findByKpiNoAndScorerAndYearMonth(int kpiNo , String scorer , String yearMonth);
    List<GroupScore> findByScorerAndScoreAndYearMonth(String scorer , BigDecimal score , String yearMonth);
}
