package com.dhht.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by imac_dhht on 2018/6/11.
 */
public class InterceptorConfig implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(InterceptorConfig.class);

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,Exception ex) throws Exception {

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //登录不做要求
        if(request.getRequestURI().equals("/login")){
            return true;
        }
        //获取session是否存在
        Object object = request.getSession().getAttribute("User");
        if(object==null){
//            response.sendRedirect(request.getContextPath()+"/login");
            response.setStatus(401);
            return false;
        }else {
            return true;
        }
    }
}
