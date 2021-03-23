package com.cx.kpi2.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcelUtilTwo {

    private static final Logger log= LoggerFactory.getLogger(ExcelUtilTwo.class);
    private static final String dateFormat="yyyy-MM-dd hh:mm:ss";

    private static final SimpleDateFormat simpleDateFormat=new SimpleDateFormat(dateFormat);

    /**
     * excel sheet填充数据
     * @param dataList
     * @param wb
     * @param headers
     * @param sheetName
     */
    public static void fillExcelSheetData(List<Map<Integer, Object>> dataList, Workbook wb, String[] headers, String sheetName){
        Sheet sheet=wb.createSheet(sheetName);

        //TODO：创建sheet的第一行数据-即excel的头部信息
        Row headerRow=sheet.createRow(0);
        for(int i=0;i<headers.length;i++){
            headerRow.createCell(i).setCellValue(headers[i]);
        }

//        CellRangeAddress region1 = new CellRangeAddress(1, 11, 0, 0);
//        sheet.addMergedRegion(region1);
        int start = 1;
        for(int i = 0 ; i < 15 ; i++){
            int last = start+10;
            CellRangeAddress region = new CellRangeAddress(start, last, 0, 0);
            sheet.addMergedRegion(region);

            CellRangeAddress region2 = new CellRangeAddress(start, last, 14, 14);
            sheet.addMergedRegion(region2);

            CellRangeAddress region3 = new CellRangeAddress(start, last, 15, 15);
            sheet.addMergedRegion(region3);

            start = last+1;
        }

        //TODO：从第二行开始塞入真正的数据列表
        int rowIndex=1;
        Row row;
        Object obj;
        for(Map<Integer, Object> rowMap:dataList){
            try {
                row=sheet.createRow(rowIndex++);
                for(int i=0;i<headers.length;i++){
                    obj=rowMap.get(i);
                    if (obj==null) {
                        row.createCell(i).setCellValue("");
                    }else if (obj instanceof Date) {
                        String tempDate=simpleDateFormat.format((Date)obj);
                        row.createCell(i).setCellValue((tempDate==null)?"":tempDate);
                    }else {
                        row.createCell(i).setCellValue(String.valueOf(obj));
                    }
                }
            } catch (Exception e) {
                log.debug("excel sheet填充数据 发生异常： ",e.fillInStackTrace());
            }
        }

    }

}
