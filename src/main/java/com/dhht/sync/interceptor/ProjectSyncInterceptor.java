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
	public void webLog(){}

	@AfterReturning(value = "webLog()", returning = "syncEntity")
	public void saveProject(JoinPoint joinPoint, SyncEntity syncEntity) throws Throwable {

		//JSONObject jsonObject = jsonObjectBO.getData();
//		Resource resource = (Resource)jsonObject.get("resource");
//     	// 遍历jsonObject数据,添加到Map对象
		Employee employee = (Employee)syncEntity.getObject();
		String projectStr =JSON.toJSONString(employee, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.DisableCircularReferenceDetect);
		syncDataToOutService.saveResult(SyncDataType.PROJECT, SyncOperateType.SAVE,JsonObjectBO.SUCCESS,projectStr);
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
