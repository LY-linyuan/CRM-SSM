package com.bjpowernode.crm.workbench.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

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
}
