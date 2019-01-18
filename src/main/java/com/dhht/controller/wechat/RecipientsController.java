package com.dhht.controller.wechat;

import com.alibaba.fastjson.JSONObject;
import com.dhht.annotation.Log;
import com.dhht.common.JsonObjectBO;
import com.dhht.controller.web.BaseController;
import com.dhht.model.Courier;
import com.dhht.model.Recipients;
import com.dhht.model.User;
import com.dhht.service.courier.CourierService;
import com.dhht.service.recipients.RecipientsService;
import com.dhht.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/weChat/recopients")
public class RecipientsController extends WeChatBaseController {
    @Autowired
    private RecipientsService recipientsService;

    @Autowired
    private CourierService courierService;

    @Autowired
    private HttpServletRequest httpServletRequest;



    @Log("邮寄用户信息添加")
    @RequestMapping("/insertRecipients")
    public JsonObjectBO insertRecipients(@RequestBody Recipients recipients,HttpServletResponse httpServletResponse){
        String telPhone = currentUserMobilePhone();
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        init(httpServletRequest,httpServletResponse);
        try {
            int a = recipientsService.insertRecipients(recipients,telPhone);
            if (a == ResultUtil.isSuccess) {
                jsonObjectBO.setCode(1);
                jsonObjectBO.setMessage("添加成功");
            } else {
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("添加失败");
            }
            return jsonObjectBO;
        } catch (Exception e) {
            e.printStackTrace();
            return JsonObjectBO.exception("添加失败");
        }
    }
    @Log("邮寄用户信息修改")
    @RequestMapping("/updateRecipients")
    public JsonObjectBO updateRecipients(@RequestBody Recipients recipients,HttpServletResponse httpServletResponse){
        String telPhone = currentUserMobilePhone();
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        init(httpServletRequest,httpServletResponse);
        try {
//            Recipients recipients = (Recipients)map.get("Recipients");
            int a = recipientsService.updateRecipients(recipients,telPhone);

            if (a == ResultUtil.isSuccess) {
                jsonObjectBO.setCode(1);
                jsonObjectBO.setMessage("修改成功");
            } else {
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("修改失败");
            }
            return jsonObjectBO;
        } catch (Exception e) {
            e.printStackTrace();
            return JsonObjectBO.exception("修改失败");
        }
    }

    @Log("邮寄用户信息删除")
    @RequestMapping("/deleteRecipients")
    public JsonObjectBO deleteRecipients(@RequestBody Map map,HttpServletResponse httpServletResponse){
        String telPhone = currentUserMobilePhone();
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        init(httpServletRequest,httpServletResponse);
        try {
            String id = (String)map.get("id");
            int a = recipientsService.deleteRecipients(id);

            if (a == ResultUtil.isSuccess) {
                jsonObjectBO.setCode(1);
                jsonObjectBO.setMessage("删除成功");
            } else {
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("删除失败");
            }
            return jsonObjectBO;
        } catch (Exception e) {
            e.printStackTrace();
            return JsonObjectBO.exception("删除失败");
        }
    }

    @RequestMapping("/recipientsInfo")
    public JsonObjectBO recipientsInfo(HttpServletResponse httpServletResponse){
//        User user = currentUser();
        init(httpServletRequest,httpServletResponse);
        String telPhone = currentUserMobilePhone();
        try {
            List<Recipients> recipientsLists = recipientsService.recipientsList(telPhone);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("recipientsLists",recipientsLists);
            return JsonObjectBO.success("查询成功",jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonObjectBO.exception("查询失败");
        }
    }


    @Log("邮件信息增加")
    @RequestMapping("/insertCourier")
    public JsonObjectBO insertCourier(@RequestBody Courier courier,HttpServletResponse httpServletResponse){
        String telPhone = currentUserMobilePhone();
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        init(httpServletRequest,httpServletResponse);
        try {
            int insertCourier = courierService.insertCourier(courier);
            if (insertCourier == ResultUtil.isSuccess) {
                jsonObjectBO.setCode(1);
                jsonObjectBO.setMessage("新增成功");
            } else {
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("新增失败");
            }
            return jsonObjectBO;
        } catch (Exception e) {
            e.printStackTrace();
            return JsonObjectBO.exception("新增失败");
        }
    }


    @Log("邮件信息查询")
    @RequestMapping("/courierList")
    public JsonObjectBO courierList(String recipientsId,HttpServletResponse httpServletResponse){
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        init(httpServletRequest,httpServletResponse);
        try {
            List<Courier> couriers = courierService.courierList(recipientsId);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("couriers",couriers);
            return JsonObjectBO.success("查询成功",jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonObjectBO.exception("查询失败");
        }
    }
}
