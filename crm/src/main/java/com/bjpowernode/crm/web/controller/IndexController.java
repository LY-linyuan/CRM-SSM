package com.bjpowernode.crm.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author 临渊
 * @Date 2022-08-09 20:38
 */

@Controller
public class IndexController {

    @RequestMapping("/")
    public String index() {
        return "index";
    }


}
