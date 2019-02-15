package com.dhht.service.order;

import com.dhht.model.SealOrder;
import com.dhht.model.SealPayOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderService {

    int insertOrder(SealPayOrder sealPayOrder);

    List<SealOrder> selectOrder(String type, String telphone);

    SealOrder selectOrderDetail(String sealId);

    int updateEvaluationStatus(String sealId);

    int updateRefundStatus ( String refundStatus,String sealId);

    int updatePayStatus(String payWay,String sealId);

    int cancelOrder(String id);
}