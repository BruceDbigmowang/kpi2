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
import java.math.RoundingMode;
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
    @Autowired
    BussinessService bussinessService;
    @Autowired
    DeptService deptService;
    @Autowired
    WeightService weightService;

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

    /**
     * 根据事业部，部门，时间来搜索对应的数据
     * 先遍历事业部
     * 接着遍历部门
     * 再遍历时间
     * 算平均分的时候，如果每遇到一个未打分的月份，均分分母（12 - 1）
     * 将某事业部下某部门一年的分数及均分放入到一个list中
     * 放入所有数据的List中
     * 再将一年的分数及平均数*权重放入到另一个list中
     * 下一个部门重复将数据放入到list中并放入所有数据的list中
     * 在上一个权重得分的list中逐个加上新的部门分数*权重
     * 部门遍历结束后，将总分list放入到总数据的list中
     * 事业部遍历结束后，所有数据存放完成
     * @param year
     * @return
     */
    @RequestMapping("/showAnnualData")
    public List<List<BigDecimal>> getAllData(@RequestParam("year")String year){
        List<List<BigDecimal>> list1 = new ArrayList<>();//用于存放所有事业部所有部门的一年的评分加总分
        List<Bussiness> bussinessList = bussinessService.getAllBussiness();
        for(int i = 0 ; i < bussinessList.size() ; i++){
            String bussinessName = bussinessList.get(i).getBussiness();

            List<List<BigDecimal>> list2 = new ArrayList<>();//用于存放某事业部所有部门的一年的评分
            List<String> deptList = deptService.getAllDept();
            List<BigDecimal> list4 = new ArrayList<>();
            for(int j = 0 ; j < deptList.size() ; j++){
                String deptName = deptList.get(j);
                List<BigDecimal> list3 = new ArrayList<>();//用于记录某事业部某部分一年的得分

                for(int s = 1 ; s < 13 ; s++){
                    String yearMonth = year+"_"+s;
                    //首先判断某事业部某部门在某年某月是否有评分
                    boolean regard = completKpiService.submitedRegard(bussinessName , deptName , yearMonth);
                    if(regard == false){
                        list3.add(new BigDecimal(0));
                    }else{
                        BigDecimal score = deptScoreService.getAMonth(bussinessName , deptName , yearMonth).get(0).getScore();
                        list3.add(score);
                    }
                }
                list2.add(list3);

            }
            /**
             * 将某事业部所有部门的所有得分传入到总得分中
             */
            for(int w = 0 ; w < list2.size() ; w++){
                List<BigDecimal> list = list2.get(w);
                list1.add(list);
            }

            //1月
            BigDecimal one = new BigDecimal(0);
            for(int a = 0 ; a < list2.size() ; a++){

                String deptName = deptList.get(a);
                String yearMonth = year+"_"+1;
                boolean regard = completKpiService.submitedRegard(bussinessName , deptName , yearMonth);
                if( regard == true){
                    BigDecimal weight = weightService.getByBussinessAndDept(bussinessName , deptName).getWeight();
                    if(list2.get(a).get(0).compareTo(BigDecimal.ZERO) != 0){
                        BigDecimal newScore = list2.get(a).get(0).multiply(weight);
                        one = one.add(newScore);
                    }else{
                        boolean happendInport = deptScoreService.happendImport(bussinessName , deptName , yearMonth);
                        if(happendInport == true){
                            one = new BigDecimal(0);
                            break;
                        }
                    }
                }

            }
            list4.add(one);
            //2月
            BigDecimal two = new BigDecimal(0);
            for(int a = 0 ; a < list2.size() ; a++){

                String deptName = deptList.get(a);
                String yearMonth = year+"_"+2;
                boolean regard = completKpiService.submitedRegard(bussinessName , deptName , yearMonth);
                if( regard == true){
                    BigDecimal weight = weightService.getByBussinessAndDept(bussinessName , deptName).getWeight();

//                    BigDecimal newScore = list2.get(a).get(1).multiply(weight);
//                    two = two.add(newScore);

                    if(list2.get(a).get(1).compareTo(BigDecimal.ZERO) != 0){
                        BigDecimal newScore = list2.get(a).get(1).multiply(weight);
                        two = two.add(newScore);
                    }else{
                        boolean happendInport = deptScoreService.happendImport(bussinessName , deptName , yearMonth);
                        if(happendInport == true){
                            two = new BigDecimal(0);
                            break;
                        }

                    }
                }

            }
            list4.add(two);

            //3月
            BigDecimal three = new BigDecimal(0);
            for(int a = 0 ; a < list2.size() ; a++){

                String deptName = deptList.get(a);
                String yearMonth = year+"_"+3;
                boolean regard = completKpiService.submitedRegard(bussinessName , deptName , yearMonth);
                if( regard == true){
                    BigDecimal weight = weightService.getByBussinessAndDept(bussinessName , deptName).getWeight();
                    if(list2.get(a).get(2).compareTo(BigDecimal.ZERO) != 0){
                        BigDecimal newScore = list2.get(a).get(2).multiply(weight);
                        three = three.add(newScore);
                    }else{
                        boolean happendInport = deptScoreService.happendImport(bussinessName , deptName , yearMonth);
                        if(happendInport == true){
                            three = new BigDecimal(0);
                            break;
                        }
                    }
                }

            }
            list4.add(three);

            //4月
            BigDecimal four = new BigDecimal(0);
            for(int a = 0 ; a < list2.size() ; a++){

                String deptName = deptList.get(a);
                String yearMonth = year+"_"+4;
                boolean regard = completKpiService.submitedRegard(bussinessName , deptName , yearMonth);
                if( regard == true){
                    BigDecimal weight = weightService.getByBussinessAndDept(bussinessName , deptName).getWeight();
                    if(list2.get(a).get(3).compareTo(BigDecimal.ZERO) != 0){
                        BigDecimal newScore = list2.get(a).get(3).multiply(weight);
                        four = four.add(newScore);
                    }else{
                        boolean happendInport = deptScoreService.happendImport(bussinessName , deptName , yearMonth);
                        if(happendInport == true){
                            four = new BigDecimal(0);
                            break;
                        }
                    }
                }

            }
            list4.add(four);

            //5月
            BigDecimal five = new BigDecimal(0);
            for(int a = 0 ; a < list2.size() ; a++){

                String deptName = deptList.get(a);
                String yearMonth = year+"_"+5;
                boolean regard = completKpiService.submitedRegard(bussinessName , deptName , yearMonth);
                if( regard == true){
                    BigDecimal weight = weightService.getByBussinessAndDept(bussinessName , deptName).getWeight();
                    if(list2.get(a).get(4).compareTo(BigDecimal.ZERO) != 0){
                        BigDecimal newScore = list2.get(a).get(4).multiply(weight);
                        five = five.add(newScore);
                    }else{
                        boolean happendInport = deptScoreService.happendImport(bussinessName , deptName , yearMonth);
                        if(happendInport == true){
                            five = new BigDecimal(0);
                            break;
                        }
                    }
                }

            }
            list4.add(five);

            //6月
            BigDecimal six = new BigDecimal(0);
            for(int a = 0 ; a < list2.size() ; a++){

                String deptName = deptList.get(a);
                String yearMonth = year+"_"+6;
                boolean regard = completKpiService.submitedRegard(bussinessName , deptName , yearMonth);
                if( regard == true){
                    BigDecimal weight = weightService.getByBussinessAndDept(bussinessName , deptName).getWeight();
                    if(list2.get(a).get(5).compareTo(BigDecimal.ZERO) != 0){
                        BigDecimal newScore = list2.get(a).get(5).multiply(weight);
                        six = six.add(newScore);
                    }else{
                        boolean happendInport = deptScoreService.happendImport(bussinessName , deptName , yearMonth);
                        if(happendInport == true){
                            six = new BigDecimal(0);
                            break;
                        }
                    }
                }

            }
            list4.add(six);

            //7月
            BigDecimal seven = new BigDecimal(0);
            for(int a = 0 ; a < list2.size() ; a++){

                String deptName = deptList.get(a);
                String yearMonth = year+"_"+7;
                boolean regard = completKpiService.submitedRegard(bussinessName , deptName , yearMonth);
                if( regard == true){
                    BigDecimal weight = weightService.getByBussinessAndDept(bussinessName , deptName).getWeight();
                    if(list2.get(a).get(6).compareTo(BigDecimal.ZERO) != 0){
                        BigDecimal newScore = list2.get(a).get(6).multiply(weight);
                        seven = seven.add(newScore);
                    }else{
                        boolean happendInport = deptScoreService.happendImport(bussinessName , deptName , yearMonth);
                        if(happendInport == true){
                            seven = new BigDecimal(0);
                            break;
                        }
                    }
                }

            }
            list4.add(seven);

            //8月
            BigDecimal eight = new BigDecimal(0);
            for(int a = 0 ; a < list2.size() ; a++){

                String deptName = deptList.get(a);
                String yearMonth = year+"_"+8;
                boolean regard = completKpiService.submitedRegard(bussinessName , deptName , yearMonth);
                if( regard == true){
                    BigDecimal weight = weightService.getByBussinessAndDept(bussinessName , deptName).getWeight();
                    if(list2.get(a).get(7).compareTo(BigDecimal.ZERO) != 0){
                        BigDecimal newScore = list2.get(a).get(7).multiply(weight);
                        eight = eight.add(newScore);
                    }else{
                        boolean happendInport = deptScoreService.happendImport(bussinessName , deptName , yearMonth);
                        if(happendInport == true){
                            eight = new BigDecimal(0);
                            break;
                        }
                    }
                }

            }
            list4.add(eight);

            //9月
            BigDecimal nine = new BigDecimal(0);
            for(int a = 0 ; a < list2.size() ; a++){

                String deptName = deptList.get(a);
                String yearMonth = year+"_"+9;
                boolean regard = completKpiService.submitedRegard(bussinessName , deptName , yearMonth);
                if( regard == true){
                    BigDecimal weight = weightService.getByBussinessAndDept(bussinessName , deptName).getWeight();
                    if(list2.get(a).get(8).compareTo(BigDecimal.ZERO) != 0){
                        BigDecimal newScore = list2.get(a).get(8).multiply(weight);
                        nine = nine.add(newScore);
                    }else{
                        boolean happendInport = deptScoreService.happendImport(bussinessName , deptName , yearMonth);
                        if(happendInport == true){
                            nine = new BigDecimal(0);
                            break;
                        }
                    }
                }

            }
            list4.add(nine);

            //10月
            BigDecimal ten = new BigDecimal(0);
            for(int a = 0 ; a < list2.size() ; a++){

                String deptName = deptList.get(a);
                String yearMonth = year+"_"+10;
                boolean regard = completKpiService.submitedRegard(bussinessName , deptName , yearMonth);
                if( regard == true){
                    BigDecimal weight = weightService.getByBussinessAndDept(bussinessName , deptName).getWeight();
                    if(list2.get(a).get(9).compareTo(BigDecimal.ZERO) != 0){
                        BigDecimal newScore = list2.get(a).get(9).multiply(weight);
                        ten = ten.add(newScore);
                    }else{
                        boolean happendInport = deptScoreService.happendImport(bussinessName , deptName , yearMonth);
                        if(happendInport == true){
                            ten = new BigDecimal(0);
                            break;
                        }
                    }
                }

            }
            list4.add(ten);

            //11月
            BigDecimal eleven = new BigDecimal(0);
            for(int a = 0 ; a < list2.size() ; a++){

                String deptName = deptList.get(a);
                String yearMonth = year+"_"+11;
                boolean regard = completKpiService.submitedRegard(bussinessName , deptName , yearMonth);
                if( regard == true){
                    BigDecimal weight = weightService.getByBussinessAndDept(bussinessName , deptName).getWeight();
                    if(list2.get(a).get(10).compareTo(BigDecimal.ZERO) != 0){
                        BigDecimal newScore = list2.get(a).get(10).multiply(weight);
                        eleven = eleven.add(newScore);
                    }else{
                        boolean happendInport = deptScoreService.happendImport(bussinessName , deptName , yearMonth);
                        if(happendInport == true){
                            eleven = new BigDecimal(0);
                            break;
                        }
                    }
                }

            }
            list4.add(eleven);

            //12月
            BigDecimal twelve = new BigDecimal(0);
            for(int a = 0 ; a < list2.size() ; a++){

                String deptName = deptList.get(a);
                String yearMonth = year+"_"+12;
                boolean regard = completKpiService.submitedRegard(bussinessName , deptName , yearMonth);
                if( regard == true){
                    BigDecimal weight = weightService.getByBussinessAndDept(bussinessName , deptName).getWeight();
                    if(list2.get(a).get(11).compareTo(BigDecimal.ZERO) != 0){
                        BigDecimal newScore = list2.get(a).get(11).multiply(weight);
                        twelve = twelve.add(newScore);
                    }else{
                        boolean happendInport = deptScoreService.happendImport(bussinessName , deptName , yearMonth);
                        if(happendInport == true){
                            twelve = new BigDecimal(0);
                            break;
                        }
                    }
                }

            }
            list4.add(twelve);

            list1.add(list4);

        }

        return list1;
    }

}
