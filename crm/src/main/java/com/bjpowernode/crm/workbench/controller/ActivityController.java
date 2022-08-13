package com.bjpowernode.crm.workbench.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.HSSFUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityRemarkService;
import com.bjpowernode.crm.workbench.service.ActivityService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

/**
 * @Author 临渊
 * @Date 2022-08-10 22:21
 */

@Controller
public class ActivityController {


    @Autowired
    UserService userService;
    @Autowired
    ActivityService activityService;
    @Autowired
    ActivityRemarkService activityRemarkService;


    @RequestMapping("/workbench/activity/index.do")
    public String index(HttpServletRequest request){
        List<User> userList = userService.queryAllUsers();
        request.setAttribute("userList", userList);
        return "workbench/activity/index";
    }


    @RequestMapping("/workbench/activity/saveCreateActivity.do")
    @ResponseBody
    public Object saveCreateActivity(Activity activity, HttpSession session) {
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        // 封装函数
        activity.setId(UUIDUtils.getUUID());
        activity.setCreateTime(DateUtils.formatTime(new Date()));
        activity.setCreateBy(user.getId());

        ReturnObject returnObject = new ReturnObject();
        try {
            // 插入语句是否报异常
            int ret = activityService.saveCreateActivity(activity);
            if (ret > 0) {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("当前系统繁忙, 请稍后再试");
            }
        } catch (Exception e) {
            e.printStackTrace();

            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("当前系统繁忙, 请稍后再试");
        }
        return returnObject;
    }


    @RequestMapping("/workbench/activity/queryActivityByConditionForPage.do")
    @ResponseBody
    public Object queryActivityByConditionForPage(String name, String owner, String startDate, String endDate,
                                                  int pageNo, int pageSize) {
        // 封装参数
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", name);
        map.put("owner", owner);
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        map.put("pageNo", (pageNo - 1) * pageSize);
        map.put("pageSize", pageSize);
        List<Activity> activityList = activityService.queryActivityByConditionForPage(map);
        int totalRows = activityService.queryCountOfActivityCondition(map);
        // 根据查询结果生成响应信息
        Map<String, Object> retMap = new HashMap<String, Object>();
        retMap.put("activityList", activityList);
        retMap.put("totalRows", totalRows);
        return retMap;
    }

    @RequestMapping("/workbench/activity/deleteActivityIds.do")
    @ResponseBody
    public Object deleteActivityIds(String[] id) {
        ReturnObject returnObject = new ReturnObject();
        // 调用service方法删除数据
        try {
            int ret = activityService.deleteActivityByIds(id);
            if (ret > 0) {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统繁忙, 请稍后重试..........");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统繁忙, 请稍后重试..........");
        }
        return returnObject;
    }


    @RequestMapping("/workbench/activity/queryActivityById.do")
    @ResponseBody
    public Object queryActivityById(String id) {
        return activityService.queryActivityById(id);
    }

    @RequestMapping("/workbench/activity/saveEditActivity.do")
    @ResponseBody Object saveEditActivity(Activity activity, HttpSession session) {
        ReturnObject returnObject = new ReturnObject();
        activity.setEditTime(DateUtils.formatTime(new Date()));
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        activity.setEditBy(user.getId());
        try {
            int ret = activityService.saveEditActivity(activity);
            if (ret == 1) {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统繁忙, 请稍后再试.......");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统繁忙, 请稍后再试.......");
        }
        return returnObject;
    }


