package com.dhht.controller.app;

import com.alibaba.fastjson.JSONObject;
import com.dhht.annotation.Log;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.APKVersion;
import com.dhht.service.user.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
@RestController
public class AppUpdateController {
    @Autowired
    private UserLoginService userLoginService;

    @Log("获取最新版本app")
    @RequestMapping(value ="app/versionupdate")
    public JsonObjectBO versionupdate(){
        try {
            APKVersion apkVersion = userLoginService.versionupdate();
            JSONObject jsonObject =new JSONObject();
            jsonObject.put("version",apkVersion);
            return JsonObjectBO.success("获取最新版本成功",jsonObject);
        } catch (Exception e) {
            return JsonObjectBO.exception(e.toString());
        }
    }

    @Log("新增版本")
    @RequestMapping(value ="app/addversion")
    public JsonObjectBO addversion(HttpServletRequest request,@RequestBody APKVersion apkVersion){
        try {
            if(userLoginService.addversion(request,apkVersion)){
                return JsonObjectBO.success("新增版本成功",null);
            }else {
                return JsonObjectBO.error("新增版本失败");
            }
        } catch (Exception e) {
            return JsonObjectBO.exception(e.toString());
        }
    }

    @Log("查询所有app版本")
    @RequestMapping(value ="app/info")
    public JsonObjectBO getAllApk(){
        try {
            List<APKVersion> apkVersion = userLoginService.getAllApk();
            JSONObject jsonObject =new JSONObject();
            jsonObject.put("version",apkVersion);
            return JsonObjectBO.success("查询所有app版本成功",jsonObject);
        } catch (Exception e) {
            return JsonObjectBO.exception(e.toString());
        }
    }
    @Log("新增app崩溃日志")
    @RequestMapping(value ="app/breaklog")
    public JsonObjectBO reportCrashLog(HttpServletRequest request, @RequestBody Map map) {
        try {
            if(userLoginService.insertlog(request,map) ){
                return JsonObjectBO.success("日志添加成功",null);
            }else {
                return JsonObjectBO.error("日志添加失败");
            }
        } catch (Exception e) {
            return JsonObjectBO.exception(e.toString());
        }
    }
}
