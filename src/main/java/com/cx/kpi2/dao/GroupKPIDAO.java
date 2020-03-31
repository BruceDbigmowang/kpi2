package com.cx.kpi2.dao;

import com.cx.kpi2.pojo.GroupKPI;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupKPIDAO extends JpaRepository<GroupKPI , Integer> {
    List<GroupKPI> findByLevel(String level);
    List<GroupKPI> findByLevelAndAndDept(String level , String dept);
    List<GroupKPI> findByDept(String dept);
}
