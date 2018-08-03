package com.dhht.sync.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dhht.annotation.Sync;

import com.dhht.common.JsonObjectBO;
import com.dhht.model.Employee;
import com.dhht.model.Resource;
import com.dhht.model.SyncEntity;
import com.dhht.sync.SyncDataType;
import com.dhht.sync.SyncOperateType;
import com.dhht.sync.service.SyncDataToOutService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

@Aspect
@Service
@Component
public class ProjectSyncInterceptor {

	@Autowired
	private SyncDataToOutService syncDataToOutService;

	@Pointcut("@annotation(com.dhht.annotation.Sync)")
	public void sync(){}

	@AfterReturning(value = "sync()", returning = "rtv")
	public void saveProject(JoinPoint joinPoint, Object rtv) throws Throwable {
		Sync sync=  getAnnotationLog(joinPoint);
		if (rtv !=null){
            String projectStr =JSON.toJSONString(rtv, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.DisableCircularReferenceDetect);
            syncDataToOutService.saveResult(sync.DataType(), sync.OperateType(),JsonObjectBO.SUCCESS,projectStr);
        }
	}

	/**
	 * 是否存在注解，如果存在就获取
	 */
	private static Sync getAnnotationLog(JoinPoint joinPoint) throws Exception {
		Signature signature = joinPoint.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;
		Method method = methodSignature.getMethod();
		if (method != null) {
			return method.getAnnotation(Sync.class);
		}
		return null;
	}


}
