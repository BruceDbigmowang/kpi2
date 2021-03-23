package com.cx.kpi2.dao;

import com.cx.kpi2.pojo.CheckDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CheckDetailDAO extends JpaRepository<CheckDetail , Integer> {

    List<CheckDetail> findByDept(String dept);
}
