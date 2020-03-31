package com.cx.kpi2.dao;

import com.cx.kpi2.pojo.GroupRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRecordDAO extends JpaRepository<GroupRecord , Integer> {
    List<GroupRecord> findByAccountAndYearMonth(String account , String yearMonth);
}
