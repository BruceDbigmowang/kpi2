package com.cx.kpi2.service;

import com.cx.kpi2.dao.*;
import com.cx.kpi2.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.*;

@Service
public class ScoreService {
    @Autowired
    BussinessDivisionDAO divisionDAO;
    @Autowired
    BussinessWeightDAO weightDAO;
    @Autowired
    CheckDetailDAO detailDAO;
    @Autowired
    AccountDAO accountDAO;
    @Autowired
    KpiRecordDAO kpiRecordDAO;
    @Autowired
    DeptSumDAO deptSumDAO;
    @Autowired
    DeptLastDAO deptLastDAO;
    @Autowired
    EventDAO eventDAO;
    @Autowired
    CompleteDAO completeDAO;

    /**
     * session中存储的只有账号与密码
     * 所以还要再根据账号查询部门信息
     */
    public Account findDeptInfo(String account){
        Account user = accountDAO.findById(account).get();
        return user;
    }

    /**
     * 进入到打分界面
     * 首先先判断该账号是否有权限进入该界面
     */
    public String enterPage(String account){
        Account accountList = accountDAO.getByAccount(account);
        if(accountList == null){
            return "没有权限";
        }else{
            return "OK";
        }
    }

    /**
     * 根据部门筛选出需要打分的事业部
     * 若某一部门需要对某一事业部进行打分
     * 那么该部门在该事业部的权重占比不为“0”（特例除外）
     *
     * 特例：
     * 信息部与审计部门因为都是扣分项，分值计算不同
     * 若需要打分  则对所有事业部进行打分
     */
    public List<BussinessWeight> loadBussiness(Account user){
        String dept = user.getDept();
        if(dept.equals("信息") || dept.equals("审计") || dept.equals("人事")){
            List<BussinessWeight> bussinessWeights = weightDAO.findByDept(dept);
            return bussinessWeights;
        }else{
            List<BigDecimal> weights = new ArrayList<>();
            weights.add(BigDecimal.ZERO);
            List<BussinessWeight> bussinessWeights = weightDAO.findByDeptAndWeightNotIn(dept , weights);
            return bussinessWeights;
        }
    }

    /**
     * 根据部门 事业部来加载考核明细
     * 其中，财务、供应链针对不同的事业部有不同的考核标准
     * 所以 这两个部门需要根据部门+类别进行查询考核明细
     * 其他部门 直接根据部门查询
     */
    public List<CheckDetail> getAllDetail(Account user , String bussiness){
        String dept = user.getDept();
        if(dept.equals("财务")){
            String type = divisionDAO.findByBussinessName(bussiness).get(0).getFtype();
            dept = dept + type;
            List<CheckDetail> detailList = detailDAO.findByDept(dept);
            return detailList;
        }else if(dept.equals("供应链")){
            String type = divisionDAO.findByBussinessName(bussiness).get(0).getGtype();
            dept = dept + type;
            List<CheckDetail> detailList = detailDAO.findByDept(dept);
            return detailList;
        }else if (dept.equals("人事")){
            String type = divisionDAO.findByBussinessName(bussiness).get(0).getHtype();
            dept = dept + type;
            List<CheckDetail> detailList = detailDAO.findByDept(dept);
            return detailList;
        }else{
            List<CheckDetail> detailList = detailDAO.findByDept(dept);
            return detailList;
        }
    }

    /**
     * *****************************
     * 记录分数有三张表：
     * 1、记录某一部门对某一事业部打分的明细
     * 2、记录某一部门对某一事业部打分的总和
     * 3、记录某一部门对某一事业部打分的总和 * 权重
     *
     * 此外还有一张表记录扣分与发生重大事项
     * 扣分与发生重大事项 会在最后的结果上进行处理
     * *****************************
     *
     * 某个部门对某一事业部进行打分
     * 1、将各个数据都记录保存下来
     * 2、计算部门总得分
     * 3、计算部门总得分 * 权重
     */
    @Transactional
    public String writeScoreData(String bussiness , String dept , int[] kpiNos , int[] scores){
        if(kpiNos.length != scores.length){
            return "程序发生错误，评分丢失";
        }
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);//获取年份
        int month=cal.get(Calendar.MONTH);//获取月份
        String yearMonth = "";
        if(month == 0){
            year = year - 1;
            yearMonth = year+"_"+12;
        }else{
            yearMonth = year+"_"+month;
        }

