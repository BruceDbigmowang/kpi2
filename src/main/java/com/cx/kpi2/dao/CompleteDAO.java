package com.cx.kpi2.dao;

import com.cx.kpi2.pojo.Complete;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompleteDAO extends JpaRepository<Complete , Integer> {

    List<Complete> findByBussinessAndDeptAndYearMonth(String bussiness , String dept , String yearMonth);
}
