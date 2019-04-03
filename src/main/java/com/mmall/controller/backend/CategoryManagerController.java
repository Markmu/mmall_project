package com.mmall.controller.backend;


import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisShardedPoolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/manage/category/")
public class CategoryManagerController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private ICategoryService iCategoryService;


    @RequestMapping(value = "add_category.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> addCategory(HttpServletRequest request, String categoryName, @RequestParam(value = "parentId", defaultValue = "0") int parentId) {
//        String loginToken = CookieUtil.readLoginToken(request);
//        User currentUser = JsonUtil.string2Obj(RedisShardedPoolUtil.get(loginToken), User.class);
//        if (currentUser == null) {
//            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录, 请登录");
//        }
//        ServerResponse sr = iUserService.checkRoleAdmin(currentUser);
//        if (sr.isSuccess()) {
//            return iCategoryService.addCategory(categoryName, parentId);
//        } else {
//            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
//        }
        return iCategoryService.addCategory(categoryName, parentId);
    }

    @RequestMapping(value = "set_category.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> setCategory(HttpServletRequest request, Integer categoryId, String categoryName) {
//        String loginToken = CookieUtil.readLoginToken(request);
//        User currentUser = JsonUtil.string2Obj(RedisShardedPoolUtil.get(loginToken), User.class);
//        if (currentUser == null) {
//            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录, 请登录");
//        }
//        ServerResponse sr = iUserService.checkRoleAdmin(currentUser);
//        if (sr.isSuccess()) {
//            return iCategoryService.setCategoryName(categoryId, categoryName);
//        } else {
//            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
//        }
        return iCategoryService.setCategoryName(categoryId, categoryName);
    }

    @RequestMapping(value = "get_category.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getChildrenParallelCategory(HttpServletRequest request, @RequestParam(value = "parentId", defaultValue = "0") Integer parentId) {
//        String loginToken = CookieUtil.readLoginToken(request);
//        User currentUser = JsonUtil.string2Obj(RedisShardedPoolUtil.get(loginToken), User.class);
//        if (currentUser == null) {
//            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录, 请登录");
//        }
//        ServerResponse sr = iUserService.checkRoleAdmin(currentUser);
//        if (sr.isSuccess()) {
//            return iCategoryService.getChildrenParallelCategory(parentId);
//        } else {
//            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
//        }
        return iCategoryService.getChildrenParallelCategory(parentId);
    }
    @RequestMapping(value = "get_deep_category.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getCategoryAndDeepChildrenCategory(HttpServletRequest request, @RequestParam(value = "parentId", defaultValue = "0") Integer parentId) {
//        String loginToken = CookieUtil.readLoginToken(request);
//        User currentUser = JsonUtil.string2Obj(RedisShardedPoolUtil.get(loginToken), User.class);
//        if (currentUser == null) {
//            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录, 请登录");
//        }
//        ServerResponse sr = iUserService.checkRoleAdmin(currentUser);
//        if (sr.isSuccess()) {
//            return iCategoryService.getCategoryAndDeepChildrenCategory(parentId);
//        } else {
//            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
//        }
        return iCategoryService.getCategoryAndDeepChildrenCategory(parentId);
    }

}
