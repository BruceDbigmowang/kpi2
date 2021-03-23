package com.cx.kpi2.dao;

import com.cx.kpi2.pojo.DeptLast;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeptLastDAO extends JpaRepository<DeptLast , Integer> {
    /**
     * 根据事业部 + 部门 + 年月
     * 查询出 某一部门对某一事业部什么时候的打分总和 * 权重
     */
    List<DeptLast> findByBussinessAndDeptAndYearMonth(String bussiness , String dept , String yearMonth);
}
