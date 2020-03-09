package com.cx.kpi2.dao;

import com.cx.kpi2.pojo.CategoryContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryContentDAO extends JpaRepository<CategoryContent, Integer> {
    List<CategoryContent> findByCategory(String category);
}
