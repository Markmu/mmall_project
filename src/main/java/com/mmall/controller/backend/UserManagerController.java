package com.mmall.controller.backend;


import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.Const;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisPoolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/user/")
public class UserManagerController {

    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password,HttpSession session, HttpServletResponse response) {

        ServerResponse<User> sr = iUserService.login(username, password);
        if (sr.isSuccess()) {
            User user = sr.getData();
            if (user.getRole() == Const.Role.ROLE_ADMIN) {
                String sessionId = session.getId();
                CookieUtil.writeLoginToken(response, sessionId);
                RedisPoolUtil.setex(sessionId, JsonUtil.obj2String(user), Const.RedisCache.SESSION_EXTIME);
                return ServerResponse.createBySuccess(user);
            }
            return ServerResponse.createByErrorMessage("不是管理员,无法登录");
        }

        return sr;
    }

    @RequestMapping(value = "register.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user) {
        return iUserService.adminRegister(user);
    }



}
