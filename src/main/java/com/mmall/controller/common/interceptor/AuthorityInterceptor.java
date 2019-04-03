package com.mmall.controller.common.interceptor;

import com.google.common.collect.Maps;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.util.Const;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

@Slf4j
public class AuthorityInterceptor implements HandlerInterceptor{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        String methodName = handlerMethod.getMethod().getName();
        String className = handlerMethod.getBean().getClass().getSimpleName();
        StringBuilder requestParamsBuilder = new StringBuilder();
        Map requestParamMap = request.getParameterMap();
        Iterator iter = requestParamMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String) entry.getKey();
            String value = StringUtils.EMPTY;
            // request 中参数map的value类型是String数组
            Object obj = entry.getKey();
            if (obj instanceof String[]) {
                String[] args = (String[]) obj;
                value = Arrays.toString(args);
            }
            requestParamsBuilder.append(key).append("=").append(value).append("  ");
        }

        if (StringUtils.equals(className, "UserManagerController") && StringUtils.equals(methodName, "login")) {
            log.info("权限拦截器拦截到请求，className:{}, methodName:{}", className, methodName);
            return true;
        }

        log.info("权限拦截器拦截到请求，className:{}, methodName:{}, params:{}", className, methodName, requestParamsBuilder);

        User user = null;

        String loginToken = CookieUtil.readLoginToken(request);
        user = JsonUtil.string2Obj(RedisShardedPoolUtil.get(loginToken), User.class);
        if (user == null || (user.getRole().intValue() != Const.Role.ROLE_ADMIN)) {
            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");

            PrintWriter out = response.getWriter();
            if (user == null) {
                if (StringUtils.equals(className, "ProductManagerController")
                        && StringUtils.equals(methodName, "richtextImgUpload")) {
                    Map resultMap = Maps.newHashMap();
                    resultMap.put("success", false);
                    resultMap.put("msg", "用户未登录，请登录");
                    out.print(resultMap);
                } else {
                    out.print(JsonUtil.obj2String(ServerResponse.createByErrorMessage("拦截：用户未登录")));
                }
            } else {
                if (StringUtils.equals(className, "ProductManagerController")
                        && StringUtils.equals(methodName, "richtextImgUpload")) {
                    Map resultMap = Maps.newHashMap();
                    resultMap.put("success", false);
                    resultMap.put("msg", "无权限操作");
                    out.print(resultMap);
                } else {
                    out.print(JsonUtil.obj2String(ServerResponse.createByErrorMessage("拦截：无管理员权限")));
                }
            }

            out.flush();
            out.close();

            return false;
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
