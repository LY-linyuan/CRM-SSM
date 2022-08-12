package com.bjpowernode.crm.poi;

/**
 * @Author 临渊
 * @Date 2022-08-12 20:28
 */

import com.bjpowernode.crm.commons.utils.HSSFUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * 演示apache-poi
 */
public class ParseExcelTest {
    public static void main(String[] args) throws Exception {
        InputStream in = new FileInputStream("C:/programme/java/实战项目/CRM-SSM/crm-project/crm/src/test/java/com/bjpowernode/crm/poi/serverDir/studentList.xls");
        HSSFWorkbook workbook = new HSSFWorkbook(in);
        // 根据workbook 获取HSSFSheet对象 封装了一页的所有信息
        HSSFSheet sheet = workbook.getSheetAt(0);  // 页的下标 从零开始
        // 根据sheet 获取HSSFRow对象 封装了一行的所有信息
        HSSFRow row = null;
        HSSFCell cell = null;
        for (int i = 0 ; i <= sheet.getLastRowNum() ; i++) {  // 最后一行下标
            row = sheet.getRow(i);  // 行的下标 从零开始一次递增
            // 根据row 获取HSSFCell对象 封装了一列的所有信息
            for (int j = 0 ; j < row.getLastCellNum() ; j++) { // 最后一列下标
                cell = row.getCell(j);// 列的下标 从零开始  一次递增
                // 获取列中的数据
                System.out.print(HSSFUtils.getCellValueForStr(cell) + "   ");
            }
            System.out.println("");
        }
    }

}
