package com.cx.kpi2.controller;

import com.cx.kpi2.pojo.Bussiness;

import com.cx.kpi2.pojo.DeptScore;
import com.cx.kpi2.pojo.DeptTotalScore;
import com.cx.kpi2.service.*;
import com.cx.kpi2.util.ExcelUtil;
import com.cx.kpi2.util.ExcelUtilThree;
import com.cx.kpi2.util.ExcelUtilTwo;
import com.cx.kpi2.util.WebUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class ExcelController {
    @Autowired
    DeptService deptService;
    @Autowired
    BussinessService bussinessService;
    @Autowired
    DeptTotalScoreService deptTotalScoreService;
    @Autowired
    RecordService recordService;
    @Autowired
    DeptScoreService deptScoreService;
    @Autowired
    WeightService weightService;

    /**
     * 将每个月的考核结果打包成Excel文件下载下来
     */
    @RequestMapping("/downloadMonthly")
    public void downloadByMonth(@RequestParam("yearMonth") String yearMonth , HttpServletResponse response) throws Exception {
        List<String> deptList = deptService.getAllDept();
        List<Bussiness> bussinessList = bussinessService.getAllBussiness();
        List<List<BigDecimal>> allScore = new ArrayList<>();
        List<BigDecimal> summary = deptTotalScoreService.getTotalScore(yearMonth);
        BigDecimal[] totals = new BigDecimal[summary.size()];
        summary.toArray(totals);

        //根据事业部当月得分对事业部进行排名
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


        for(int i = 0 ; i < bussinessList.size() ; i++){
            String bussinessName = bussinessList.get(i).getBussiness();
            List<DeptTotalScore> deptTotalScoreList = deptTotalScoreService.getAllDeptByBussinessAndYearMonth(bussinessName , yearMonth);
            List<BigDecimal> oneBussinessTotalScore = new ArrayList<>();

            for(int j = 0 ; j < deptTotalScoreList.size() ; j++){
                BigDecimal score = deptTotalScoreList.get(j).getScore();

                oneBussinessTotalScore.add(score);
            }
            allScore.add(oneBussinessTotalScore);

        }

        String[] headers=new String[13];
        headers[0] = "事业部";
        int target = 1;
        for(int i = 0 ; i <deptList.size() ; i++){
            headers[target] = "集团"+deptList.get(i);
            target = target+1;
        }
        headers[11] = "合计";
        headers[12] = "排名";

        List<Map<Integer, Object>> dataList= ExcelUtil.manageProductList(allScore , bussinessList , summary , ranks);
        Workbook wb=new HSSFWorkbook();
        String sheetProductName = yearMonth+"_所有事业部月度报表.xls";
        String excelProductName = yearMonth+"_所有事业部月度报表.xls";
        ExcelUtilThree.fillExcelSheetData(dataList, wb, headers, sheetProductName);
        WebUtil.downloadExcel(response, wb, excelProductName);
    }

    /**
     * 将某一年的所有考核结果打包成Excel文件下载下来
     */
    @RequestMapping("/downloadYearly")
    public void downloadByYear(@RequestParam("year")String year , HttpServletResponse response) throws Exception {
        List<List<Object>> all = new ArrayList<>();
        List<Bussiness> bussinessList = bussinessService.getAllBussiness();
        List<String> deptList = deptService.getAllDept();
        List<BigDecimal> totalScoreYearly = new ArrayList<>();
        for(int i = 0 ; i < bussinessList.size() ; i++){
            String bussinessName = bussinessList.get(i).getBussiness();

            List<Object> summary = new ArrayList<>();
            summary.add(bussinessName);
            summary.add("合计");
            for(int month = 1 ; month < 13 ; month++){
                String yearMonth = year+"_"+month;
                List<DeptScore> allScore = deptScoreService.getAllDept(bussinessName , yearMonth);
                BigDecimal total = new BigDecimal(0);
                if(allScore!=null && allScore.size()!=0){
                    for(int j = 0 ; j < allScore.size() ; j++){
                        if(allScore.get(j).getScore().compareTo(BigDecimal.ZERO) == 0){
                            if(recordService.occurBig(allScore.get(j).getBussiness() , allScore.get(j).getDept() , allScore.get(j).getYearMonth()) == true){
                                total = BigDecimal.ZERO;
                                break;
                            }
                        }else{
                            String dept = allScore.get(j).getDept();
                            BigDecimal weight = weightService.getByBussinessAndDept(bussinessName , dept).getWeight();
                            total = total.add(allScore.get(j).getScore().multiply(weight));
                        }

                    }
                }
                total = total.setScale(2 , BigDecimal.ROUND_HALF_UP);
                summary.add(total);
            }
            BigDecimal yearTotal = new BigDecimal(0);
            for(int j=2 ; j <summary.size() ; j++){
                if(summary.get(j) != null){
                    yearTotal = yearTotal.add(new BigDecimal(String.valueOf(summary.get(j))));
                }
            }
            summary.add(yearTotal);
            totalScoreYearly.add(yearTotal);

            for (int j = 0 ; j < deptList.size() ; j++){
                String deptName = deptList.get(j);
                List<Object> oneDept = new ArrayList<>();
                oneDept.add(bussinessName);
                oneDept.add("集团"+deptName);
                for(int month = 1 ; month < 13 ; month++){
                    String yearMonth = year+"_"+month;
                    BigDecimal score = deptTotalScoreService.findByBussinessAndDeptAndYearMonth(bussinessName , deptName , yearMonth);
                    oneDept.add(score);
                }
                oneDept.add(yearTotal);
                all.add(oneDept);
            }

            all.add(summary);
        }

        BigDecimal[] totals = new BigDecimal[totalScoreYearly.size()];
        totalScoreYearly.toArray(totals);

        //根据事业部当月得分对事业部进行排名
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
        }

        int tar = 0;
        for(int i = 0 ; i < bussinessList.size() ; i++){

            for(int j = 0 ; j < 11 ; j++){
                List<Object> targetList = all.get(tar);
                targetList.add(ranks.get(i));
                tar = tar+1;
            }
        }

        String[] headers=new String[16];
        headers[0] = "事业部";
        headers[1] = "部门";
        int target = 2;
        for(int i = 1 ; i <13 ; i++){
            headers[target] = i+"月";
            target = target+1;
        }
        headers[14] = "总分";
        headers[15] = "排名";
        List<Map<Integer, Object>> dataList= ExcelUtil.manageDataYearly(all);
        Workbook wb=new HSSFWorkbook();
        String sheetProductName = year+"_所有事业部年度报表.xls";
        String excelProductName = year+"_所有事业部年度报表.xls";
        ExcelUtilTwo.fillExcelSheetData(dataList, wb, headers, sheetProductName);
        WebUtil.downloadExcel(response, wb, excelProductName);

    }
}
