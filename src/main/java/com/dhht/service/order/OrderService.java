package com.dhht.service.order;

import com.dhht.model.SealOrder;
import com.dhht.model.SealPayOrder;

import java.util.List;

public interface OrderService {
    List<SealOrder> selectOrder(String type, String telphone);

    SealOrder selectOrderDetail(String sealId);

    int updatePay(SealPayOrder sealPayOrder);
}
