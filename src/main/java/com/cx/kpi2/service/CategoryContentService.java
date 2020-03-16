package com.cx.kpi2.service;

import com.cx.kpi2.dao.CategoryContentDAO;
import com.cx.kpi2.pojo.CategoryContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryContentService {
    @Autowired
    CategoryContentDAO categoryContentDAO;

    public List<CategoryContent> getByCategory(int category){
        List<CategoryContent> categoryContentList = categoryContentDAO.findByCategory(category);
//        List<String> categoryContents = new ArrayList<>();
//        for(int i = 0 ; i < categoryContentList.size() ; i++){
//            categoryContents.add(categoryContentList.get(i).getContent());
//        }
        return categoryContentList;
    }

    public CategoryContent getById(int id){
        return categoryContentDAO.getOne(id);
    }
}
