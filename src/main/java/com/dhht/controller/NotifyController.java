package com.dhht.controller;

import com.alibaba.fastjson.JSONObject;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.*;
import com.dhht.service.message.NotifyService;
import com.dhht.service.tools.FileService;
import com.dhht.service.user.RoleService;
import com.dhht.util.ResultUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/message/notify")
public class NotifyController {

    @Autowired
    private NotifyService notifyService;
    @Autowired
    private FileService fileService;
    @Autowired
    private RoleService roleService;

    /**
     * 登录时请求获取新通知的条数
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/notifyTip")
    public JsonObjectBO newNotify(HttpServletRequest httpServletRequest){
        User user = (User)httpServletRequest.getSession().getAttribute("user");
        JSONObject jsonObject = new JSONObject();
        try {
            int i =  notifyService.countNewNotify(user.getId());
            jsonObject.put("notifyCount",i);
            return JsonObjectBO.success("查询成功",jsonObject);
        }catch (Exception e){
            return JsonObjectBO.exception("获取通知异常！");
        }
    }

    /**
     * 通知详情接口
     * @param httpServletRequest
     * @param map
     * @return
     */
    @RequestMapping(value = "/notifyDetail")
    public JsonObjectBO notifyDetail(HttpServletRequest httpServletRequest, @RequestBody Map map){
        User user = (User) httpServletRequest.getSession().getAttribute("user");
        int pageNum = (Integer)map.get("pageNum");
        int pageSize = (Integer)map.get("pageSize");

        PageHelper.startPage(pageNum,pageSize);
        JSONObject jsonObject = new JSONObject();

        try {
            PageInfo<Notify> pageInfo = new PageInfo<>(notifyService.selectNotifyDetail(user.getId()));
            jsonObject.put("notify",pageInfo);
            return JsonObjectBO.success("查询成功",jsonObject);
        }catch (Exception e){
            return JsonObjectBO.exception(e.toString());
        }
    }

    /**
     * 通知文件上传
     * @param multipartFiles
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/upload",produces = "application/json;charset=UTF-8")
    public JsonObjectBO upload(@RequestParam("file") MultipartFile multipartFiles, HttpServletRequest httpServletRequest){
        JSONObject jsonObject = new JSONObject();
        try {
            FileInfo file = fileService.insertFile(httpServletRequest,multipartFiles);
            if(file==null){
            return JsonObjectBO.error("文件上传失败");
            }
            jsonObject.put("file",file);
            return JsonObjectBO.success("文件上传成功",jsonObject);
        }catch (Exception e){
            return JsonObjectBO.error("文件上传时发生错误");
        }
    }

    /**
     * 添加通知
     * @param notify
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/insert")
    public JsonObjectBO insertNotify(@RequestBody Notify notify,HttpServletRequest httpServletRequest){
        User user = (User)httpServletRequest.getSession().getAttribute("user");
        return ResultUtil.getResult(notifyService.insertNotify(notify,user));
    }

    /**
     * 撤回通知
     * @param map
     * @return
     */
    @RequestMapping(value = "/recall")
    public JsonObjectBO recallNotify(@RequestBody Map map){
        String id = (String)map.get("id");
        String result = (String)map.get("result");
        return ResultUtil.getResult(notifyService.recallNotify(id,result));
    }

    /**
     * 管理员查看自己法发送的信息
     * @param httpServletRequest
     * @param map
     * @return
     */
    @RequestMapping(value = "/info")
    public JsonObjectBO selectNotifyByUserName(HttpServletRequest httpServletRequest,@RequestBody Map map){
        User user = (User)httpServletRequest.getSession().getAttribute("user");
        int pageNum = (Integer)map.get("pageNum");
        int pageSize = (Integer)map.get("pageSize");
        JSONObject jsonObject = new JSONObject();
        PageHelper.startPage(pageNum, pageSize);

        try {
            List<Notify> list = notifyService.selectNotifyBySendUser(user.getUserName());
            PageInfo<Notify> pageInfo = new PageInfo<>(list);
            jsonObject.put("notify",pageInfo);
            return JsonObjectBO.success("查询成功",jsonObject);
        }catch (Exception e){
            return JsonObjectBO.exception(e.getMessage());
        }
    }

    /**
     * 角色用户接口
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/roleUser")
    public JsonObjectBO getRoleUser(HttpServletRequest httpServletRequest){
       // User user = (User)httpServletRequest.getSession().getAttribute("user");
        JSONObject jsonObject = new JSONObject();

        try{
            List<Role> roles = roleService.getRoleUser("330000");
            jsonObject.put("roleUser",roles);
            return JsonObjectBO.success("查询成功",jsonObject);
        }catch (Exception e){
            return JsonObjectBO.exception("获取角色失败！");
        }
    }

}
