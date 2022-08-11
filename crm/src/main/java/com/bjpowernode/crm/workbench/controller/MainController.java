package com.bjpowernode.crm.workbench.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author 临渊
 * @Date 2022-08-10 21:25
 */

@Controller
public class MainController {


    @RequestMapping("/workbench/main/index.do")
    public String index() {
        return "workbench/main/index";
    }
}