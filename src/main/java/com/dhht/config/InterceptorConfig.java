package com.dhht.config;
import com.dhht.model.Resource;
import com.dhht.model.User;
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
        if (resources == null) {return false;}
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
        boolean judgeIsMoblie = JudgeIsMoblie(request);
//        response.setHeader("Access-Control-Allow-Origin", "*");
        //app端、微信小程序端和门户网站端不拦截
        if(judgeIsMoblie==true||request.getRequestURI().contains("/portal")||request.getRequestURI().contains("/weChat") ){
            if(request.getRequestURI().contains("/recopients")){
                //获取session是否存在
                Object object = request.getSession().getAttribute("user");
                if(object==null ) {
                    String path = request.getServletPath();
                    if (path.equals("/menu") || path.equals("/currentUser")) {
                        response.setStatus(401);
//                        response.setHeader("Access-Control-Allow-Origin", "*");
                        return false;
                    }
                    response.setStatus(401);
                    return false;
                }
            }else {
                return true;
            }
        }

        if(request.getRequestURI().equals("/login")||request.getRequestURI().equals("/logout")||request.getRequestURI().equals("/error")||request.getRequestURI().equals("/sys/user/resetPwd")||request.getRequestURI().equals("/sys/user/Code")
                ||request.getRequestURI().equals("/app/login")||request.getRequestURI().equals("/make/makeDepartment/makeDepartmentCode")||request.getRequestURI().equals("/make/employee/insertEmployeeImport")||request.getRequestURI().equals("/app/login")||request.getRequestURI().equals("/make/makeDepartment/makeDepartmentCode")|| request.getRequestURI().equals("/evaluate/insert")
                || request.getRequestURI().equals("/evaluate/delete")
                || request.getRequestURI().equals("/evaluate/info")
                || request.getRequestURI().equals("/seal/record/sealRecord")
                ||request.getRequestURI().equals("/seal/record/getCheckCode")
                ||request.getRequestURI().equals("/seal/record/newRecord")
                ||request.getRequestURI().equals("/seal/record/underTake")
                ||request.getRequestURI().equals("/seal/record/sealInfo")
//                ||request.getRequestURI().equals("/weChat/sealRecord")
// request.getRequestURI().contains("/weChat")

                ){
            return true;
        }
        //获取session是否存在
        Object object = request.getSession().getAttribute("user");
        if(object==null ){
            String path = request.getServletPath();
            if(path.equals("/menu")||path.equals("/currentUser")){
                response.setStatus(401);
                return false;
            }
            response.setStatus(401);
            return false;
        }
        else {
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
    //判断是手机还是web登录
    public static boolean JudgeIsMoblie(HttpServletRequest request) {
        boolean isMoblie = false;
        String[] mobileAgents = { "iphone", "android","ipad", "phone", "mobile", "wap", "netfront", "java", "opera mobi",
                "opera mini", "ucweb", "windows ce", "symbian", "series", "webos", "sony", "blackberry", "dopod",
                "nokia", "samsung", "palmsource", "xda", "pieplus", "meizu", "midp", "cldc", "motorola", "foma",
                "docomo", "up.browser", "up.link", "blazer", "helio", "hosin", "huawei", "novarra", "coolpad", "webos",
                "techfaith", "palmsource", "alcatel", "amoi", "ktouch", "nexian", "ericsson", "philips", "sagem",
                "wellcom", "bunjalloo", "maui", "smartphone", "iemobile", "spice", "bird", "zte-", "longcos",
                "pantech", "gionee", "portalmmm", "jig browser", "hiptop", "benq", "haier", "^lct", "320x320",
                "240x320", "176x220", "w3c ", "acs-", "alav", "alca", "amoi", "audi", "avan", "benq", "bird", "blac",
                "blaz", "brew", "cell", "cldc", "cmd-", "dang", "doco", "eric", "hipt", "inno", "ipaq", "java", "jigs",
                "kddi", "keji", "leno", "lg-c", "lg-d", "lg-g", "lge-", "maui", "maxo", "midp", "mits", "mmef", "mobi",
                "mot-", "moto", "mwbp", "nec-", "newt", "noki", "oper", "palm", "pana", "pant", "phil", "play", "port",
                "prox", "qwap", "sage", "sams", "sany", "sch-", "sec-", "send", "seri", "sgh-", "shar", "sie-", "siem",
                "smal", "smar", "sony", "sph-", "symb", "t-mo", "teli", "tim-", "tosh", "tsm-", "upg1", "upsi", "vk-v",
                "voda", "wap-", "wapa", "wapi", "wapp", "wapr", "webc", "winw", "winw", "xda", "xda-",
                "Googlebot-Mobile" };
        if (request.getHeader("User-Agent") != null) {
            String agent=request.getHeader("User-Agent");
            for (String mobileAgent : mobileAgents) {
                if (agent.toLowerCase().indexOf(mobileAgent) >= 0&&agent.toLowerCase().indexOf("windows nt")<=0 &&agent.toLowerCase().indexOf("macintosh")<=0) {
                    isMoblie = true;
                    break;
                }
            }
        }
        return isMoblie;
    }

}
