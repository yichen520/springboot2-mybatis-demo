package com.dhht.sync.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import com.dhht.sync.SyncDataType;
import com.dhht.sync.SyncOperateType;
import com.dhht.sync.service.SyncDataToOutService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Aspect
@Service
public class ProjectRecordSyncInterceptor {
    @Resource
    private SyncDataToOutService syncDataToOutService;
    
   
    //审核结果
//    @AfterReturning(value = "execution(* com.dhht.service.project.record.TransProjectRecordService.tempUpdateProjectRecord(..))", returning = "rtv")
//    public void operateRecord(JoinPoint joinPoint, Object rtv) throws Throwable {
//    	ProjectRecord pr = (ProjectRecord) rtv;
//        String prStr =JSON.toJSONString(pr, new SimplePropertyPreFilter(ProjectRecord.VIEW_SYNC_PROJECTRECORD), SerializerFeature.WriteDateUseDateFormat, SerializerFeature.DisableCircularReferenceDetect);
//        String reason = pr.getReason(); //退回原因, 通过就为""
//        String companyId = pr.getCompanyId(); //企业id  未通过就为""
//        int step = pr.getStep()-1;
//        User user =  WebUtil.getLoginUser();
//        syncDataToOutService.saveResult(SyncDataType.PROJECTRECORD, SyncOperateType.AGREE, pr.getId(),reason,pr.getState(),companyId,user.getDeptId(),user.getRealName(),step);
//    }
    
}
