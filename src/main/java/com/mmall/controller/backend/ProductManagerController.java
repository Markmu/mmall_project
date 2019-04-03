package com.mmall.controller.backend;


import com.google.common.collect.Maps;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IFileService;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import com.mmall.util.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/manage/product/")
public class ProductManagerController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IProductService iProductService;

    @Autowired
    private IFileService iFileService;

    @RequestMapping(value = "save.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> productSave(HttpServletRequest request, Product product) {
//        String loginToken = CookieUtil.readLoginToken(request);
//        User currentUser = JsonUtil.string2Obj(RedisShardedPoolUtil.get(loginToken), User.class);
//        if (currentUser == null) {
//            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
//        }
//        if (iUserService.checkRoleAdmin(currentUser).isSuccess()) {
//            return iProductService.saveOrUpdateProduct(product);
//        }
//        return ServerResponse.createByErrorMessage("无权限操作");
        return iProductService.saveOrUpdateProduct(product);

    }

    @RequestMapping(value = "set_sale_status.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> setSaleStatus(HttpServletRequest request, Integer productId, Integer status) {
//        String loginToken = CookieUtil.readLoginToken(request);
//        User user = JsonUtil.string2Obj(RedisShardedPoolUtil.get(loginToken), User.class);
//        if (user == null) {
//            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
//        }
//        if (iUserService.checkRoleAdmin(user).isSuccess()) {
//            return iProductService.setSaleStatus(productId, status);
//        }
//        return ServerResponse.createByErrorMessage("无权限操作");

        return iProductService.setSaleStatus(productId, status);

    }

    @RequestMapping(value = "detail.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse manageProductDetail(HttpServletRequest request, Integer productId) {
//        String loginToken = CookieUtil.readLoginToken(request);
//        User user = JsonUtil.string2Obj(RedisShardedPoolUtil.get(loginToken), User.class);
//        if (user == null) {
//            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
//        }
//        if (iUserService.checkRoleAdmin(user).isSuccess()) {
//            return iProductService.manageProductDetail(productId);
//        }
//        return ServerResponse.createByErrorMessage("无权限操作");
        return iProductService.manageProductDetail(productId);

    }

    @RequestMapping(value = "list.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getList(HttpServletRequest request,
                                  @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                  @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
//        String loginToken = CookieUtil.readLoginToken(request);
//        User user = JsonUtil.string2Obj(RedisShardedPoolUtil.get(loginToken), User.class);
//        if (user == null) {
//            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
//        }
//        if (iUserService.checkRoleAdmin(user).isSuccess()) {
//            return iProductService.getProductList(pageNum, pageSize);
//        }
//        return ServerResponse.createByErrorMessage("无权限操作");

        return iProductService.getProductList(pageNum, pageSize);

    }

    @RequestMapping(value = "search.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse search(HttpServletRequest request, String productName, Integer productId,
                                 @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                 @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
//        String loginToken = CookieUtil.readLoginToken(request);
//        User user = JsonUtil.string2Obj(RedisShardedPoolUtil.get(loginToken), User.class);
//        if (user == null) {
//            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
//        }
//        if (iUserService.checkRoleAdmin(user).isSuccess()) {
//            return iProductService.searchProductList(productName, productId, pageNum, pageSize);
//        }
//        return ServerResponse.createByErrorMessage("无权限操作");

        return iProductService.searchProductList(productName, productId, pageNum, pageSize);

    }

    @RequestMapping(value = "upload.do")
    @ResponseBody
    public ServerResponse upload(@RequestParam("upload_file") MultipartFile file, HttpServletRequest request) {
//        String loginToken = CookieUtil.readLoginToken(request);
//        User user = JsonUtil.string2Obj(RedisShardedPoolUtil.get(loginToken), User.class);
//        if (user == null) {
//            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
//        }
//        if (iUserService.checkRoleAdmin(user).isSuccess()) {
//            String path = request.getSession().getServletContext().getRealPath("upload");
//            String targetFileName = iFileService.upload(file, path);
//            String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;
//            Map fileMap = Maps.newHashMap();
//            fileMap.put("url", url);
//            fileMap.put("uri", targetFileName);
//            return ServerResponse.createBySuccess(fileMap);
//        }
//        return ServerResponse.createByErrorMessage("无权限操作");

        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = iFileService.upload(file, path);
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;
        Map fileMap = Maps.newHashMap();
        fileMap.put("url", url);
        fileMap.put("uri", targetFileName);
        return ServerResponse.createBySuccess(fileMap);
    }
    @RequestMapping(value = "richtext_img_upload.do")
    @ResponseBody
    public Map richtextImgUpload(@RequestParam("upload_file") MultipartFile file,
                                 HttpServletRequest request, HttpServletResponse response) {
//        Map resultMap = Maps.newHashMap();
//        String loginToken = CookieUtil.readLoginToken(request);
//        User user = JsonUtil.string2Obj(RedisShardedPoolUtil.get(loginToken), User.class);
//        if (user == null) {
//            resultMap.put("success", false);
//            resultMap.put("msg", "用户未登录，请登录");
//            return resultMap;
//        }
//        if (iUserService.checkRoleAdmin(user).isSuccess()) {
//            String path = request.getSession().getServletContext().getRealPath("upload");
//            String targetFileName = iFileService.upload(file, path);
//            if (StringUtils.isBlank(targetFileName)) {
//                resultMap.put("success", false);
//                resultMap.put("msg", "上传失败");
//                return resultMap;
//            }
//            String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;
//            resultMap.put("success", true);
//            resultMap.put("msg", "上传成功");
//            resultMap.put("file_path", url);
//            response.addHeader("Access-Controll-Allow-Headers", "X-File-Name");
//            return resultMap;
//        }
//
//        resultMap.put("success", false);
//        resultMap.put("msg", "无权限操作");
//        return resultMap;

        Map resultMap = Maps.newHashMap();
        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = iFileService.upload(file, path);
        if (StringUtils.isBlank(targetFileName)) {
            resultMap.put("success", false);
            resultMap.put("msg", "上传失败");
            return resultMap;
        }
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;
        resultMap.put("success", true);
        resultMap.put("msg", "上传成功");
        resultMap.put("file_path", url);
        response.addHeader("Access-Controll-Allow-Headers", "X-File-Name");
        return resultMap;
    }

}
