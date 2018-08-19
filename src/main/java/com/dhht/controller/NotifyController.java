package com.dhht.controller;

import com.alibaba.fastjson.JSONObject;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.FileInfo;
import com.dhht.model.Notify;
import com.dhht.model.Role;
import com.dhht.model.User;
import com.dhht.service.message.NotifyService;
import com.dhht.service.user.RoleService;
import com.dhht.util.ResultUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/message/notify")
public class NotifyController {

    @Autowired
    private NotifyService notifyService;
    @Autowired
    private RoleService roleService;

    /**
     * 登录时请求获取新通知的条数
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/notifyTip")
    public JsonObjectBO newNotify(HttpServletRequest httpServletRequest){
        try {
            User user = (User)httpServletRequest.getSession().getAttribute("user");
            JSONObject jsonObject = new JSONObject();

            int i =  notifyService.countNewNotify(user.getId());
            jsonObject.put("notifyCount",i);
            return JsonObjectBO.success("查询成功",jsonObject);
        }catch (Exception e){
            return JsonObjectBO.exception(e.toString());
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
        JSONObject jsonObject = new JSONObject();

        try {
            PageInfo<Notify> pageInfo =notifyService.selectNotifyDetail(user.getId(),pageNum,pageSize);
            jsonObject.put("notify",pageInfo);
            return JsonObjectBO.success("查询成功",jsonObject);
        }catch (Exception e){
            return JsonObjectBO.exception("查询失败");
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


        try {
            PageHelper.startPage(pageNum, pageSize);
            List<Notify> list = notifyService.selectNotifyBySendUser(user.getUserName());
            PageInfo<Notify> pageInfo = new PageInfo<>(list);
            jsonObject.put("notify",pageInfo);
            return JsonObjectBO.success("查询成功",jsonObject);
        }catch (Exception e){
            return JsonObjectBO.exception("获取通知数据失败！");
        }
    }

    /**
     * 角色用户接口
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/roleUser")
    public JsonObjectBO getRoleUser(HttpServletRequest httpServletRequest){
        User user = (User)httpServletRequest.getSession().getAttribute("user");
        JSONObject jsonObject = new JSONObject();

        try{
            List<Role> roles = roleService.getRoleUser(user.getDistrictId());
            jsonObject.put("roleUser",roles);
            return JsonObjectBO.success("查询成功",jsonObject);
        }catch (Exception e){
            //System.out.println(e.toString());
            return JsonObjectBO.exception("获取用户信息失败！");
        }
    }

}
