package com.bjpowernode.crm.commons.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;

/**
 * @Author 临渊
 * @Date 2022-08-12 21:07
 */

/**
 *  关于excel操作的工具类
 */
public class HSSFUtils {

    /**
     *  从指定的HSSFCell对象中获取列的值
     * @return
     */
    public static String getCellValueForStr(HSSFCell cell) {
        String rets = "";
        if (cell.getCellType()==HSSFCell.CELL_TYPE_STRING){
            rets = cell.getStringCellValue();
        } else if (cell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC) {
            rets = String.valueOf(cell.getNumericCellValue());
        } else if (cell.getCellType()==HSSFCell.CELL_TYPE_BOOLEAN) {
            rets = String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellType()==HSSFCell.CELL_TYPE_FORMULA) {
            rets = cell.getCellFormula();
        } else {
            rets = "";
        }
        return rets;
    }
}
