package com.dhht.service.order.Impl;

import com.dhht.dao.CourierMapper;
import com.dhht.dao.RecipientsMapper;
import com.dhht.dao.SealDao;
import com.dhht.dao.SealPayOrderMapper;
import com.dhht.model.*;
import com.dhht.service.order.OrderService;
import com.dhht.util.DateUtil;
import com.dhht.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
       return sealPayOrderMapper.updatePayStatus(payWay,id,payJsOrderId);
    }


    @Override
    public int cancelOrder(String id) {
        SealPayOrder sealPayOrder = sealPayOrderMapper.selectByPrimaryKey(id);
        if(sealPayOrder.getRefundStatus().equals("1")){
            if(sealPayOrder.getIsPay()){
                int result = sealPayOrderMapper.updateRefundStatus("2",id);
                if(result>0){
                    return ResultUtil.refundOrderOk;
                }else {
                    return ResultUtil.orderError;
                }
            }else {
                int result = sealPayOrderMapper.updateRefundStatus("3",id);
                if(result>0){
                    return ResultUtil.cancelOrderOk;
                }else {
                    return ResultUtil.orderError;
                }
            }
        }else {
            return ResultUtil.cancelOrederFail;
        }
    }


}
