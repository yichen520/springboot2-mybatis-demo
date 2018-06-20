package com.dhht.config;


import com.dhht.model.Resource;
import com.dhht.model.Users;
import com.dhht.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public boolean validateResource(List<Resource> resources, String path) {
        if (resources == null) return false;
        for (Resource resource : resources) {
            if (resource.getUrl().equals(path)) {
                return true;
            }
            if (validateResource(resource.getChildren(), path)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //登录不做要求
        if(request.getRequestURI().equals("/login")){
            return true;
        }
        //获取session是否存在
        Object object = request.getSession().getAttribute("user");
        if(object==null){
            response.setStatus(401);
            return false;
        }else {
            List<Resource> resources = (List<Resource>)request.getSession().getAttribute("resources");
            String path = request.getServletPath();
            if (path.equals("/login")||path.equals("/menu")||path.equals("/currentUser")){
                return true;
            }
            if (validateResource(resources,path)){
                try {
                    return true;
                } catch (Throwable e) {
                    e.printStackTrace();
                    response.setStatus(403);
                    return false;
                }
            }else {
                response.setStatus(403);
                return false;
            }
        }
    }

    /**
     * 记录登录成功
     * @param request 当前用户请求
     * @param
     * @return 操作成功：返回sessionId，操作失败：返回null
     */
    public static void loginSuccess(HttpServletRequest request ,Users User) {
//        Timestamp curTime = DateUtil.getCurrentTime();
//        String sessionId = SystemCommonConfig.LOGIN_USER + DateUtil.longToTimeStr(curTime.getTime(), DateUtil.dateFormat10) +
//                NumberUtil.createNumberStr(12);
//
//
//        UserSessionDO userSessionDO = new UserSessionDO();
//
//        boolean insertResult = getSessionService().insertUserSession(userSessionDO);
//        if (!insertResult) {
//            return;
//        }
//        //session缓存信息
//        userSession.setOutTime(curTime.getTime());
//        request.getSession().setAttribute("user", user);
//        //记录用户id与sessionid的映射关系
//        String springSessionId = request.getSession().getId();
//        String SESSION_USER_KEY = SessionConfig.SESSION_USER_ID + String.valueOf(userSession.getUserId());
//        String SESSION_SESSIONS_KEY = SessionConfig.SESSION_SESSIONS + springSessionId;
//        getRedisService().addList(SESSION_USER_KEY, SESSION_SESSIONS_KEY, SessionConfig.SESSION_TIME_OUT);
//        return;
    }
}
