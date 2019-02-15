package com.dhht.service.order;

import com.dhht.model.SealOrder;

import java.util.List;

public interface OrderService {
    List<SealOrder> selectOrder(String type, String telphone);

    SealOrder selectOrderDetail(String sealId);
}
