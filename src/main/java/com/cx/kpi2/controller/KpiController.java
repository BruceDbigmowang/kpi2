package com.cx.kpi2.controller;

import com.cx.kpi2.dao.HrkpiDAO;
import com.cx.kpi2.dao.OtherkpiDAO;
import com.cx.kpi2.pojo.*;
import com.cx.kpi2.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
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
    @Autowired
    RecordService recordService;
    @Autowired
    BussinessService bussinessService;
    @Autowired
    HrkpiService hrkpiService;
    @Autowired
    DeptScoreService deptScoreService;
    @Autowired
    DeptTotalService deptTotalService;
    @Autowired
    WeightService weightService;
    @Autowired
    CompletKpiService completKpiService;
    @Autowired
    HrkpiDAO hrkpiDAO;
    @Autowired
    OtherkpiDAO otherkpiDAO;

    /**
     * 进入到打分页面，显示一级标题
     * @return
     */


    /**
     * 除人事以外，其它部门所有事业部均使用一个模板
     * @param session
     * @return
     */
    @RequestMapping("/otherFirstLevel")
    public List<TestCategory> getDataByDept(HttpSession session){
        People user = (People)session.getAttribute("user");
        String dept = accountService.getUserByAccount(user.getAccount()).getDept();
        System.out.println(dept);
        return testCategoryService.getAllByDept(dept);
    }

    @RequestMapping("/otherFirstLevel2")
    public List<TestCategory> getDataByDept2(@RequestParam("dept")String dept){
        dept = dept.substring(2);
        System.out.println(dept);
        return testCategoryService.getAllByDept(dept);
    }

    /**
     * 人事KPI考核均为考核项目对应一个考核内容
     * 所以只要判断所属事业部
     * 人事KPI考核将所有事业部分为三类，使用三个模板
     * @return
     */
    @RequestMapping("/hrKpiModel")
    public List<Hrkpi> getAllHrKpi(@RequestParam("bussiness")String bussiness){
        Bussiness bussiness1 = bussinessService.getByBussiness(bussiness);
        String type = bussiness1.getCategory();
       //查询出所属事业部的种类
        //根据所属事业部的分类查询出其kpi考核的具体项目
        return hrkpiService.getByBussiness(type);
    }

    /**
     * 显示二级标题
     * @param first
     * @return
     */
    @RequestMapping("/getSecondLevel")
    public List<CategoryContent> getContentByCategory(@RequestParam("first")int first){
        return categoryContentService.getByCategory(first);
    }

    /**
     * 由于显示结果仅有三级，此处显示最下层明细
     * @param second
     * @return
     */
    @RequestMapping("/getKpiDetail")
    public List<Otherkpi> getAllKpi(@RequestParam("first")int first ,@RequestParam("second")int second) throws Exception{
        CategoryContent categoryContent = categoryContentService.getById(second);
        String content = categoryContent.getContent();
        System.out.println("执行到此处");
        return otherkpiService.getKpiByContent(first, second);
    }

    @PostMapping("/writeRecord")
    public String wirteOneRecord(@RequestParam(value = "kpiNo[]")int[] kpiNos , @RequestParam(value = "score[]")int[] scores , @RequestParam("bussiness")String bussiness , HttpSession session){
        People user = (People) session.getAttribute("user");
        String account = user.getAccount();
        String dept = accountService.getUserByAccount(account).getDept();
        BigDecimal deptWeight = new BigDecimal(0);
        Weight bussinessDeptWeight = weightService.getByBussinessAndDept(bussiness , dept);
        if(null != bussinessDeptWeight){
            deptWeight = deptWeight.add(bussinessDeptWeight.getWeight());
        }else{
            return "未查询到该事业部下该部门的考核权重";
        }
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH);
        String targetTime;
        if(month == 0){
            year = year-1;
            targetTime = year+"_"+"12";
        }else{
            targetTime = year+"_"+month;
        }
        //将页面中所有数据写入到Record表中
        for(int i = 0 ; i < kpiNos.length ; i++){
            int kpiNo = kpiNos[i];
            int score = scores[i];
            Record record = new Record();
            record.setBussiness(bussiness);
            record.setDept(dept);
            record.setKpiNo(kpiNo);
            record.setScore(score);
            record.setYearmonth(targetTime);
            recordService.writeRecord(record);
        }
        //判断所属部门是否是：人事 ————人事属于特殊情况另当别论，第一段代码中列出
        //根据所属部门查询相关权重，KPI考核等级进行相关计算
        BigDecimal totalScore = new BigDecimal(0);
        if(dept.equals("人事")){
            for(int i = 0 ; i < kpiNos.length ; i++){
                int kpiNo = kpiNos[i];
                Hrkpi hrkpi = hrkpiService.getById(kpiNo);
                BigDecimal weight = hrkpi.getWeight();
                String testLevel = hrkpi.getTestLevel();
                if(testLevel.equals("S")){
                    if(scores[i] != 0){

                        totalScore = totalScore.add(new BigDecimal(0));

//                        DeptScore deptScore = new DeptScore();
//                        deptScore.setBussiness(bussiness);
//                        deptScore.setDept("人事");
//                        deptScore.setWeight(weight);
//                        deptScore.setScore(new BigDecimal(1));
//                        deptScore.setYearMonth(year+"_"+month);
//                        deptScoreService.saveOne(deptScore);
                    }else{
                        DeptScore deptScore = new DeptScore();
                        deptScore.setBussiness(bussiness);
                        deptScore.setDept("人事");
                        deptScore.setWeight(deptWeight);
                        deptScore.setScore(new BigDecimal(0));
                        deptScore.setYearMonth(targetTime);
                        deptScoreService.saveOne(deptScore);

                        DeptTotalScore deptTotalScore = new DeptTotalScore();
                        deptTotalScore.setBussiness(bussiness);
                        deptTotalScore.setDept("人事");
                        deptTotalScore.setScore(new BigDecimal(0));
                        deptScore.setScore(new BigDecimal(0));
                        deptTotalService.saveOne(deptTotalScore);

                        completKpiService.writeRecord(bussiness , dept , targetTime);
                        return "该事业部发生重大事项，已记录在案";
                    }
                }else if(testLevel.equals("A")){
                    if(scores[i]!=0){

                        totalScore = totalScore.add(new BigDecimal(scores[i]));

//                        DeptScore deptScore = new DeptScore();
//                        deptScore.setBussiness(bussiness);
//                        deptScore.setDept("人事");
//                        deptScore.setWeight(weight);
//                        deptScore.setScore((new BigDecimal(scores[i])));
//                        deptScore.setYearMonth(year+"_"+month);
//                        deptScoreService.saveOne(deptScore);
                    }
                }else if(testLevel.equals("B")){
                    if(scores[i]!=0){

                        totalScore = totalScore.add(new BigDecimal(scores[i]));

//                        DeptScore deptScore = new DeptScore();
//                        deptScore.setBussiness(bussiness);
//                        deptScore.setDept("人事");
//                        deptScore.setWeight(weight);
//                        deptScore.setScore((new BigDecimal(scores[i])).multiply(weight));
//                        deptScore.setYearMonth(year+"_"+month);
//                        deptScoreService.saveOne(deptScore);
                    }else{

                        totalScore = totalScore.add(new BigDecimal(0));

//                        DeptScore deptScore = new DeptScore();
//                        deptScore.setBussiness(bussiness);
//                        deptScore.setDept("人事");
//                        deptScore.setWeight(weight);
//                        deptScore.setScore(new BigDecimal(0));
//                        deptScore.setYearMonth(year+"_"+month);
//                        deptScoreService.saveOne(deptScore);
                    }
                }
            }
            DeptScore deptScore = new DeptScore();
            deptScore.setBussiness(bussiness);
            deptScore.setDept("人事");
            deptScore.setWeight(deptWeight);
            deptScore.setScore(totalScore);
             deptScore.setYearMonth(targetTime);
             deptScoreService.saveOne(deptScore);
        }else{
            for(int i = 0 ; i < kpiNos .length ; i++){
                int kpiNo = kpiNos[i];
                Otherkpi otherkpi = otherkpiService.getById(kpiNo);
                BigDecimal weight = otherkpi.getWeight();
                String testLevel = otherkpi.getTestLevel();
                if(testLevel.equals("S")){
                    if(scores[i] != 0){

                        totalScore = totalScore.add(new BigDecimal(0));

//                        DeptScore deptScore = new DeptScore();
//                        deptScore.setBussiness(bussiness);
//                        deptScore.setDept(dept);
//                        deptScore.setWeight(weight);
//                        deptScore.setScore(new BigDecimal(1));
//                        deptScore.setYearMonth(year+"_"+month);
//                        deptScoreService.saveOne(deptScore);
                    }else{
                        DeptScore deptScore = new DeptScore();
                        deptScore.setBussiness(bussiness);
                        deptScore.setDept(dept);
                        deptScore.setWeight(deptWeight);
                        deptScore.setScore(new BigDecimal(0));
                        deptScore.setYearMonth(targetTime);
                        deptScoreService.saveOne(deptScore);
                        completKpiService.writeRecord(bussiness , dept , targetTime);
                        return "该事业部发生重大事项，已记录在案";
                    }
                }else if(testLevel.equals("A")){
                    if(scores[i]!=0){

                        totalScore = totalScore.add(new BigDecimal(scores[i]));

//                        DeptScore deptScore = new DeptScore();
//                        deptScore.setBussiness(bussiness);
//                        deptScore.setDept(dept);
//                        deptScore.setWeight(weight);
//                        deptScore.setScore((new BigDecimal(scores[i])));
//                        deptScore.setYearMonth(year+"_"+month);
//                        deptScoreService.saveOne(deptScore);
                    }
                }else if(testLevel.equals("B")){
                    if(scores[i]!=0){

                        totalScore = totalScore.add((new BigDecimal(scores[i])));

//                        DeptScore deptScore = new DeptScore();
//                        deptScore.setBussiness(bussiness);
//                        deptScore.setDept(dept);
//                        deptScore.setWeight(weight);
//                        deptScore.setScore((new BigDecimal(scores[i])).multiply(weight));
//                        deptScore.setYearMonth(year+"_"+month);
//                        deptScoreService.saveOne(deptScore);
                    }else{

                        totalScore = totalScore.add(new BigDecimal(0));

//                        DeptScore deptScore = new DeptScore();
//                        deptScore.setBussiness(bussiness);
//                        deptScore.setDept(dept);
//                        deptScore.setWeight(weight);
//                        deptScore.setScore(new BigDecimal(0));
//                        deptScore.setYearMonth(year+"_"+month);
//                        deptScoreService.saveOne(deptScore);
                    }
                }
            }
            DeptScore deptScore = new DeptScore();
            deptScore.setBussiness(bussiness);
            deptScore.setDept(dept);
            deptScore.setWeight(deptWeight);
            deptScore.setScore(totalScore);
            deptScore.setYearMonth(targetTime);
            deptScoreService.saveOne(deptScore);
        }
        //统计某事业部 某部门的总分数
        List<DeptScore> deptScores = deptScoreService.getAMonth(bussiness , dept , targetTime);
        //检查该部门 有无发生重大事项，若发生则该事业部该部门成绩为0
        //若无发生重要事项，则正常计算
