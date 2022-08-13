package com.bjpowernode.crm.workbench.controller;

import com.bjpowernode.crm.workbench.service.ActivityRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @Author 临渊
 * @Date 2022-08-13 10:42
 */

@Controller
public class ActivityRemarkController {

    @Autowired
    ActivityRemarkService activityRemarkService;
}
