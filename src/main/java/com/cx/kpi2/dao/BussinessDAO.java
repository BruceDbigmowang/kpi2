package com.cx.kpi2.dao;

import com.cx.kpi2.pojo.Bussiness;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.constraints.Max;
import java.util.List;

public interface BussinessDAO extends JpaRepository<Bussiness , Integer> {
}
