package com.cx.kpi2.dao;

import com.cx.kpi2.pojo.Dept;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeptDAO extends JpaRepository<Dept , Integer> {
}