    @RequestMapping("/workbench/activity/fileDownload.do")
    public void fileDownload(HttpServletResponse response) throws IOException {
        // 设置响应类型
        response.setContentType("application/octet-stream;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        // 可以设置响应头信息, 是浏览器接收到响应信息之后, 直接激活文件下载 即使能打开也不打开
        response.addHeader("Content-Disposition", "attachment;filename=myStudentList.xsl");
        // 读取excel文件(InputStream) 输出到浏览器(OutputStream)
        InputStream in = new FileInputStream("C:/programme/java/实战项目/CRM-SSM/crm-project/crm/src/test/java/com/bjpowernode/crm/poi/serverDir/studentList.xls");
        byte[] buff = new byte[256];
        int len = 0;
        while ((len = in.read(buff)) != -1) {
            out.write(buff);
        }
        in.close();
        out.close();
    }


    @RequestMapping("/workbench/activity/exportAllActivities.do")
    public void exportAllActivities(HttpServletResponse response) throws Exception {
        // 调用service方法查询所有市场活动
        List<Activity> activityList = activityService.queryAllActivities();
        // 创建excel文件 并把activityList写入到excel文件当中
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("市场活动列表");
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("ID");
        cell = row.createCell(1);
        cell.setCellValue("所有者");
        cell = row.createCell(2);
        cell.setCellValue("名称");
        cell = row.createCell(3);
        cell.setCellValue("开始日期");
        cell = row.createCell(4);
        cell.setCellValue("结束日期");
        cell = row.createCell(5);
        cell.setCellValue("成本");
        cell = row.createCell(6);
        cell.setCellValue("描述");
        cell = row.createCell(7);
        cell.setCellValue("创建时间");
        cell = row.createCell(8);
        cell.setCellValue("创建者");
        cell = row.createCell(9);
        cell.setCellValue("修改时间");
        cell = row.createCell(10);
        cell.setCellValue("修改者");

        // 遍历市场获取 创建HSSFRow对象生成错在数据行
        if (activityList != null && activityList.size() > 0) {
            for (int i = 0 ; i < activityList.toArray().length ; i++) {
                Activity activity = activityList.get(i);
                row = sheet.createRow(i + 1);
                cell = row.createCell(0);
                cell.setCellValue(activity.getId());
                cell = row.createCell(1);
                cell.setCellValue(activity.getOwner());
                cell = row.createCell(2);
                cell.setCellValue(activity.getName());
                cell = row.createCell(3);
                cell.setCellValue(activity.getStartDate());
                cell = row.createCell(4);
                cell.setCellValue(activity.getEndDate());
                cell = row.createCell(5);
                cell.setCellValue(activity.getCost());
                cell = row.createCell(6);
                cell.setCellValue(activity.getDescription());
                cell = row.createCell(7);
                cell.setCellValue(activity.getCreateTime());
                cell = row.createCell(8);
                cell.setCellValue(activity.getCreateBy());
                cell = row.createCell(9);
                cell.setCellValue(activity.getEditTime());
                cell = row.createCell(10);
                cell.setCellValue(activity.getEditBy());
            }
        }
        /*// 根据workbook对象生成excel对象
        OutputStream os = new FileOutputStream("C:/programme/java/实战项目/CRM-SSM/crm-project/crm/src/main/java/com/bjpowernode/crm/workbench/excel/activityList.xls");
        workbook.write(os);
        workbook.close();
        os.close();
        // 把生成的文excel文件下载到客户端
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Content-Disposition", "attachment;filename=activityList.xsl");
        OutputStream out = response.getOutputStream();
        InputStream in = new FileInputStream("C:/programme/java/实战项目/CRM-SSM/crm-project/crm/src/main/java/com/bjpowernode/crm/workbench/excel/activityList.xls");
        byte[] buff = new byte[256];
        int len = 0;
        while ((len = in.read(buff)) != -1) {
            out.write(buff);
        }
        in.close();
        out.close();*/


        // 根据workbook对象生成excel对象
        /*OutputStream os = new FileOutputStream("C:/programme/java/实战项目/CRM-SSM/crm-project/crm/src/main/java/com/bjpowernode/crm/workbench/excel/activityList.xls");
        workbook.write(os);
        workbook.close();
        os.close();*/
        // 把生成的文excel文件下载到客户端
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Content-Disposition", "attachment;filename=activityList.xsl");
        OutputStream out = response.getOutputStream();
        /*InputStream in = new FileInputStream("C:/programme/java/实战项目/CRM-SSM/crm-project/crm/src/main/java/com/bjpowernode/crm/workbench/excel/activityList.xls");
        byte[] buff = new byte[256];
        int len = 0;
        while ((len = in.read(buff)) != -1) {
            out.write(buff);
        }
        in.close();*/

        workbook.write(out);

        workbook.close();
        out.close();
    }


    /**
     *  配置springmvc的文件上传解析器
     * @param userName
     * @param myFile
     * @return
     */
    @RequestMapping("/workbench/activity/fileUpload.do")
    @ResponseBody
    public Object fillUpload(String userName, MultipartFile myFile) throws IOException {
        // 把文本数据打印到控制台
        System.out.println("userName" + userName);
        // 把文件在服务器指定的目录中生成一个同样的文件
        // 路径 必须手动创建好  文件如果不存在会自动创建
        File file = new File("C:/programme/java/实战项目/CRM-SSM/crm-project/crm/src/main/java/com/bjpowernode/crm/workbench/excel/aaa." + myFile.getOriginalFilename());
        myFile.transferTo(file);
        // 返回响应信息
        ReturnObject returnObject = new ReturnObject();
        returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        returnObject.setMessage("上传成功");
        return returnObject;
    }


    @RequestMapping("/workbench/activity/importActivity.do")
    @ResponseBody
    public Object importActivity(MultipartFile activityFile, HttpSession session, String userName) {
        System.out.println("userName = " + userName);
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        ReturnObject returnObject = new ReturnObject();
        try {
            /*// 把excel文件写到磁盘目录中
            String originalFilename = activityFile.getOriginalFilename();
            File file = new File("C:/programme/java/实战项目/CRM-SSM/crm-project/crm/src/main/java/com/bjpowernode/crm/workbench/excel2/", originalFilename); // 路径必须手动创建好，文件如果不存在，会自动创建
            activityFile.transferTo(file);

            // 解析excel文件，获取文件中的数据，并且封装成activityList
            // 根据excel文件生成HSSFWorkbook对象，封装了excel文件的所有信息
            InputStream in = new FileInputStream("C:/programme/java/实战项目/CRM-SSM/crm-project/crm/src/main/java/com/bjpowernode/crm/workbench/excel2/" + originalFilename);*/

            InputStream in = activityFile.getInputStream();

            HSSFWorkbook workbook = new HSSFWorkbook(in);
            // 根据wb获取HSSFSheet对象，封装了一页的所有信息
            HSSFSheet sheet = workbook.getSheetAt(0);   // 页的下标，下标从0开始，依次增加
            // 根据sheet获取HSSFRow对象，封装了一行的所有信息
            HSSFRow row = null;
            HSSFCell cell = null;
            Activity activity = null;
            List<Activity> activityList = new ArrayList<Activity>();
            for(int i = 1 ; i <= sheet.getLastRowNum() ; i++) { // sheet.getLastRowNum()：最后一行的下标
                row = sheet.getRow(i);// 行的下标，下标从0开始，依次增加
                activity = new Activity();
                activity.setId(UUIDUtils.getUUID());
                activity.setOwner(user.getId());
                activity.setCreateTime(DateUtils.formatDate(new Date()));
                activity.setCreateBy(user.getId());

                for(int j = 0 ; j < row.getLastCellNum() ; j++) { // row.getLastCellNum():最后一列的下标+1
                    // 根据row获取HSSFCell对象，封装了一列的所有信息
                    cell = row.getCell(j); // 列的下标，下标从0开始，依次增加

                    // 获取列中的数据
                    String cellValue = HSSFUtils.getCellValueForStr(cell);
                    if(j == 2) {
                         activity.setName(cellValue);
                    } else if(j == 3) {
                         activity.setStartDate(cellValue);
                    } else if(j == 4) {
                        activity.setEndDate(cellValue);
                    } else if (j == 5) {
                        activity.setCost(cellValue);
                    } else if (j == 6) {
                        activity.setDescription(cellValue);
                    }
                }

                // 每一行中所有列都封装完成之后，把activity保存到list中
                activityList.add(activity);
            }
            // 调用service层方法，保存市场活动
            int ret = activityService.saveCreateActivityByList(activityList);
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            returnObject.setRetData(ret);
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统繁忙，请稍后重试......");
        }

        return returnObject;
    }


    @RequestMapping("/workbench/activity/detailActivity.do")
    public String detailActivity(String id, HttpServletRequest request) {
        // 查询数据
         Activity activity = activityService.queryActivityForDetailById(id);
         List <ActivityRemark> activityRemarkList = activityRemarkService.queryActivityRemarkForDetailByActivityId(id);
         // 数据保存到request中
        request.setAttribute("activity", activity);
        request.setAttribute("activityRemarkList", activityRemarkList);
        // 请求转发
        return "workbench/activity/detail";
    }
}
