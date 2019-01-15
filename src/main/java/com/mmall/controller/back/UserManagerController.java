package com.mmall.controller.back;


import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manager/user/")
public class UserManagerController {

    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session) {

        ServerResponse<User> sr = iUserService.login(username, password);
        if (sr.isSuccess()) {
            User user = sr.getData();
            if (user.getRole() == Const.Role.ROLE_ADMIN) {
                return ServerResponse.createBySuccess(user);
            }
            return ServerResponse.createByErrorMessage("不是管理员,无法登录");
        }
        return sr;
    }

}