        List<Complete> hasCompleted = completeDAO.findByBussinessAndDeptAndYearMonth(bussiness , dept , yearMonth);
        if(hasCompleted.size() != 0){
            return bussiness+"已经完成打分";
        }

        /**
         * 将各个数据都记录保存下来
         */
        List<KpiRecord> recordList = new ArrayList<>();
        int total = 0;
        for(int i = 0 ; i < kpiNos.length ; i++){
            CheckDetail detail = detailDAO.findById(kpiNos[i]).get();
            KpiRecord record = new KpiRecord();
            record.setBussiness(bussiness);
            record.setDept(dept);
            record.setKpiNo(kpiNos[i]);
            record.setScore(scores[i]);
            record.setYearMonth(yearMonth);
            record.setCheckLevel(detail.getCheckLevel());
            if(detail.getCheckLevel().equals("A") || detail.getCheckLevel().equals("B")){
                total = total + scores[i];
            }
            recordList.add(record);
        }

        /**
         * 遍历所有保存下来的数据
         * 查询数据 看是否发生重大事项
         */
        List<Event> events = new ArrayList<>();
        for(int i = 0 ; i < recordList.size() ; i++){
            KpiRecord record = recordList.get(i);
            if(record.getCheckLevel().equals("S") && record.getScore() == 0){
                Event event = new Event();
                event.setBussiness(bussiness);
                event.setDept(dept);
                event.setCheckLevel("S");
                event.setYearMonth(yearMonth);
                events.add(event);
                break;
            }
        }
        List<DeptSum> sumList = new ArrayList<>();
        if(events.size() != 0){
            DeptSum sum = new DeptSum();
            sum.setBussiness(bussiness);
            sum.setDept(dept);
            sum.setYearMonth(yearMonth);
            sum.setScore(0);
            sumList.add(sum);
        }else{
            /**
             * 若存在扣分项  记录到数据库中
             */
            for(int i = 0 ; i < recordList.size() ; i++){
                KpiRecord record = recordList.get(i);
                if(record.getCheckLevel().equals("C") && record.getScore() != 20){
                    Event event = new Event();
                    event.setBussiness(bussiness);
                    event.setDept(dept);
                    event.setCheckLevel("C");
                    event.setYearMonth(yearMonth);
                    event.setDeduct(20 - record.getScore());
                    events.add(event);
                    break;
                }
            }

            DeptSum sum = new DeptSum();
            sum.setBussiness(bussiness);
            sum.setDept(dept);
            sum.setYearMonth(yearMonth);
            sum.setScore(total);
            sumList.add(sum);
        }
        DeptLast last = new DeptLast();
        BigDecimal weight = weightDAO.findByBussinessAndDept(bussiness, dept).get(0).getWeight();
        last.setBussiness(bussiness);
        last.setDept(dept);
        last.setYearMonth(yearMonth);
        if(sumList.get(0).getScore() == 0){
            last.setScore(BigDecimal.ZERO);
        }else{
            BigDecimal lastScore = weight.multiply(new BigDecimal(sumList.get(0).getScore())).setScale(2 , BigDecimal.ROUND_HALF_UP);
            last.setScore(lastScore);
        }

        Complete ck = new Complete();
        ck.setBussiness(bussiness);
        ck.setDept(dept);
        ck.setYearMonth(yearMonth);

        try{
            for(KpiRecord kpiRecord : recordList){
                kpiRecordDAO.save(kpiRecord);
            }
            for(DeptSum sum : sumList){
                deptSumDAO.save(sum);
            }
            for(Event event : events){
                eventDAO.save(event);
            }
            deptLastDAO.save(last);
            completeDAO.save(ck);
        }catch (Exception e){
            return e.getMessage();
        }

