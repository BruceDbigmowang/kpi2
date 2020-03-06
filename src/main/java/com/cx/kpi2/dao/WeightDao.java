package com.cx.kpi2.dao;

import com.cx.kpi2.pojo.Weight;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeightDao extends JpaRepository<Weight , Integer> {
    Weight getByBussinessAndDept(String bussiness , String dept);
}
