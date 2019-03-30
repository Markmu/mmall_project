package com.mmall.controller.portal;

import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.Const;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisShardedPoolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user/springsession/")
public class UserSpringSessionController {

    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "login.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session, HttpServletResponse httpServletResponse) {

        int i = 0;
        int j = 10/i;

        ServerResponse<User> sr = iUserService.login(username, password);
        if (sr.isSuccess()) {
//            String sessionId = session.getId();
//            CookieUtil.writeLoginToken(httpServletResponse, sessionId);
//            RedisShardedPoolUtil.setex(sessionId, JsonUtil.obj2String(sr.getData()), Const.RedisCache.SESSION_EXTIME);
            User user = sr.getData();
            session.setAttribute(Const.CURRENT_USER, user);
        }
        return sr;
    }


    @RequestMapping(value = "logout.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session,HttpServletRequest request, HttpServletResponse response) {
//        String loginToken = CookieUtil.readLoginToken(request);
//        CookieUtil.delLoginToken(request, response);
//        RedisShardedPoolUtil.del(loginToken);
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }


    @RequestMapping(value = "get_user_info.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session, HttpServletRequest request) {
//        String sessionId = CookieUtil.readLoginToken(request);
//        if (sessionId == null) {
//            return ServerResponse.createByErrorMessage("用户未登录,请重新登录");
//        }
//        User user = JsonUtil.string2Obj(RedisShardedPoolUtil.get(sessionId), User.class);
        User user = (User) session.getAttribute(Const.CURRENT_USER);

        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录,请重新登录");
        }
        return ServerResponse.createBySuccess(user);
    }

}
