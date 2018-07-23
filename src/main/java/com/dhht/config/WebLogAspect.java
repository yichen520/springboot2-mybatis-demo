package com.dhht.config;


import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.dhht.annotation.Log;
import com.dhht.common.JsonObjectBO;
import com.dhht.dao.LogDao;
import com.dhht.model.Resource;
import com.dhht.model.SysLog;
import com.dhht.model.User;
import com.dhht.util.IPUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class WebLogAspect {

    private Logger logger = Logger.getLogger(getClass());
    @Autowired
    private LogDao logDao;

    @Pointcut("execution(public * com.dhht.controller..*.*(..))")
    public void log(){}

    @Pointcut("@annotation(com.dhht.annotation.Log)")
    public void webLog(){}



//    private void saveLog(ProceedingJoinPoint joinPoint, long time) {
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        Method method = signature.getMethod();
//        SysLog sysLog = new SysLog();
//        Log logAnnotation = method.getAnnotation(Log.class);
//        if (logAnnotation != null) {
//            // 注解上的描述
//            sysLog.setLogType(logAnnotation.value());
//        }
//        // 获取request
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = attributes.getRequest();
//        // 设置IP地址
//        sysLog.setIp(IPUtil.getIpAddr(request));
//       Users users = (Users)request.getSession(true).getAttribute("user");
//       String user = users.getRealName();
//        sysLog.setLogUser(user);
//        sysLog.setTime((int) time);
//        String dateStr = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
//        sysLog.setLogTime(dateStr);
//        // 保存系统日志
//        sysLogDao.saveLog(sysLog);
//    }

    @AfterThrowing(value = "log()",throwing = "exception")
    public void doAfterThrowingAdvice(JoinPoint joinPoint,Throwable exception){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        SysLog sysLog = new SysLog();
        Log logAnnotation = method.getAnnotation(Log.class);
        if (logAnnotation != null) {
            // 注解上的描述
            sysLog.setLogType(logAnnotation.value());
        }
        // 获取request
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 设置IP地址
        sysLog.setIp(IPUtil.getIpAddr(request));
        User users = (User)request.getSession(true).getAttribute("user");
        String user = users.getRealName();
        sysLog.setLogUser(user);
        String dateStr = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
        sysLog.setLogTime(dateStr);
        sysLog.setLogResult("发生异常");
        logDao.saveLog(sysLog);
    }

    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(JoinPoint joinPoint, Object ret) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        SysLog sysLog = new SysLog();
        Log logAnnotation = method.getAnnotation(Log.class);
        if (logAnnotation != null) {
            // 注解上的描述
            sysLog.setLogType(logAnnotation.value());
        }
        // 获取request
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 设置IP地址
        sysLog.setIp(IPUtil.getIpAddr(request));
        User users = (User)request.getSession(true).getAttribute("user");
        String user = users.getRealName();
        sysLog.setLogUser(user);
        String dateStr = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
        sysLog.setLogTime(dateStr);
        if (ret instanceof Map ){
            Map<String, String> ret1 = (Map<String, String>)ret;
            String status = ret1.get("status");
            if (status.equals("ok")){
                sysLog.setLogResult("成功");
            }else {
                sysLog.setLogResult("失败");
            }
        }else if(ret instanceof JsonObjectBO) {
           if (((JsonObjectBO) ret).getCode()==1){
               sysLog.setLogResult("成功");
           }else {
               sysLog.setLogResult("失败");
           }
        }
        // 保存系统日志
        logDao.saveLog(sysLog);
    }
}