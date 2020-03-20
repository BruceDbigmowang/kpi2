package com.cx.kpi2.controller;

import com.cx.kpi2.dao.AccountDAO;
import com.cx.kpi2.dao.HrkpiDAO;
import com.cx.kpi2.dao.OtherkpiDAO;
import com.cx.kpi2.dao.RecordDAO;
import com.cx.kpi2.pojo.*;
import com.cx.kpi2.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@RestController
public class ShowDataController {
    @Autowired
    CompletKpiService completKpiService;
    @Autowired
    DeptTotalScoreService deptTotalScoreService;
    @Autowired
    DeptScoreService deptScoreService;
    @Autowired
    RecordService recordService;
    @Autowired
    RecordDAO recordDAO;
    @Autowired
    OtherkpiDAO otherkpiDAO;
    @Autowired
    HrkpiDAO hrkpiDAO;
    @Autowired
    AccountDAO accountDAO;
    @Autowired
    DetailService detailService;

    @RequestMapping("/allBussinessGetTime")
    public List<String> getAllTime(){
        return completKpiService.getRecordTime();
    }
    @RequestMapping("/oneBussinessGetTime")
    public List<String> getOneTime(@RequestParam("bussiness")String bussiness){
        return completKpiService.getRecordTimeByBussiness(bussiness);
    }

    /**
     * 根据时间来获取所有部门对所有事业部的KPI考核评分
     * 由于事业部与部门均是从数据库中获取得到，是按顺序的
     * 所以得到所有数据只要时间即可
     * 将一个事业部这个月的所有数据塞入到一个list中
     * 再将所有事业部的list塞入到一个总集合list中
     * @param yearMonth
     * @return
     */
    @RequestMapping("/getDataByTime")
    public List<List<BigDecimal>> getDataByYearMonth(@RequestParam("yearMonth")String yearMonth){
        return deptTotalScoreService.getLastScore(yearMonth);
    }

    @RequestMapping("/getAllTotalByTime")
    public List<BigDecimal> getTotalByYearMonth(@RequestParam("yearMonth")String yearMonth){
        return deptTotalScoreService.getTotalScore(yearMonth);
    }
    @RequestMapping("/getTopBussiness")
    public String getTop(@RequestParam("yearMonth")String yearMonth){
        return deptTotalScoreService.getGoodBussiness(yearMonth);
    }

    /**
     * 获取某事业部 各个考核部门给出的分数*权重得到的最后结果
     * @param bussiness
     * @param yearMonth
     * @return
     */
    @RequestMapping("/getOneBussinessScore")
    public List<DeptTotalScore> getOneBussinessData(@RequestParam("bussiness")String bussiness , @RequestParam("yearMonth")String yearMonth){
        return deptTotalScoreService.getAllDeptByBussinessAndYearMonth(bussiness , yearMonth);
    }

    @RequestMapping("/getOneBussinessAllDeptScore")
    public BigDecimal getOneBussinessAllData(@RequestParam("bussiness")String bussiness , @RequestParam("yearMonth")String yearMonth){

        List<Record> recordList = recordDAO.getByBussinessAndScoreAndYearmonth(bussiness , 0 , yearMonth);
        BigDecimal total = new BigDecimal(0);
        if(recordList.size() == 0){
            List<DeptTotalScore> deptTotalScoreList = deptTotalScoreService.getAllDeptByBussinessAndYearMonth(bussiness , yearMonth);

            for(int i = 0 ; i < deptTotalScoreList.size() ; i++){
                total = total.add(deptTotalScoreList.get(i).getScore());
            }
            return total;
        }else{
            for(int j = 0 ; j < recordList.size() ; j++){
                String dept = recordList.get(j).getDept();
                int kpiNo = recordList.get(j).getKpiNo();
                if(dept.equals("人事")){
                    String testLevel = hrkpiDAO.getOne(kpiNo).getTestLevel();
                    if(testLevel.equals("S")){
                        return total;
                    }
                }else{
                    List<Otherkpi> otherkpiList = otherkpiDAO.findById(kpiNo);
                    if(otherkpiList != null) {
                        String testLevel = otherkpiList.get(0).getTestLevel();
                        if (testLevel.equals("S")) {
                            return total;
                        }
                    }
                }
            }
        }

        List<DeptTotalScore> deptTotalScoreList = deptTotalScoreService.getAllDeptByBussinessAndYearMonth(bussiness , yearMonth);
        for(int i = 0 ; i < deptTotalScoreList.size() ; i++){
            total = total.add(deptTotalScoreList.get(i).getScore());
        }
        return total;
    }

    /**
     * 获取某事业部 各个考核部门给出的考核分数以及权重
     */
    @RequestMapping("/getOneBussinessKpiScore")
    public List<DeptScore> getKpiScore(@RequestParam("bussiness")String bussiness , @RequestParam("yearMonth")String yearMonth){
        return deptScoreService.getAllDept(bussiness , yearMonth);
    }

    /**
     * 判断某部门考评人对该事业部相关考核是否进行考核打分
     */
    @RequestMapping("/regardOrNot")
    public boolean getRegard(@RequestParam("bussiness")String bussiness , @RequestParam("dept")String dept , @RequestParam("yearMonth")String yearMonth){
        return completKpiService.completKpi(bussiness , dept,yearMonth);
    }

    @RequestMapping("/getKpiScorePro")
    public List<Integer> getScoreSimple(@RequestParam("kpiNo[]")int[] kpiNo , @RequestParam("bussiness")String bussiness , @RequestParam("dept")String dept , @RequestParam("yearMonth")String yearMonth){
        dept = dept.substring(2);
        List<Integer> scores = new ArrayList<>();
        for(int i = 0 ; i < kpiNo.length ; i++){
            Record record = recordService.getScore(bussiness,dept,kpiNo[i],yearMonth);
            scores.add(record.getScore());
        }
        return scores;
    }

    @RequestMapping("checkSubmited")
    public boolean checkHaveSubmit(@RequestParam("bussiness") String bussiness , HttpSession session){
        People user = (People)session.getAttribute("user");

        String dept = accountDAO.getByAccount(user.getAccount()).getDept();

        Calendar now = Calendar.getInstance();
        String yearMonth="";
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH);
        yearMonth = year+"_"+month;
        if(month == 0){
            yearMonth = year-1+"_"+12;
        }else{
            yearMonth = year+"_"+month;
        }
        System.out.println(bussiness);
        System.out.println(dept);
        System.out.println(yearMonth);
        return  completKpiService.submitedRegard(bussiness , dept , yearMonth);
    }
    /**
     * 使用表格的形式展现某事业部下各个考核部门的打分明细
     */
    @RequestMapping("/getAllDetail")
    public List<Detail> getallDetail(@RequestParam("bussiness")String bussiness , @RequestParam("yearMonth")String yearMonth){
        return detailService.getAllDetail(bussiness , yearMonth);
    }

}
