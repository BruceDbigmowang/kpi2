package com.cx.kpi2.service;

import com.cx.kpi2.dao.DeptDAO;
import com.cx.kpi2.dao.GroupKPIDAO;
import com.cx.kpi2.dao.GroupScoreDAO;
import com.cx.kpi2.pojo.Dept;
import com.cx.kpi2.pojo.GroupKPI;
import com.cx.kpi2.pojo.GroupScore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class GroupScoreService {
    @Autowired
    GroupScoreDAO groupScoreDAO;
    @Autowired
    DeptDAO deptDAO;
    @Autowired
    GroupKPIDAO groupKPIDAO;

    public void save(GroupScore groupScore){
        groupScoreDAO.save(groupScore);
    }

    public List<List<BigDecimal>> getAnnualAverage(String year){
        List<List<BigDecimal>> annualAverage = new ArrayList<>();

        List<Dept> deptList = deptDAO.findAll();
        for(int i = 0 ; i < deptList.size() ; i++){
            String dept = deptList.get(i).getDeptName();
            List<BigDecimal> deptAverage = new ArrayList<>();

            for(int j = 1 ; j < 13 ; j++){
                String yearMonth = year+"_"+j;

                BigDecimal allAverage = new BigDecimal(0);
                List<GroupKPI> groupKPIList = groupKPIDAO.findByDept(dept);
                for( int s = 0 ; s < groupKPIList.size() ; s++){

                    GroupKPI groupKPI = groupKPIList.get(s);
                    int kpiNo = groupKPI.getId();
                    String level = groupKPI.getLevel();
                    /**
                     * 根据考核项查询出相关考核项的总分
                     */
                    if(level.equals("S")){
                        /**
                         * 若考核等级为S 则需要对总裁跟分管副总两个人给的分数求均值
                         */
                        List<GroupScore> groupScoreList = groupScoreDAO.findByKpiNoAndYearMonth(kpiNo , yearMonth);
                        BigDecimal total = new BigDecimal(0);
                        BigDecimal avergae = new BigDecimal(0);
                        if(groupScoreList != null && groupScoreList.size() != 0){
                            for(int w = 0 ; w < groupScoreList.size() ; w++){
                                total = total.add(groupScoreList.get(w).getScore());
                            }
                            avergae = total.divide(new BigDecimal(2));
                        }
                        allAverage = allAverage.add(avergae);
                    }else if(level.equals("A")){
                        /**
                         * 若考核等级为S 则需要对总裁跟分管副总两个人给的分数求均值
                         */
                        List<GroupScore> groupScoreList = groupScoreDAO.findByKpiNoAndYearMonth(kpiNo , yearMonth);
                        BigDecimal total = new BigDecimal(0);
                        BigDecimal avergae = new BigDecimal(0);
                        if(groupScoreList != null && groupScoreList.size() != 0){
                            for(int w = 0 ; w < groupScoreList.size() ; w++){
                                total = total.add(groupScoreList.get(w).getScore());
                            }
                            avergae = total.divide(new BigDecimal(11), 2, BigDecimal.ROUND_HALF_UP);
                        }
                        allAverage = allAverage.add(avergae);
                    }else if(level.equals("B")){
                        /**
                         * 若考核等级为S 则需要对总裁跟分管副总两个人给的分数求均值
                         */
                        List<GroupScore> groupScoreList = groupScoreDAO.findByKpiNoAndYearMonth(kpiNo , yearMonth);
                        BigDecimal total = new BigDecimal(0);
                        BigDecimal avergae = new BigDecimal(0);
                        if(groupScoreList != null && groupScoreList.size() != 0){
                            for(int w = 0 ; w < groupScoreList.size() ; w++){
                                total = total.add(groupScoreList.get(w).getScore());
                            }
                            avergae = total.divide(new BigDecimal(15), 2, BigDecimal.ROUND_HALF_UP);
                        }
                        allAverage = allAverage.add(avergae);
                    }

                }

                deptAverage.add(allAverage);
            }
            annualAverage.add(deptAverage);
        }
        return annualAverage;
    }

    public GroupScore findBykpiNoAndScorerAndYearMonth(int kpiNo , String scorer , String yearMonth){
        List<GroupScore> groupScoreList = groupScoreDAO.findByKpiNoAndScorerAndYearMonth(kpiNo , scorer , yearMonth);
        if(groupScoreList != null && groupScoreList.size() != 0){
            return groupScoreList.get(0);
        }else{
            return null;
        }
    }

    public boolean completeOrNot(String scorer , BigDecimal score , String yearMonth){
        List<GroupScore> groupScoreList = groupScoreDAO.findByScorerAndScoreAndYearMonth(scorer , score , yearMonth);
        if(groupScoreList == null || groupScoreList.size() == 0){
            return false;
        }else{
            return true;
        }
    }

    public List<BigDecimal> getAverage(String yearMonth){

        List<BigDecimal> averageList = new ArrayList<>();
        List<Dept> deptList = deptDAO.findAll();
        /**
         * 查询出所有部门
         */
        for(int i = 0 ; i < deptList.size() ; i++){
            String dept = deptList.get(i).getDeptName();
            List<GroupKPI> groupKPIList = groupKPIDAO.findByDept(dept);
            /**
             * 根据部门查询出所有的考核项
             */
            BigDecimal allAverage = new BigDecimal(0);
            for(int j = 0 ; j < groupKPIList.size() ; j++){
                GroupKPI groupKPI = groupKPIList.get(j);
                int kpiNo = groupKPI.getId();
                String level = groupKPI.getLevel();
                /**
                 * 根据考核项查询出相关考核项的总分
                 */
                if(level.equals("S")){
                    /**
                     * 若考核等级为S 则需要对总裁跟分管副总两个人给的分数求均值
                     */
                    List<GroupScore> groupScoreList = groupScoreDAO.findByKpiNoAndYearMonth(kpiNo , yearMonth);
                    BigDecimal total = new BigDecimal(0);
                    BigDecimal avergae = new BigDecimal(0);
                    if(groupScoreList != null && groupScoreList.size() != 0){
                        for(int w = 0 ; w < groupScoreList.size() ; w++){
                            total = total.add(groupScoreList.get(w).getScore());
                        }
                        avergae = total.divide(new BigDecimal(2));
                    }
                    allAverage = allAverage.add(avergae);
                }else if(level.equals("A")){
                    /**
                     * 若考核等级为S 则需要对总裁跟分管副总两个人给的分数求均值
                     */
                    List<GroupScore> groupScoreList = groupScoreDAO.findByKpiNoAndYearMonth(kpiNo , yearMonth);
                    BigDecimal total = new BigDecimal(0);
                    BigDecimal avergae = new BigDecimal(0);
                    if(groupScoreList != null && groupScoreList.size() != 0){
                        for(int w = 0 ; w < groupScoreList.size() ; w++){
                            total = total.add(groupScoreList.get(w).getScore());
                        }
                        avergae = total.divide(new BigDecimal(11),2, BigDecimal.ROUND_HALF_UP);
                    }
                    allAverage = allAverage.add(avergae);
                }else if(level.equals("B")){
                    /**
                     * 若考核等级为S 则需要对总裁跟分管副总两个人给的分数求均值
                     */
                    List<GroupScore> groupScoreList = groupScoreDAO.findByKpiNoAndYearMonth(kpiNo , yearMonth);
                    BigDecimal total = new BigDecimal(0);
                    BigDecimal avergae = new BigDecimal(0);
                    if(groupScoreList != null && groupScoreList.size() != 0){
                        for(int w = 0 ; w < groupScoreList.size() ; w++){
                            total = total.add(groupScoreList.get(w).getScore());
                        }
                        avergae = total.divide(new BigDecimal(15),2, BigDecimal.ROUND_HALF_UP);
                    }
                    allAverage = allAverage.add(avergae);
                }
            }
            averageList.add(allAverage);

        }
        return averageList;
    }

}