//        BigDecimal total = new BigDecimal(0);
//        for(int i = 0 ; i < deptScores.size() ; i++){
//            DeptScore deptScore = deptScores.get(i);
//            if(deptScore.getTestLevel().equals("S")){
//                if(deptScore.getScore().equals(0)){
//                    DeptTotalScore deptTotalScore = new DeptTotalScore();
//                    deptTotalScore.setBussiness(bussiness);
//                    deptTotalScore.setDept(dept);
//                    deptTotalScore.setYearMonth(year+"_"+month);
//                    deptTotalScore.setScore(new BigDecimal(0));
//                    deptTotalService.saveOne(deptTotalScore);
//                    break;
//                }else{
//                    total = total.add(new BigDecimal(0));
//                }
//
//            }else if(deptScore.getTestLevel().equals("A")){
//                total = total.add(deptScore.getScore());
//            }else if(deptScore.getTestLevel().equals("B")){
//                total = total.add(deptScore.getScore().multiply(deptScore.getWeight()));
//            }
        if(deptScores.size() == 0){
            return "程序错误，KPI考核记录失败";
        }else{
            BigDecimal total = deptScores.get(0).getScore();
            DeptTotalScore deptTotalScore = new DeptTotalScore();
            deptTotalScore.setBussiness(bussiness);
            deptTotalScore.setDept(dept);
            deptTotalScore.setYearMonth(targetTime);
            deptTotalScore.setScore(total.multiply(deptWeight));
            deptTotalService.saveOne(deptTotalScore);
        }
    completKpiService.writeRecord(bussiness , dept , targetTime);

    return "KPI考核记录成功";
    }

    @RequestMapping("/checkSubmit")
    public String checkData(@RequestParam(value = "kpiNo[]")int[] kpiNos , @RequestParam(value = "score[]")int[] scores , @RequestParam("bussiness")String bussiness , HttpSession session){
        People user = (People) session.getAttribute("user");
        String dept = accountService.getUserByAccount(user.getAccount()).getDept();
        String errorInfo = "";
        if(dept.equals("人事")){
            for(int i = 0 ; i < kpiNos.length ; i++){
                Hrkpi hrkpi = hrkpiDAO.getOne(kpiNos[i]);
                int scoreMax = hrkpi.getScoreMax();
                if(scores[i] >scoreMax){
                    errorInfo = errorInfo+"["+hrkpi.getTestDetail()+"]";
                }
            }
        }else{
            for(int i = 0 ; i < kpiNos.length ; i++){
                Otherkpi otherkpi = otherkpiDAO.getOne(kpiNos[i]);
                int scoreMax = otherkpi.getScoreMax();
                if(scores[i] >scoreMax){
                    errorInfo = errorInfo+"["+otherkpi.getTestDetail()+"]";
                }
            }
        }
        if("".equals(errorInfo)){
            return "OK";
        }else{
            return errorInfo;
        }
    }
}
