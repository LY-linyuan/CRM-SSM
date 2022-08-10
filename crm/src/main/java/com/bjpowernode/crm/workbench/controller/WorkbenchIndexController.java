package com.bjpowernode.crm.workbench.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author 临渊
 * @Date 2022-08-10 11:32
 */

@Controller
public class WorkbenchIndexController {


    @RequestMapping("/workbench/index.do")
    public String index() {
        return "workbench/index";
    }


}
