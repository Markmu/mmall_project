package com.mmall.controller.portal;


import com.github.pagehelper.PageInfo;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Shipping;
import com.mmall.pojo.User;
import com.mmall.service.IShippingService;
import com.mmall.util.Const;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisPoolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/shipping/")
public class ShippingController {

    @Autowired
    private IShippingService iShippingService;

    @RequestMapping(value = "add.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse add(HttpServletRequest request, Shipping shipping) {
        String loginToken = CookieUtil.readLoginToken(request);
        User user = JsonUtil.string2Obj(RedisPoolUtil.get(loginToken), User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iShippingService.add(user.getId(), shipping);
    }

    @RequestMapping(value = "del.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse del(HttpServletRequest request, Integer shippingId) {
        String loginToken = CookieUtil.readLoginToken(request);
        User user = JsonUtil.string2Obj(RedisPoolUtil.get(loginToken), User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iShippingService.del(user.getId(), shippingId);
    }

    @RequestMapping(value = "update.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse update(HttpServletRequest request, Shipping shipping) {
        String loginToken = CookieUtil.readLoginToken(request);
        User user = JsonUtil.string2Obj(RedisPoolUtil.get(loginToken), User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iShippingService.update(user.getId(), shipping);
    }


    @RequestMapping(value = "select.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse select(HttpServletRequest request, Integer shippingId) {
        String loginToken = CookieUtil.readLoginToken(request);
        User user = JsonUtil.string2Obj(RedisPoolUtil.get(loginToken), User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iShippingService.select(user.getId(), shippingId);
    }

    @RequestMapping(value = "list.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<PageInfo> list(HttpServletRequest request,
                                         @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        String loginToken = CookieUtil.readLoginToken(request);
        User user = JsonUtil.string2Obj(RedisPoolUtil.get(loginToken), User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iShippingService.list(pageNum, pageSize, user.getId());
    }


}
