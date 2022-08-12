package com.bjpowernode.crm.poi;

/**
 * @Author 临渊
 * @Date 2022-08-12 11:49
 */

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *  使用apache-poi商城excel文件
 */
public class CreatExcelTest {
    public static void main(String[] args) throws IOException {
        // 创建HSSWorkbook 对应一个excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 使用wb创建SSFSheet对象, 对于wb文件的一页
        HSSFSheet sheet = wb.createSheet("学生列表");
        // 使用sheet创建HSSFRow对象, 对应 sheet中的一行
        HSSFRow row = sheet.createRow(0);// 行号, 从0开始, 依次增加
        // 使用row创建HSSFCell对象 对应row中的列号
        HSSFCell cell = row.createCell(0);// 列的编号 从 0 开始 依次增加
        cell.setCellValue("学号");
        cell = row.createCell(1);
        cell.setCellValue("姓名");
        cell = row.createCell(2);
        cell.setCellValue("年龄");

        // 生成HSSFCellStyle对象
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);

        for (int i = 1; i < 11 ; i++) {
            row = sheet.createRow(i);
            cell = row.createCell(0);
            cell.setCellValue(100 + i);
            cell = row.createCell(1);
            cell.setCellValue(i + "姓名");
            cell = row.createCell(2);
            cell.setCellStyle(style);
            cell.setCellValue(i + "年龄");
        }
        // 调用工具函数生成excel文件
        FileOutputStream os = new FileOutputStream("C:/programme/java/实战项目/CRM-SSM/crm-project/crm/src/test/java/com/bjpowernode/crm/poi/serverDir/studentList.xls");
        wb.write(os);
        os.close();
        wb.close();
        System.out.println("=======执行成功======");

    }
}
