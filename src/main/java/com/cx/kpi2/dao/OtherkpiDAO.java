package com.cx.kpi2.dao;

import com.cx.kpi2.pojo.Otherkpi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OtherkpiDAO extends JpaRepository<Otherkpi , Integer> {
    List<Otherkpi> findByTestContent(String content);
}
