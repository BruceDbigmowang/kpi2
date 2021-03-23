package com.cx.kpi2.dao;

import com.cx.kpi2.pojo.DeptSum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeptSumDAO extends JpaRepository<DeptSum , Integer> {

    /**
     * 根据部门  事业部 查询考核得分
     * 若未查询到  则为 0
     * 若查询到  则正常取值
     */
    List<DeptSum> findByBussinessAndDeptAndYearMonth(String bussiness , String dept , String yearMonth);
}
