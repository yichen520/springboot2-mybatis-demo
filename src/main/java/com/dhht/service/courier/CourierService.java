package com.dhht.service.courier;

import com.dhht.model.Courier;
import com.dhht.model.User;

import java.util.List;

public interface CourierService {

    //添加
    int insertCourier(Courier courier);

    //查询
    List<Courier> courierList(String recipients);

}
