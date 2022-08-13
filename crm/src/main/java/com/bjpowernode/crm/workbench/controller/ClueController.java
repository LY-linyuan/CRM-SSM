package com.bjpowernode.crm.workbench.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.DicValueService;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.ClueRemark;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.ClueRemarkService;
import com.bjpowernode.crm.workbench.service.ClueService;
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
 * @Date 2022-08-13 20:13
 */

@Controller
public class ClueController {


    @Autowired
    DicValueService dicValueService;
    @Autowired
    UserService userService;
    @Autowired
    ClueService clueService;
    @Autowired
    ClueRemarkService clueRemarkService;
    @Autowired
    ActivityService activityService;


    @RequestMapping("/workbench/clue/index.do")
    public String index(HttpServletRequest request){
        List<User> userList = userService.queryAllUsers();
        List<DicValue> appellationList = dicValueService.queryDicValueByTypeCode("appellation");
        List<DicValue> clueStateList = dicValueService.queryDicValueByTypeCode("clueState");
        List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");
        request.setAttribute("userList", userList);
        request.setAttribute("appellationList", appellationList);
        request.setAttribute("clueStateList", clueStateList);
        request.setAttribute("sourceList", sourceList);
        return "workbench/clue/index";
    }


    @RequestMapping("/workbench/clue/saveCreateClue.do")
    public @ResponseBody Object saveCreateClue(Clue clue, HttpSession session){
        User user=(User)session.getAttribute(Contants.SESSION_USER);

        // 封装参数
        clue.setId(UUIDUtils.getUUID());
        clue.setCreateTime(DateUtils.formatDate(new Date()));
        clue.setCreateBy(user.getId());

        ReturnObject returnObject=new ReturnObject();
        try {
            // 调用service层方法，保存创建的线索
            int ret = clueService.saveCreateClue(clue);

            if(ret>0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else{
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统繁忙，请稍后重试......");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统繁忙，请稍后重试......");
        }

        return returnObject;
    }

    @RequestMapping("/workbench/clue/detailClue.do")
    public String detailClue(String id, HttpServletRequest request){
        // 调用service层方法，查询数据
        Clue clue = clueService.queryClueForDetailById(id);
        List<ClueRemark> remarkList = clueRemarkService.queryClueRemarkForDetailByClueId(id);
        List<Activity> activityList = activityService.queryActivityForDetailByClueId(id);
        // 把数据保存到request中
        request.setAttribute("clue", clue);
        request.setAttribute("remarkList", remarkList);
        request.setAttribute("activityList", activityList);
        // 请求转发
        return "workbench/clue/detail";
    }






}
