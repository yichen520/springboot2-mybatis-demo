package com.dhht.config;

import com.dhht.model.FaceCompareResult;
import com.dhht.service.tools.FileStoreService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;

@Aspect
@Service
@Component
public class DeleteTempFileAop {
    @Resource(name="localStoreFileService")
    private FileStoreService localStoreFileService;

    @Pointcut("@annotation(com.dhht.annotation.DeleteTempFile)")
    public void delete(){}

    @AfterReturning(value = "delete()",returning = "rtv")
    public void deleteTempFile(JoinPoint joinPoint, Object rtv) throws Throwable {
        if(rtv!=null){
            FaceCompareResult faceCompareResult = (FaceCompareResult)rtv;
            File file1 = new File(faceCompareResult.getFieldPhotoPath());
            File file2 = new File(faceCompareResult.getIdCardPhotoPath());
            file1.delete();
            file2.delete();
        }
    }
}
