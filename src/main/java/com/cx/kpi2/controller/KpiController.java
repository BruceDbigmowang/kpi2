package com.cx.kpi2.controller;

import com.cx.kpi2.pojo.Account;
import com.cx.kpi2.pojo.Otherkpi;
import com.cx.kpi2.pojo.TestCategory;
import com.cx.kpi2.service.AccountService;
import com.cx.kpi2.service.CategoryContentService;
import com.cx.kpi2.service.OtherkpiService;
import com.cx.kpi2.service.TestCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class KpiController {
    @Autowired
    OtherkpiService otherkpiService;
    @Autowired
    TestCategoryService testCategoryService;
    @Autowired
    CategoryContentService categoryContentService;
    @Autowired
    AccountService accountService;

    /**
     * 进入到打分页面，显示一级标题
     * @return
     */
    @RequestMapping("/getFirstLevel")
    public List<String> getTestCategoryByDept(HttpSession session){
        Account user = (Account)session.getAttribute("user");
        String account = user.getAccount();
        Account man = accountService.getUserByAccount(account);
        String dept = man.getDept();
//        String dept = user.getDept();
        return testCategoryService.getAllByDept(dept);
    }

    /**
     * 显示二级标题
     * @param category
     * @return
     */
    @RequestMapping("/getSecondLevel")
    public List<String> getContentByCategory(@RequestParam("category")String category){
        return categoryContentService.getByCategory(category);
    }

    /**
     * 由于显示结果仅有三级，此处显示最下层明细
     * @param content
     * @return
     */
    @RequestMapping("/getKpiDetail")
    public List<Otherkpi> getAllKpi(@RequestParam("content")String content){
        return otherkpiService.getKpiByContent(content);
    }
}
