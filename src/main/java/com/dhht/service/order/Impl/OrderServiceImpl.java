package com.dhht.service.order.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dhht.dao.CourierMapper;
import com.dhht.dao.RecipientsMapper;
import com.dhht.dao.SealDao;
import com.dhht.dao.SealPayOrderMapper;
import com.dhht.model.*;
import com.dhht.service.order.OrderService;
import com.dhht.service.seal.SealService;
import com.dhht.util.DateUtil;
import com.dhht.util.ResultUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.*;

@Service("orderService")
@Transactional
public class OrderServiceImpl implements OrderService {

    private static String ACCOUNT_TIP="该刻制单位还未配置此类型印章价格，只能通过到店支付方式付款元";

    @Autowired
    private SealDao sealDao;

    @Autowired
    private SealPayOrderMapper sealPayOrderMapper;

    @Autowired
    private RecipientsMapper recipientsMapper;

    @Autowired
    private CourierMapper courierMapper;

    @Autowired
    private SealService sealService;


    @Override
    public int insertOrder(SealPayOrder sealPayOrder) {
        sealPayOrder.setIsEvaluation(false);
        sealPayOrder.setCreateTime(DateUtil.getCurrentTime());
        return sealPayOrderMapper.insert(sealPayOrder);
    }

    @Override
    public List<SealOrder> selectOrder(String type, String telphone) {
        List<SealOrder> orders = new ArrayList<>();
        if (type.equals("1")) {
            List<Seal> seals = sealDao.selectSealByTelphone(telphone);
            for (int i = 0; i < seals.size(); i++) {
                SealOrder sealOrder = new SealOrder();
                Seal seal = seals.get(i);
                SealPayOrder sealPayOrder = sealPayOrderMapper.selectBySealId(seal.getId());
                if(sealPayOrder.getRefundStatus().equals("0")||sealPayOrder.getRefundStatus().equals("1")){
                    sealPayOrder.setIsRefund(true);
                }else {
                    sealPayOrder.setIsRefund(false);
                }

                if(sealPayOrder.getExpressWay()){
                    sealPayOrder.setExpressWayName("EMS");
                }else {
                    sealPayOrder.setExpressWayName("自取");
                }

                if(ACCOUNT_TIP.contains(sealPayOrder.getPayAccout())){
                    sealPayOrder.setPayAccout("价格到店商议");
                }
                sealOrder.setSeal(seal);
                sealOrder.setSealPayOrder(sealPayOrder);
                orders.add(sealOrder);
            }
        }
        if (type.equals("2")) {
            List<Seal> seals = sealDao.selectSealByTelphone(telphone);
            for (int i = 0; i < seals.size(); i++) {
                SealOrder sealOrder = new SealOrder();
                Seal seal = seals.get(i);
                SealPayOrder sealPayOrder = sealPayOrderMapper.selectBySealId(seal.getId());
                if(sealPayOrder.getRefundStatus().equals("0")||sealPayOrder.getRefundStatus().equals("1")){
                    sealPayOrder.setIsRefund(true);
                }else {
                    sealPayOrder.setIsRefund(false);
                }

                if(sealPayOrder.getExpressWay()){
                    sealPayOrder.setExpressWayName("EMS");
                }else {
                    sealPayOrder.setExpressWayName("自取");
                }
                if(ACCOUNT_TIP.contains(sealPayOrder.getPayAccout())){
                    sealPayOrder.setPayAccout("价格到店商议");
                }
                if (!sealPayOrder.getIsPay()){
                    sealOrder.setSeal(seal);
                    sealOrder.setSealPayOrder(sealPayOrder);
                    orders.add(sealOrder);
                }
            }
        }
        if (type.equals("3")) {
            List<Seal> seals = sealDao.selectSealByTelphone(telphone);
            for (int i = 0; i < seals.size(); i++) {
                SealOrder sealOrder = new SealOrder();
                Seal seal = seals.get(i);
                SealPayOrder sealPayOrder = sealPayOrderMapper.selectBySealId(seal.getId());
                if(sealPayOrder.getRefundStatus().equals("0")||sealPayOrder.getRefundStatus().equals("1")){
                    sealPayOrder.setIsRefund(true);
                }else {
                    sealPayOrder.setIsRefund(false);
                }

                if(sealPayOrder.getExpressWay()){
                    sealPayOrder.setExpressWayName("EMS");
                }else {
                    sealPayOrder.setExpressWayName("自取");
                }
                if(ACCOUNT_TIP.contains(sealPayOrder.getPayAccout())){
                    sealPayOrder.setPayAccout("价格到店商议");
                }
                if (seal.getSealStatusCode().equals("04")||seal.getSealStatusCode().equals("09")){
                    sealOrder.setSeal(seal);
                    sealOrder.setSealPayOrder(sealPayOrder);
                    orders.add(sealOrder);
                }else {

                }
            }
        }
        return orders;
    }

