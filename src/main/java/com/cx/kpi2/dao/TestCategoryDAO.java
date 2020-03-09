package com.cx.kpi2.dao;

import com.cx.kpi2.pojo.TestCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestCategoryDAO extends JpaRepository<TestCategory , Integer> {
    List<TestCategory> findByDept(String dept);
}
