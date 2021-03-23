package com.cx.kpi2.dao;

import com.cx.kpi2.pojo.BussinessDivision;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BussinessDivisionDAO extends JpaRepository<BussinessDivision , Integer> {
    /**
     * 根据事业部名称来查询数据
     *
     * 有的部门对某些事业部打分时，会出现不同的打分项目
     * 用于识别事业部性质
     */
    List<BussinessDivision> findByBussinessName(String bussinessName);
}