    @Override
    public SealOrder selectOrderDetail(String sealId) {
        SealOrder sealOrder = new SealOrder();
        Seal seal = sealDao.selectByPrimaryKey(sealId);
        SealMaterial sealMaterial = sealDao.selectSealMaterial(seal.getSealCode(),seal.getSealTypeCode());
        Recipients recipients = recipientsMapper.selectByPrimaryKey(courierMapper.selectBySealId(seal.getId()).getRecipientsId());
        sealOrder.setSeal(seal);
        sealOrder.setSealMaterial(sealMaterial);
        sealOrder.setRecipients(recipients);
        return sealOrder;
    }



    @Override
    public int updateEvaluationStatus(String sealId) {
        SealPayOrder sealPayOrder = sealPayOrderMapper.selectBySealId(sealId);
        return sealPayOrderMapper.updateEvaluationStatus(sealPayOrder.getId());
    }

    @Override
    public int updateRefundStatus(String refundStatus, String sealId) {
        SealPayOrder sealPayOrder = sealPayOrderMapper.selectBySealId(sealId);
        return sealPayOrderMapper.updateRefundStatus(refundStatus,sealPayOrder.getId());
    }

    @Override
    public int updatePayStatus(String payWay, String id,String payJsOrderId) {
        Date payDate = DateUtil.getCurrentTime();
       return sealPayOrderMapper.updatePayStatus(payWay,id,payJsOrderId,payDate);
    }


    @Override
    public int cancelOrder(String id,WeChatUser weChatUser) {
        SealPayOrder sealPayOrder = sealPayOrderMapper.selectByPrimaryKey(id);
        if(sealPayOrder.getRefundStatus().equals("1")){
            if(sealPayOrder.getIsPay()){
                int result = sealPayOrderMapper.updateRefundStatus("2",id);
                int sealResult = sealService.cancelSeal(sealPayOrder.getSealId(),weChatUser);
                if(result>0&&sealResult>0){
                    return ResultUtil.refundOrderOk;
                }else {
                    return ResultUtil.orderError;
                }
            }else {
                int result = sealPayOrderMapper.updateRefundStatus("3",id);
                int sealResult = sealService.cancelSeal(sealPayOrder.getSealId(),weChatUser);
                boolean refundResult = refundOrderToPayJs(sealPayOrder.getPayJsOrderId());
                if(result>0&&sealResult>0&&refundResult){
                    return ResultUtil.cancelOrderOk;
                }else {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return ResultUtil.orderError;
                }
            }
        }else {
            return ResultUtil.cancelOrederFail;
        }
    }

    public boolean refundOrderToPayJs(String payJsOrderId){
        try {
            HttpPost httpPost = new HttpPost("https://payjs.cn/api/refund");
            CloseableHttpClient client = HttpClients.createDefault();
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("payjs_order_id",payJsOrderId);
            jsonParam.put("sign",null);
            StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            HttpResponse resp = client.execute(httpPost);
            if (resp.getStatusLine().getStatusCode() == 200) {
                HttpEntity httpEntity = resp.getEntity();
                String respContent = EntityUtils.toString(httpEntity, "UTF-8");
                JSONObject jsonObject = JSONObject.parseObject(respContent);
                String returnCode = (String) jsonObject.get("return_code");
                if(returnCode.equals("1")){
                    return true;
                }else {
                    return false;
                }
            }else {
                return false;
            }
        }catch (Exception e) {
            return false;
        }
    }

}