        return "OK";
    }

    /**
     * 判断是否存在重大事项
     */
    public boolean hasImportantEvent(List<Event> eventList){
        for(Event event : eventList){
            if(event.getCheckLevel().equals("S")){
                return true;
            }
        }
        return false;
    }

    /**
     * 先封装表头 “事业部” + 各个部门 + “总分”
     * 获取所有的事业部
     * 循环{
     *     获取事业部；
     *     获取所有部门；
     *     定义总分
     *     循环{
     *         获取部门；
     *         根据事业部 部门查询数据，若未查询到数据  则为0，查询到了  则为具体数据
     *         总分累加；
     *     }
     *     查询是否有重大事项或扣分项；
     *     若存在重大事项，总分为 0；
     *     若不存在重大事项但存在扣分项 ， 总分=总分 - 所有扣分之和；
     *     若不存在重大事项  且 不存在扣分项  总分不变
     *
     * }
     */
    public Map<String , Object> loadAllScore(String yearMonth){
        Map<String , Object> map = new HashMap<>();
        /**
         * 封装表头
         *
         * “事业部” + 各个部门 + “总分”
         */
        List<String> thead = new ArrayList<>();
        thead.add("事业部");
        List<String> deptList = new ArrayList<>();
        List<Account> accountList = accountDAO.findAll();
        for(Account account : accountList){
            String deptName = account.getDept();
            if(deptList.contains(deptName)){

            }else{
                deptList.add(deptName);
            }
        }
        for(String dept : deptList){
            thead.add(dept);
        }
        thead.add("扣分");
        thead.add("总分");
        thead.add("排名");
        map.put("thead" , thead);
        //---------------表头结束-------------------------



        /**
         * 封装表体
         * 每一行数据：具体某一事业部 + 各个部门的总分*权重 + 总分
         */
        List<List<Object>> tbody = new ArrayList<>();
        List<BussinessDivision> bussinessList = divisionDAO.findAll();
        System.out.println("所有事业部有"+bussinessList.size());
        for(BussinessDivision bussinessDivision : bussinessList){
            List<Object> tr = new ArrayList<>();
            String bussinessName = bussinessDivision.getBussinessName();
            tr.add(bussinessName);
            BigDecimal totalScore = BigDecimal.ZERO;
            /**
             * 获取某一事业部下  各个部门得分的明细
             */
            for(String dept : deptList){
                List<DeptLast> deptLasts = deptLastDAO.findByBussinessAndDeptAndYearMonth(bussinessName , dept , yearMonth);
                if(deptLasts.size() == 0){
                    tr.add(BigDecimal.ZERO);
                }else{
                    BigDecimal getScore = deptLasts.get(0).getScore();
                    tr.add(getScore);
                    totalScore = totalScore.add(getScore);
                }
            }
            List<Event> eventList1 = eventDAO.findByBussinessAndCheckLevelAndYearMonth(bussinessName , "C" , yearMonth);
            String div = "";
            for(int i = 0 ; i < eventList1.size() ; i++){
                if(i == 0){
                    div = div+eventList1.get(i).getDept()+"&nbsp;-"+eventList1.get(i).getDeduct();
                }else{
                    div = div+"<br>"+eventList1.get(i).getDept()+"&nbsp;-"+eventList1.get(i).getDeduct();
                }
            }
            tr.add(div);

            /**
             * 计算某一事业部的总分
             * 1、Event表中没有该部分当月记录，即：无重大事项 无扣分项
             * 2、存在重大事项，则当月得分为 0
             * 3、无重大事项但存在扣分项，总分上扣除一定的分数
             */
            List<Event> eventList = eventDAO.findByBussinessAndYearMonth(bussinessName , yearMonth);
            if(eventList.size() == 0){
                tr.add(totalScore);
            }else if(hasImportantEvent(eventList) == true){
                tr.add(BigDecimal.ZERO);
            }else{
                for(Event event : eventList){
                    totalScore = totalScore.subtract(new BigDecimal(event.getDeduct()));
                }
                tr.add(totalScore);
            }
            tbody.add(tr);
        }

        //将表体所有数据根据总分来排序
        List<List<Object>> tb = new ArrayList<>();
        for(int j = 0 ; j < 13 ; j++){
            List<Object> tr = new ArrayList<>();
            for(int i = 0 ; i < tbody.size() ; i++){
                if(i == 0){
                    tr = tbody.get(0);
                }else{
                    BigDecimal tr1 = (BigDecimal)tr.get(12);
                    BigDecimal tr2 = (BigDecimal)tbody.get(i).get(12);
                    if(tr1.compareTo(tr2) == -1){
                        tr = tbody.get(i);
                    }
                }

                if(i == tbody.size() - 1){
                    tbody.remove(tr);
                    tb.add(tr);
                }
            }
        }
        int start = 1;
        for(int i = 0 ; i < tb.size() ; i++){
            if(i == 0){
                tb.get(i).add(start);
            }else{
                BigDecimal more = (BigDecimal) tb.get(i - 1).get(12);
                BigDecimal less = (BigDecimal)tb.get(i).get(12);
                if(more.compareTo(less) == 0){
                    tb.get(i).add(start);
                }else{
                    start = start + 1;
                    tb.get(i).add(start);
                }
            }
        }
        //———————————排序结束—————————————
        String bestBussiness = "";
        for(int i = 0 ; i < tb.size() ; i++){
            int range = (Integer)tb.get(i).get(13);
            BigDecimal total = (BigDecimal)tb.get(i).get(12);
            if( range == 1 && total.compareTo(BigDecimal.ZERO) != 0){
                if("".equals(bestBussiness)){
                    bestBussiness = bestBussiness+String.valueOf(tb.get(i).get(0));
                }else{
                    bestBussiness = bestBussiness+"、"+String.valueOf(tb.get(i).get(0));
                }
            }
        }
        System.out.println("明星事业部"+bestBussiness);
        map.put("tbody" , tb);
        map.put("bestBussiness" , bestBussiness);
        //___________________表体结束______________________

        return map;
    }

    /**
     * 新版KPI考核评分
     * 加载所有考核时间
     * @return
     */
    public List<String> loadAllRecordTime(){
        List<String> allTime = new ArrayList<>();
        List<Complete> completeList = completeDAO.findAll();
        for(Complete complete : completeList){
            if(!allTime.contains(complete.getYearMonth())){
                allTime.add(complete.getYearMonth());
            }
        }
        if(allTime.size() == 0){
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);//获取年份
            int month=cal.get(Calendar.MONTH);//获取月份
            String yearMonth = "";
            if(month == 0){
                year = year - 1;
                yearMonth = year+"_"+12;
            }else{
                yearMonth = year+"_"+month;
            }
            allTime.add(yearMonth);
        }
        return allTime;
    }

    /**
     * 查询事业部得分明细
     * 1、事业部
     * 2、各个部门
     * 3、各个部门对该事业部打分的总和
     * 4、各个部门在该事业部中的考核权重
     * 5、
     */
    public Map<String , Object> loadAllScoreByBussiness(String bussiness , String yearMonth){
        Map<String , Object> map = new HashMap<>();
        /**
         * 查询所有的部门
         */
        List<String> deptList = new ArrayList<>();
        List<Account> accountList = accountDAO.findAll();
        for(Account account : accountList){
            String deptName = account.getDept();
            if(deptList.contains(deptName)){

            }else{
                deptList.add(deptName);
            }
        }
        map.put("bussiness" , bussiness);
        /**
         * 根据事业部 + 部门 查询 部门考核总分  权重  最后得分
         */
        BigDecimal totalScore = BigDecimal.ZERO;
        List<List<Object>> allData = new ArrayList<>();
        for(int i = 0 ; i < deptList.size() ; i++){
            List<Object> data = new ArrayList<>();
            String dept = deptList.get(i);
            data.add(dept);
            /**
             * 查询部门考核总分
             */
            List<DeptSum> deptSumList = deptSumDAO.findByBussinessAndDeptAndYearMonth(bussiness , dept , yearMonth);
            if(deptSumList.size() == 0){
                data.add(0);
            }else{
                data.add(deptSumList.get(0).getScore());
            }
            /**
             * 查询 权重
             */
            List<BussinessWeight> bwList = weightDAO.findByBussinessAndDept(bussiness , dept);
            System.out.println(bussiness);
            System.out.println(dept);
            data.add(bwList.get(0).getWeight());
            /**
             * 查询部门考核最后得分
             */
            List<DeptLast> deptLastList = deptLastDAO.findByBussinessAndDeptAndYearMonth(bussiness , dept , yearMonth);
            if(deptLastList.size() == 0){
                data.add(BigDecimal.ZERO);
            }else{
                data.add(deptLastList.get(0).getScore());
                totalScore = totalScore.add(deptLastList.get(0).getScore());
            }
            allData.add(data);
        }
        map.put("allData" , allData);
        map.put("total" , totalScore);

        return map;
    }

    /**
     * 根据事业部 部门 年月  查询打分明细
     */
    public Map<String , Object> findRegardDetail(String bussiness , String dept, String yearMonth){
        Map<String , Object> map = new HashMap<>();
        String deptAndType = "";
        if(dept.equals("财务")){
            String type = divisionDAO.findByBussinessName(bussiness).get(0).getFtype();
            deptAndType = dept + type;
        }else if(dept.equals("供应链")){
            String type = divisionDAO.findByBussinessName(bussiness).get(0).getGtype();
            deptAndType = dept + type;
        }else if (dept.equals("人事")){
            String type = divisionDAO.findByBussinessName(bussiness).get(0).getHtype();
            deptAndType = dept + type;
        }else{
            /**
             * 其余部门不用加工
             */
            deptAndType = dept;
        }
        List<CheckDetail> checkDetails = detailDAO.findByDept(deptAndType);
        map.put("details" , checkDetails);
        List<Integer> allResult = new ArrayList<>();
        for(int i = 0 ; i < checkDetails.size() ; i++){
            CheckDetail checkDetail = checkDetails.get(i);
            List<KpiRecord> kpiRecordList = kpiRecordDAO.findByBussinessAndDeptAndYearMonthAndKpiNo(bussiness , dept , yearMonth , checkDetail.getId());
            if(kpiRecordList.size() == 0){
                allResult.add(0);
            }else{
                allResult.add(kpiRecordList.get(0).getScore());
            }
        }
        map.put("results" , allResult);
        return map;
    }

    public Map<String  , Object> findAllDataByYear(String year){
        Map<String , Object> map = new HashMap<>();
        List<BussinessDivision> bussinessList = divisionDAO.findAll();
        /**
         * 用于存储所有事业部一年中所有数据
         */
        List<List<Object>> allDataLast = new ArrayList<>();
        /**
         * 用于存储各事业部年度得分总和
         */
        List<BigDecimal> allBussinessTotal = new ArrayList<>();
        for(int i = 0 ; i < bussinessList.size() ; i++){
            /**
             * 用于存储一个是事业部一年中的所有得分
             *
             * 一条数据表示该事业部某个月的分数
             *
             * 最后转换为：一条数据表示某一部门对该事业部一年的打分
             */
            List<List<Object>> allDataPrevious = new ArrayList<>();
            BussinessDivision bd = bussinessList.get(i);
            String bussiness = bd.getBussinessName();

            BigDecimal bussinessTotal = BigDecimal.ZERO;
            for(int j = 1 ; j < 13 ; j++){
                List<Object> dataList = new ArrayList<>();
                String yearMonth = year+"_"+j;
                List<String> deptList = new ArrayList<>();
                List<Account> accountList = accountDAO.findAll();
                for(Account account : accountList){
                    String deptName = account.getDept();
                    if(deptList.contains(deptName)){

                    }else{
                        deptList.add(deptName);
                    }
                }
                /**
                 * 循环 一个事业部 某一个月 所有的得分
                 */
                BigDecimal total = BigDecimal.ZERO;
                for(String dept : deptList){
                    List<DeptSum> deptSumList = deptSumDAO.findByBussinessAndDeptAndYearMonth(bussiness , dept , yearMonth);
                    if(deptSumList.size() == 0){
                        dataList.add(0);
                    }else{
                        dataList.add(deptSumList.get(0).getScore());
                        BigDecimal weight = weightDAO.findByBussinessAndDept(bussiness , dept).get(0).getWeight();
                        total = total.add(weight.multiply(new BigDecimal(deptSumList.get(0).getScore())));
                    }
                }
                List<Event> eventList1 = eventDAO.findByBussinessAndCheckLevelAndYearMonth(bussiness , "S" , yearMonth);
                if(eventList1.size() != 0){
                    total = BigDecimal.ZERO;
                }
                List<Event> eventList2 = eventDAO.findByBussinessAndCheckLevelAndYearMonth(bussiness , "C" , yearMonth);
                if(eventList2.size() != 0){
                    for(Event event : eventList2){
                        total = total.subtract(new BigDecimal(event.getDeduct()));
                    }
                }
                dataList.add(total);
                bussinessTotal = bussinessTotal.add(total);
                allDataPrevious.add(dataList);
            }
            allBussinessTotal.add(bussinessTotal);

            List<String> deptList = new ArrayList<>();
            List<Account> accountList = accountDAO.findAll();
            for(Account account : accountList){
                String deptName = account.getDept();
                if(deptList.contains(deptName)){

                }else{
                    deptList.add(deptName);
                }
            }

            for(int j = 0 ; j < 11 ; j++){
                if(j == 10){
                    List<Object> lastList = new ArrayList<>();
                    lastList.add(bussiness);
                    lastList.add("合计");
                    for(int s = 0 ; s < allDataPrevious.size() ; s++){
                        lastList.add(allDataPrevious.get(s).get(j));
                    }
                    lastList.add(bussinessTotal);
                    allDataLast.add(lastList);
                }else{
                    List<Object> lastList = new ArrayList<>();
                    lastList.add(bussiness);
                    lastList.add(deptList.get(j));
                    for(int s = 0 ; s < allDataPrevious.size() ; s++){
                        lastList.add(allDataPrevious.get(s).get(j));
                    }
                    lastList.add(bussinessTotal);
                    allDataLast.add(lastList);
                }
            }

        }
        System.out.println("参与排名的数据："+allBussinessTotal.size());
        map.put("result" , allDataLast);
        List<String> deptList = new ArrayList<>();
        List<Account> accountList = accountDAO.findAll();
        for(Account account : accountList){
            String deptName = account.getDept();
            if(deptList.contains(deptName)){

            }else{
                deptList.add(deptName);
            }
        }
        int obl = deptList.size()+1;
        map.put("obl" , obl);
        /**
         * 对list进行排序
         * 然后根据排序后的下标获取排名
         */
        List<BigDecimal> middleList = new ArrayList<>();
        for(int i = 0 ; i < allBussinessTotal.size() ; i++){
            BigDecimal no = allBussinessTotal.get(i);
            middleList.add(no);
        }
        List<BigDecimal> rangeList = new ArrayList<>();
        System.out.println(middleList.size());
        for(int s = 0 ; s < 13 ; s++){
            BigDecimal bigest = BigDecimal.ZERO;
            for(int i = 0 ; i < middleList.size() ; i++){
                BigDecimal middleNum = middleList.get(i);
                if(i == 0){
                    bigest = middleNum;
                }else{
                    if(bigest.compareTo(middleNum) == -1){
                        bigest = middleList.get(i);
                    }
                }
            }
            rangeList.add(bigest);
            middleList.remove(bigest);
        }

        List<String> ranges = new ArrayList<>();
        for (int a = 0 ; a < allBussinessTotal.size() ; a++){
            BigDecimal allYear = allBussinessTotal.get(a);
            System.out.println(allYear);
            int target = rangeList.indexOf(allYear) + 1;
            ranges.add("第"+target+"名");
            System.out.println("第"+target+"名");
        }
        map.put("ranges" , ranges);
        return map;
    }
}
