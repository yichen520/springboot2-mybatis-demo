package com.dhht.sync.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.Resource;
import com.dhht.sync.SyncDataType;
import com.dhht.sync.SyncOperateType;
import com.dhht.sync.service.SyncDataToOutService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


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

	@AfterReturning(value = "webLog()", returning = "rtv")
	public void saveProject(JoinPoint joinPoint, Object rtv) throws Throwable {
		JsonObjectBO jsonObjectBO =(JsonObjectBO)rtv;
		JSONObject jsonObject = jsonObjectBO.getData();
		Resource resource = (Resource)jsonObject.get("resource");
     	// 遍历jsonObject数据,添加到Map对象
		String projectStr =JSON.toJSONString(resource, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.DisableCircularReferenceDetect);
		syncDataToOutService.saveResult(SyncDataType.PROJECT, SyncOperateType.SAVE,JsonObjectBO.SUCCESS,projectStr);
	}

}
