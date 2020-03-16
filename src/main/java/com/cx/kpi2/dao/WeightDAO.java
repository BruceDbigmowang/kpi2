package com.cx.kpi2.dao;

import com.cx.kpi2.pojo.Weight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeightDAO extends JpaRepository<Weight , Integer> {
    List<Weight> getByBussinessAndDept(String bussiness , String dept);
}
