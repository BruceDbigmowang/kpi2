package com.cx.kpi2.controller;

import com.cx.kpi2.pojo.*;
import com.cx.kpi2.service.*;
import com.cx.kpi2.util.BubbleSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class GroupKPIController {
    @Autowired
    PresidentService presidentService;
    @Autowired
    GroupKPIService groupKPIService;
    @Autowired
    LeaderService leaderService;
    @Autowired
    GroupLeaderService groupLeaderService;
    @Autowired
    BussinessLeaderService bussinessLeaderService;
    @Autowired
    GroupScoreService groupScoreService;
    @Autowired
    GroupRecordService groupRecordService;

    @RequestMapping("/showGroupRegard")
    public List<GroupKPI> showAllRegard(HttpSession session){
        People user = (People)session.getAttribute("user");
        String account = user.getAccount();
        List<GroupKPI> groupKPIList = new ArrayList<>();
        /**
         * 根据账号，对四个角色进行逐一验证
         * 根据账号所属角色显示所有数据
         */
        //判断是否是总裁
        if(presidentService.isPresident(account)){
            List<GroupKPI> groupKPIS = groupKPIService.findByLevel("S");
            for(int i = 0 ; i < groupKPIS.size() ; i++){
                GroupKPI groupKPI = groupKPIS.get(i);
                if(!groupKPIList.contains(groupKPI)){
                    groupKPIList.add(groupKPI);
                };
            };
        };
        //判断是否是分管副总
        if(leaderService.isLeader(account)){
            String dept = leaderService.getDept(account);
            List<GroupKPI> groupKPIS = groupKPIService.findByLevelAndDept("S",dept);
            for(int i = 0 ; i < groupKPIS.size() ; i++){
                GroupKPI groupKPI = groupKPIS.get(i);
                if(!groupKPIList.contains(groupKPI)){
                    groupKPIList.add(groupKPI);
                };
            };
        };
        //判断是否是集团副总
        if(groupLeaderService.isGroupLeader(account)){
            List<GroupKPI> groupKPIS = groupKPIService.findByLevel("A");
            for(int i = 0 ; i < groupKPIS.size() ; i++){
                GroupKPI groupKPI = groupKPIS.get(i);
                if(!groupKPIList.contains(groupKPI)){
                    groupKPIList.add(groupKPI);
                };
            };
        };
        //判断是否是事业部负责人
        if(bussinessLeaderService.isBussinessLeader(account)){
            List<GroupKPI> groupKPIS = groupKPIService.findByLevel("B");
            for(int i = 0 ; i < groupKPIS.size() ; i++){
                GroupKPI groupKPI = groupKPIS.get(i);
                if(!groupKPIList.contains(groupKPI)){
                    groupKPIList.add(groupKPI);
                };
            };
        };
        return groupKPIList;
    }

    @RequestMapping("/checkData")
    public String checkInputData(@RequestParam("kpis[]")int[] kpis , @RequestParam("scores[]")BigDecimal[] scores){
        String errorInfo = "";
        for(int i = 0 ; i < kpis.length ; i++){
            int kpiId = kpis[i];
            BigDecimal score = scores[i];
            int no = i+1;
            int scoreMax = groupKPIService.getOne(kpiId).getScore();
            if(score.compareTo(new BigDecimal(scoreMax)) > 0){
                errorInfo = "第"+no+"项输入的值大于最大分值";
                break;
            }
        }
        return errorInfo;
    }

    @RequestMapping("/writeGroupDeptRecord")
    public void saveRecord(@RequestParam("kpis[]")int[] kpis , @RequestParam("scores[]") BigDecimal[] scores ,@RequestParam("yearMonth")String yearMonth , HttpSession session){
        People user = (People)session.getAttribute("user");
        String account = user.getAccount();
        for(int i = 0 ; i < kpis.length ; i++){
            int kpiNo = kpis[i];
            BigDecimal score = scores[i];
            GroupKPI groupKPI = groupKPIService.getOne(kpiNo);
            String level = groupKPI.getLevel();
            String dept = groupKPI.getDept();
            if(level.equals("S")){
                /**
                 * 当level == S时
                 * 需要总裁和分管副总两个人打分
                 * 此时存在某个账号对应的人即是总裁又是某个部门的分管副总
                 * 需要逐一判断
                 */
                if(presidentService.isPresident(account)){
                    String name = presidentService.getName(account);
                    String scorer = "总裁："+name+"("+account+")";
                    GroupScore groupScore = new GroupScore();
                    groupScore.setDept(dept);
                    groupScore.setKpiNo(kpiNo);
                    groupScore.setScorer(scorer);
                    groupScore.setScore(score);
                    groupScore.setYearMonth(yearMonth);
                    groupScoreService.save(groupScore);
                };
                if(leaderService.isLeaderToDept(account , dept)){
                    String name = leaderService.getName(account);
                    String scorer = "分管副总："+name+"("+account+")";
                    GroupScore groupScore = new GroupScore();
                    groupScore.setDept(dept);
                    groupScore.setKpiNo(kpiNo);
                    groupScore.setScorer(scorer);
                    groupScore.setScore(score);
                    groupScore.setYearMonth(yearMonth);
                    groupScoreService.save(groupScore);
                }
            }else if(level.equals("A")){
                String name = groupLeaderService.getName(account);
                String scorer = "集团副总："+name+"("+account+")";
                GroupScore groupScore = new GroupScore();
                groupScore.setDept(dept);
                groupScore.setKpiNo(kpiNo);
                groupScore.setScorer(scorer);
                groupScore.setScore(score);
                groupScore.setYearMonth(yearMonth);
                groupScoreService.save(groupScore);
            }else if(level.equals("B")){
                String name = bussinessLeaderService.getName(account);
                String bussiness = bussinessLeaderService.getBussiness(account);
                String scorer = bussiness+"负责人："+name+"("+account+")";
                GroupScore groupScore = new GroupScore();
                groupScore.setDept(dept);
                groupScore.setKpiNo(kpiNo);
                groupScore.setScorer(scorer);
                groupScore.setScore(score);
                groupScore.setYearMonth(yearMonth);
                groupScoreService.save(groupScore);
            }
        }
        GroupRecord groupRecord = new GroupRecord();
        groupRecord.setAccount(account);
        groupRecord.setYearMonth(yearMonth);
        groupRecordService.save(groupRecord);
    }

    @RequestMapping("/completGroupTest")
    public boolean completOrNot(@RequestParam("yearMonth")String yearMonth , HttpSession session){
        People user = (People)session.getAttribute("user");
        String account = user.getAccount();
        boolean result = groupRecordService.completRegard(account , yearMonth);
        return result;
    }

    @RequestMapping("getAllRecordTime")
    public List<String> allRecordTime(){
        return groupRecordService.getAllRecordTime();
    }

    @RequestMapping("getAllRecordYearMonth")
    public List<String> allTime(){
        return groupRecordService.getAllRecordYearMOnth();
    }

    @RequestMapping("/getAverageScore")
    public List<List<BigDecimal>> getAllAverage(@RequestParam("yearMonth")String yearMonth){
        return groupScoreService.getAnnualAverage(yearMonth);
    }

    @RequestMapping("/getAverageScoreMonth")
    public List<BigDecimal> getAllAverageMonth(@RequestParam("yearMonth")String yearMonth){
        return groupScoreService.getAverage(yearMonth);
    }

    @RequestMapping("/getGroupTestDetail")
    public List<GroupScore> getAllDetails(@RequestParam("dept")String dept ,@RequestParam("yearMonth")String yearMonth){
        System.out.println("++++"+yearMonth);
        System.out.println(dept+"++++");
        /**
         * 查询出某部门所有的考核明细
         */
        List<GroupScore> scoreList = new ArrayList<>();
        List<GroupKPI> groupKPIList = groupKPIService.getByDept(dept);
        /**
         * 根据各个考核明细的等级，在对应表中查询出评分人
         * 再根据评分人，时间，KPI的id，在表中找到相关数据，若未找到，设为0
         * 将这些信息放入list集合中返回给前台
         */
        for(int i = 0 ; i < groupKPIList.size() ; i++){
            GroupKPI groupKPI = groupKPIList.get(i);
            int kpiNo = groupKPI.getId();
            String level = groupKPI.getLevel();
            if(level.equals("S")){
                /**
                 * 当考核等级为S时
                 * 需要查询总裁与分管副总的评分
                 * 查询到则正常显示
                 * 若未查询到，设置为0
                 */
                String presidentAccount = presidentService.findPresident();
                System.out.println(presidentAccount);
                String presidentName = presidentService.getName(presidentAccount);
                String scorer = "总裁："+presidentName+"("+presidentAccount+")";
                GroupScore score1 = groupScoreService.findBykpiNoAndScorerAndYearMonth(kpiNo , scorer , yearMonth);
                if(score1 != null){
                    scoreList.add(score1);
                }else{
                    GroupScore newGroupScore = new GroupScore();
                    newGroupScore.setScorer(scorer);
                    newGroupScore.setScore(new BigDecimal(0));
                    scoreList.add(newGroupScore);
                }
                //查询分管副总的评分
                String leaderAccount = leaderService.findByDept(dept);
                String leaderName = leaderService.getName(leaderAccount);
                String scorer2 = "分管副总："+leaderName+"("+leaderAccount+")";
                GroupScore score2 = groupScoreService.findBykpiNoAndScorerAndYearMonth(kpiNo , scorer2 , yearMonth);
                if(score2 != null){
                    scoreList.add(score2);
                }else{
                    GroupScore newGroupScore = new GroupScore();
                    newGroupScore.setScorer(scorer2);
                    newGroupScore.setScore(new BigDecimal(0));
                    scoreList.add(newGroupScore);
                }

            }else if(level.equals("A")){
                /**
                 * 等级为A的，集团所有副总均要打分
                 */
                List<GroupLeader> groupLeaderList = groupLeaderService.getAll();
                for(int j = 0 ; j < groupLeaderList.size() ; j++){
                    GroupLeader groupLeader = groupLeaderList.get(j);
                    String groupLeaderAccount = groupLeader.getAccount();
                    String groupLeaderName = groupLeader.getName();
                    String scorer = "集团副总："+groupLeaderName+"("+groupLeaderAccount+")";
                    GroupScore score = groupScoreService.findBykpiNoAndScorerAndYearMonth(kpiNo , scorer , yearMonth);
                    if(score != null){
                        scoreList.add(score);
                    }else{
                        GroupScore newGroupScore = new GroupScore();
                        newGroupScore.setScorer(scorer);
                        newGroupScore.setScore(new BigDecimal(0));
                        scoreList.add(newGroupScore);
                    }
                }
            }else if(level.equals("B")){
                /**
                 * 等级为B的，所有事业部的负责人均要打分
                 */

                List<BussinessLeader> bussinessLeaderList = bussinessLeaderService.findAll();
                for(int j = 0 ; j < bussinessLeaderList.size() ; j++){
                    BussinessLeader bussinessLeader = bussinessLeaderList.get(j);
                    String bussinessLeaderAccount = bussinessLeader.getAccount();
                    String bussinessLeaderName = bussinessLeader.getName();
                    String bussiness = bussinessLeader.getBussiness();
                    String scorer = bussiness+"负责人："+bussinessLeaderName+"("+bussinessLeaderAccount+")";
                    GroupScore score = groupScoreService.findBykpiNoAndScorerAndYearMonth(kpiNo , scorer , yearMonth);
                    if(score != null){
                        scoreList.add(score);
                    }else{
                        GroupScore newGroupScore = new GroupScore();
                        newGroupScore.setScorer(scorer);
                        newGroupScore.setScore(new BigDecimal(0));
                        scoreList.add(newGroupScore);
                    }
                }

            }

        }

        return scoreList;
    }

    @RequestMapping("/getGroupKPIByDept")
    public List<GroupKPI> getByDept(@RequestParam("dept")String dept){
        return groupKPIService.getByDept(dept);
    }

    @RequestMapping("/getGroupRegarIdentity")
    public boolean getIdentity(HttpSession session){
        People user = (People)session.getAttribute("user");
        String account = user.getAccount();
        boolean president = presidentService.isPresident(account);
        boolean leader = leaderService.isLeader(account);
        boolean groupLeader = groupLeaderService.isGroupLeader(account);
        boolean bussinessLeader = bussinessLeaderService.isBussinessLeader(account);
        if(president == false && leader == false && groupLeader == false && bussinessLeader == false){
            return false;
        }else{
            return true;
        }
    }

    @RequestMapping("/checkCompleteSubmit")
    public String chaeckSubmitData(@RequestParam("scorer")String scorer , @RequestParam("score")BigDecimal score , @RequestParam("yearMonth")String yearMonth){
        boolean result = groupScoreService.completeOrNot(scorer , score , yearMonth);
        if(result == true){
            return "该项考核已完成";
        }else{
            return "该项考核未完成";
        }
    }

    @RequestMapping("/getRank")
    public List<String> rank(@RequestParam("totals[]")BigDecimal[] totals){
        List<BigDecimal> newTotal = new ArrayList<>();
        for(int i = 0 ; i <totals.length ; i++){
            newTotal.add(totals[i]);
        }

        BigDecimal current;
        for (int i = 0; i < totals.length - 1; i++) {
            current = totals[i + 1];
            int preIndex = i;
            while (preIndex >= 0 && totals[preIndex].compareTo(current)==-1) {
                totals[preIndex + 1] = totals[preIndex];
                preIndex--;
            }
            totals[preIndex + 1] = current;
        }

        List<String> ranks = new ArrayList<>();
//        System.out.println(totals[0]+"_"+totals[1]+"_"+totals[2]+"_"+totals[3]+"_"+totals[4]+"_"+totals[5]+"_"+totals[6]+"_"+totals[7]+"_"+totals[8]+"_"+totals[9]);
        for(int i = 0 ; i < newTotal.size() ; i++){
            BigDecimal total = newTotal.get(i);
            int position = 0;
            for (int j = 0; j < totals.length; j++) {
                if (totals[j].compareTo(total)== 0) {
                    position = j+1;
                    break;
                }
            }
            ranks.add("第"+position+"名");
//            System.out.println("第"+position+"名");
        }

        return ranks;
    }

}
