package com.cx.kpi2.service;

import com.cx.kpi2.dao.TestCategoryDAO;
import com.cx.kpi2.pojo.TestCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TestCategoryService {
    @Autowired
    TestCategoryDAO testCategoryDAO;

    public List<TestCategory> getAllByDept(String dept){
        List<TestCategory> categories = testCategoryDAO.findByDept(dept);
        return categories;
    }
    public TestCategory getById(int first){
        return testCategoryDAO.getOne(first);
    }
}
