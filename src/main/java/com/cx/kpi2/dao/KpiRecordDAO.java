package com.cx.kpi2.dao;

import com.cx.kpi2.pojo.KpiRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KpiRecordDAO extends JpaRepository<KpiRecord , Integer> {

    List<KpiRecord> findByBussinessAndDeptAndYearMonthAndKpiNo(String bussiness , String dept , String yearMonth , int kpiNo);
}
