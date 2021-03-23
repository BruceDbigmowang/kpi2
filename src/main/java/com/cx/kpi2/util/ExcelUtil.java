package com.cx.kpi2.util;

import com.cx.kpi2.pojo.Bussiness;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtil {

    public static List<Map<Integer, Object>> manageProductList(final List<List<BigDecimal>> scoreList , final List<Bussiness> bussinessList , final List<BigDecimal> totalScore , final List<String> ranks){
        List<Map<Integer, Object>> dataList=new ArrayList<>();
        if (scoreList!=null && scoreList.size()>0) {
            int length=scoreList.size();

            Map<Integer, Object> dataMap;
            for (int i = 0; i < length; i++) {
                List<BigDecimal> bean=scoreList.get(i);

                dataMap=new HashMap<>();
                dataMap.put(0, bussinessList.get(i).getBussiness());
                int target = 1;
                for(int j = 0 ; j < bean.size() ; j++){
                    dataMap.put(target , bean.get(j));
                    target = target + 1;
                }
                dataMap.put(target , totalScore.get(i));
                dataMap.put(target+1 , ranks.get(i));
                dataList.add(dataMap);
            }
        }
        return dataList;
    }

    public static List<Map<Integer, Object>> manageDataYearly(final List<List<Object>> scoreList){
        List<Map<Integer, Object>> dataList=new ArrayList<>();
        if (scoreList!=null && scoreList.size()>0) {
            int length=scoreList.size();

            Map<Integer, Object> dataMap;
            for (int i = 0; i < length; i++) {
               List<Object> bean = scoreList.get(i);

                dataMap=new HashMap<>();

                for(int j = 0 ; j < bean.size() ; j++){
                    dataMap.put(j , bean.get(j));
                }

                dataList.add(dataMap);
            }
        }
        return dataList;
    }

}
