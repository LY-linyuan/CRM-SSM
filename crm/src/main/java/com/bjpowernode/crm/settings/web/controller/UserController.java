package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author 临渊
 * @Date 2022-08-09 21:01
 */

@Controller
public class UserController {


    @Autowired
    private UserService userService;


    @RequestMapping("/settings/qx/user/toLogin.do")
    public String toLogin() {
        return "settings/qx/user/login";
    }


    @RequestMapping("/settings/qx/user/login.do")
    @ResponseBody
    private Object toLogin(String loginAct, String loginPwd, String isRemPwd, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("loginAct", loginAct);
        map.put("loginPwd", loginPwd);
        User user = userService.queryUserByLoginActAndPwd(map);

        // 相应信息
        ReturnObject returnObject = new ReturnObject();
        if (user == null) {
            // 用户名或密码错误
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("用户名或密码错误");
        } else {
            // 进一步验证
            // 过期时间
            if (DateUtils.formatDate(new Date()).compareTo(user.getExpireTime()) > 0) {
                // 过期了
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("账号已过期");
            } else if ("0".equals(user.getLockState())){
                // 没过期  判断状态是否锁定
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("账号已锁定");
            } else if (!user.getAllowIps().contains(request.getRemoteAddr())) {
                // 没过期 状态 也没有锁定  判断ip地址
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("ip地址受限");
            } else {
                // 登录成功
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                session.setAttribute(Contants.SESSION_USER, user);
                // 如果需要记住密码  存储到cookie
                if ("true".equals(isRemPwd)) {
                    Cookie cookie1 = new Cookie("loginAct", loginAct);
                    Cookie cookie2 = new Cookie("loginPwd", loginPwd);
                    cookie1.setMaxAge(60 * 60 * 24 * 10);
                    cookie2.setMaxAge(60 * 60 * 24 * 10);
                    response.addCookie(cookie1);
                    response.addCookie(cookie2);
                } else {
                    // 删除没有过期的cookie
                    Cookie cookie1 = new Cookie("loginAct", "1");
                    Cookie cookie2 = new Cookie("loginPwd", "1");
                    cookie1.setMaxAge(0);
                    cookie2.setMaxAge(0);
                    response.addCookie(cookie1);
                    response.addCookie(cookie2);
                }
            }
        }

        return returnObject;
    }


    @RequestMapping("/settings/qx/user/logout.do")
    public String logout(HttpServletResponse response, HttpSession session) {
        // 清空cookie
        Cookie cookie1 = new Cookie("loginAct", "1");
        Cookie cookie2 = new Cookie("loginPwd", "1");
        cookie1.setMaxAge(0);
        cookie2.setMaxAge(0);
        response.addCookie(cookie1);
        response.addCookie(cookie2);
        // 销毁Session
        session.invalidate();
        // 跳转到首页
        return "redirect:/";
    }

}
