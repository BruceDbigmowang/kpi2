package com.cx.kpi2.dao;

import com.cx.kpi2.pojo.BussinessWeight;
import javafx.scene.control.ListCell;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface BussinessWeightDAO extends JpaRepository<BussinessWeight , Integer> {
    /**
     * 根据部门判断其需要打分的事业部
     */
    List<BussinessWeight> findByDeptAndWeightNotIn(String dept , List<BigDecimal> weights);

    /**
     * 根据部门查询所有事业部
     * 使用该方法的为特例的部门，权重全为 0
     */
    List<BussinessWeight> findByDept(String dept);

    /**
     * 根据部门  事业部  查询具体权重
     */
    List<BussinessWeight> findByBussinessAndDept(String bussiness , String dept);
}
