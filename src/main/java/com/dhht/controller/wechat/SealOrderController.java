package com.dhht.controller.wechat;

import com.alibaba.fastjson.JSONObject;
import com.dhht.common.JsonObjectBO;
import com.dhht.controller.web.BaseController;
import com.dhht.model.SealOrder;
import com.dhht.model.WeChatUser;
import com.dhht.service.order.OrderService;
import com.dhht.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "weChat/sealOrder")
public class SealOrderController extends WeChatBaseController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private HttpServletRequest httpServletRequest;
    /**
     * 根据session中的用户查询订单
     */
    @RequestMapping(value = "/orderInfo", method = RequestMethod.POST)
    public JsonObjectBO order(@RequestBody Map map, HttpServletResponse httpServletResponse) {
        try {
            String type = (String) map.get("type");
            init(httpServletRequest, httpServletResponse);
            JSONObject jsonObject = new JSONObject();
            WeChatUser weChatUser = currentUser();
            List<SealOrder> sealOrders = orderService.selectOrder(type, weChatUser.getTelphone());
            jsonObject.put("sealorders", sealOrders);
            return JsonObjectBO.success("查询订单成功", jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonObjectBO.exceptionWithMessage(e.getMessage(), "查询订单失败");
        }
    }

    @RequestMapping(value = "/updateOrder")
    public JsonObjectBO updateOrder(@RequestBody Map map){
        try {
            String id = (String) map.get("id");
            String pagJsOrderId = (String)map.get("payJsOrderId");
            String payWay = ("在线支付");
            int result = orderService.updatePayStatus(payWay,id,pagJsOrderId);
            if(result>0){
                return JsonObjectBO.ok("订单更新成功");
            }else {
                return JsonObjectBO.error("订单更新错误，请联系管理员");
            }
        }catch (Exception e){
            return JsonObjectBO.exception("订单更新异常，请联系管理员");
        }
    }

    @RequestMapping(value = "/cancelOrder")
    public JsonObjectBO cancelOrder(@RequestBody Map map,HttpServletResponse httpServletResponse){
        try {
            init(httpServletRequest, httpServletResponse);
            WeChatUser weChatUser = currentUser();
            String id = (String)map.get("id");
            int res = orderService.cancelOrder(id,weChatUser);
            return ResultUtil.getResult(res);
        }catch (Exception e){
            return JsonObjectBO.exception("取消订单失败");
        }
    }


}
