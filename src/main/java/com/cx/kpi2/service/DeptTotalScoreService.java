package com.cx.kpi2.service;

import com.cx.kpi2.dao.*;
import com.cx.kpi2.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class DeptTotalScoreService {
    @Autowired
    DeptTotalScoreDAO deptTotalScoreDAO;
    @Autowired
    DeptDAO deptDAO;
    @Autowired
    BussinessDAO bussinessDAO;
    @Autowired
    RecordDAO recordDAO;
    @Autowired
    OtherkpiDAO otherkpiDAO;
    @Autowired
    HrkpiDAO hrkpiDAO;
    //将将一个事业部所有数据放入到list中
    //再将所有事业部：多个list放入到一个新的list中
    //对应取出相应数据
    public List<List<BigDecimal>> getLastScore(String yearMonth){
        List<Bussiness> bussinessList = bussinessDAO.findAll();
        List<List<BigDecimal>> allBussiness = new ArrayList<>();
        for(int i = 0 ; i < bussinessList.size() ; i++){
            String bussiness = bussinessList.get(i).getBussiness();
            List<Dept> deptList = deptDAO.findAll();
            List<BigDecimal> oneBussiness = new ArrayList<>();
            for(int j = 0 ; j < deptList.size() ; j++){
                String dept = deptList.get(j).getDeptName();
                BigDecimal lasetScore = new BigDecimal(0);
                List<DeptTotalScore> deptTotalScoreList = deptTotalScoreDAO.getByBussinessAndDeptAndYearMonth(bussiness , dept , yearMonth);
                if(deptTotalScoreList.size() == 0){
                    lasetScore = lasetScore.add(new BigDecimal(0));
                }else{
                    lasetScore = lasetScore.add(deptTotalScoreList.get(0).getScore());
                }
                oneBussiness.add(lasetScore);

            }
            allBussiness.add(oneBussiness);
        }
        return allBussiness;
    }

    /**
     * 将一个事业部所有部门得分相加，求出合计总得分
     * 将所有事业部的总得分放入到list中，之后依次取出
     * @param yearMonth
     * @return
     */
    public List<BigDecimal> getTotalScore(String yearMonth){
        List<Bussiness> bussinessList = bussinessDAO.findAll();
        List<BigDecimal> allTotal = new ArrayList<>();
        for(int i = 0 ; i < bussinessList.size() ; i++) {
            String bussiness = bussinessList.get(i).getBussiness();
            //验证所有部门评分为0是否是因为发生重大事项
            //若是因为发生重大事项，合计为0
            //若没有发生则正常计算
            List<Record> recordList = recordDAO.getByBussinessAndScoreAndYearmonth(bussiness, 0, yearMonth);
            if (recordList.size() == 0) {
                List<Dept> deptList = deptDAO.findAll();
                BigDecimal oneBussinessTotal = new BigDecimal(0);
                for (int j = 0; j < deptList.size(); j++) {
                    String dept = deptList.get(j).getDeptName();
                    BigDecimal lasetScore = new BigDecimal(0);
                    List<DeptTotalScore> deptTotalScoreList = deptTotalScoreDAO.getByBussinessAndDeptAndYearMonth(bussiness, dept, yearMonth);
                    if (deptTotalScoreList.size() == 0) {
                        lasetScore = lasetScore.add(new BigDecimal(0));
                    } else {
                        lasetScore = lasetScore.add(deptTotalScoreList.get(0).getScore());
                    }
                    oneBussinessTotal = oneBussinessTotal.add(lasetScore);

                }
                allTotal.add(oneBussinessTotal);
            } else {


                boolean have = false;
                //判断是否存在重大事项
                for (int s = 0; s < recordList.size(); s++) {
                    String kpiDept = recordList.get(s).getDept();
                    int kpiNo = recordList.get(s).getKpiNo();
                    if (kpiDept.equals("人事")) {
                        String testLevel = hrkpiDAO.getOne(kpiNo).getTestLevel();
                        if (testLevel.equals("S")) {
                            have = true;
                            break;
                        }
                    } else {
                        List<Otherkpi> otherkpiList = otherkpiDAO.findById(kpiNo);
                        if (otherkpiList != null) {
                            String testLevel = otherkpiList.get(0).getTestLevel();
                            if (testLevel.equals("S")) {
                                have = true;
                                break;
                            }
                        }
                    }

                }
                if (have == true) {
                    allTotal.add(new BigDecimal(0));
                } else {
                    List<Dept> deptList = deptDAO.findAll();
                    BigDecimal oneBussinessTotal = new BigDecimal(0);
                    for (int j = 0; j < deptList.size(); j++) {
                        String dept = deptList.get(j).getDeptName();
                        BigDecimal lasetScore = new BigDecimal(0);
                        List<DeptTotalScore> deptTotalScoreList = deptTotalScoreDAO.getByBussinessAndDeptAndYearMonth(bussiness, dept, yearMonth);
                        if (deptTotalScoreList.size() == 0) {
                            lasetScore = lasetScore.add(new BigDecimal(0));
                        } else {
                            lasetScore = lasetScore.add(deptTotalScoreList.get(0).getScore());
                        }
                        oneBussinessTotal = oneBussinessTotal.add(lasetScore);

                    }
                    allTotal.add(oneBussinessTotal);
                }
            }
        }
            return allTotal;

    }

    public List<DeptTotalScore> getByBussinessAndYearMonth(String bussiness , String yearMonth){
        return deptTotalScoreDAO.getByBussinessAndYearMonth(bussiness , yearMonth);
    }
    //判断是否有值为0
    //若有值为0 则查看是否发生重大事项
    public List<DeptTotalScore> getAllDeptByBussinessAndYearMonth(String bussiness , String yearMonth){
        List<Dept> deptList = deptDAO.findAll();
        List<DeptTotalScore> deptTotalScoreList = new ArrayList<>();
        for(int i  = 0 ; i < deptList.size();i++){
            String dept = deptList.get(i).getDeptName();
            List<DeptTotalScore> deptTotalScores = deptTotalScoreDAO.getByBussinessAndDeptAndYearMonth(bussiness , dept , yearMonth);
            if(deptTotalScores.size() == 0){
                DeptTotalScore deptTotalScore = new DeptTotalScore();
                deptTotalScore.setScore(new BigDecimal(0));
                deptTotalScoreList.add(deptTotalScore);
            }else{
                deptTotalScoreList.add(deptTotalScores.get(0));
            }
        }
        return deptTotalScoreList;

    }

    public String getGoodBussiness(String yearMonth){

        List<Bussiness> bussinessList = bussinessDAO.findAll();
        List<BigDecimal> allTotal = new ArrayList<>();
        for(int i = 0 ; i < bussinessList.size() ; i++) {
            String bussiness = bussinessList.get(i).getBussiness();
            //验证所有部门评分为0是否是因为发生重大事项
            //若是因为发生重大事项，合计为0
            //若没有发生则正常计算
            List<Record> recordList = recordDAO.getByBussinessAndScoreAndYearmonth(bussiness, 0, yearMonth);
            if (recordList.size() == 0) {
                List<Dept> deptList = deptDAO.findAll();
                BigDecimal oneBussinessTotal = new BigDecimal(0);
                for (int j = 0; j < deptList.size(); j++) {
                    String dept = deptList.get(j).getDeptName();
                    BigDecimal lasetScore = new BigDecimal(0);
                    List<DeptTotalScore> deptTotalScoreList = deptTotalScoreDAO.getByBussinessAndDeptAndYearMonth(bussiness, dept, yearMonth);
                    if (deptTotalScoreList.size() == 0) {
                        lasetScore = lasetScore.add(new BigDecimal(0));
                    } else {
                        lasetScore = lasetScore.add(deptTotalScoreList.get(0).getScore());
                    }
                    oneBussinessTotal = oneBussinessTotal.add(lasetScore);

                }
                allTotal.add(oneBussinessTotal);
            } else {


                boolean have = false;
                //判断是否存在重大事项
                for (int s = 0; s < recordList.size(); s++) {
                    String kpiDept = recordList.get(s).getDept();
                    int kpiNo = recordList.get(s).getKpiNo();

                    System.out.println(kpiDept);
                    System.out.println(kpiNo);

                    if (kpiDept.equals("人事")) {
                        String testLevel = hrkpiDAO.getOne(kpiNo).getTestLevel();
                        if (testLevel.equals("S")) {
                            have = true;
                            break;
                        }
                    } else {
                        List<Otherkpi> otherkpiList = otherkpiDAO.findById(kpiNo);
                        if(otherkpiList != null){
                            String testLevel = otherkpiList.get(0).getTestLevel();
                            if (testLevel.equals("S")) {
                                have = true;
                                break;
                            }

                        }
                    }

                }
                if (have == true) {
                    allTotal.add(new BigDecimal(0));
                } else {
                    List<Dept> deptList = deptDAO.findAll();
                    BigDecimal oneBussinessTotal = new BigDecimal(0);
                    for (int j = 0; j < deptList.size(); j++) {
                        String dept = deptList.get(j).getDeptName();
                        BigDecimal lasetScore = new BigDecimal(0);
                        List<DeptTotalScore> deptTotalScoreList = deptTotalScoreDAO.getByBussinessAndDeptAndYearMonth(bussiness, dept, yearMonth);
                        if (deptTotalScoreList.size() == 0) {
                            lasetScore = lasetScore.add(new BigDecimal(0));
                        } else {
                            lasetScore = lasetScore.add(deptTotalScoreList.get(0).getScore());
                        }
                        oneBussinessTotal = oneBussinessTotal.add(lasetScore);

                    }
                    allTotal.add(oneBussinessTotal);
                }
            }
        }
        int maxId = 0;
        List<Integer> idList = new ArrayList<>();
        BigDecimal max = new BigDecimal(0);
        for(int s = 0 ; s < allTotal.size() ; s++){
            BigDecimal oneTotal = allTotal.get(s);
            if(max.compareTo(oneTotal)<0){
                max = oneTotal;
                maxId = s;
            }
        }
        idList.add(maxId);
        for(int s = 0 ; s < allTotal.size() ; s++){
            BigDecimal oneTotal = allTotal.get(s);
            if(max.compareTo(oneTotal)==0){
               if(maxId != s){
                   idList.add(s);
               }
            }
        }
        String goodBussiness = "";
        for(int s = 0 ; s < idList.size() ; s++){
            if(s==0){
                goodBussiness = goodBussiness+bussinessList.get(idList.get(s)).getBussiness();
            }else{
                goodBussiness = goodBussiness+","+bussinessList.get(idList.get(s)).getBussiness();
            }
        }
        return goodBussiness;
    }
}
