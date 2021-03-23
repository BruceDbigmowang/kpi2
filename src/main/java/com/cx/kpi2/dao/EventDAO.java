package com.cx.kpi2.dao;

import com.cx.kpi2.pojo.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventDAO extends JpaRepository<Event , Integer> {
    /**
     * 根据事业部  时间查询某一事业部是否发生重大事项或扣分项
     */
    List<Event> findByBussinessAndYearMonth(String bussiness , String yearMonth);

    /**
     * 查询是否有扣分项
     */
    List<Event> findByBussinessAndCheckLevelAndYearMonth(String bussiness , String checkLevel , String yearMonth);
}
