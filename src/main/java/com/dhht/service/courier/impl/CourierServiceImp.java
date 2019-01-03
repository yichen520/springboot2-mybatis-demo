package com.dhht.service.courier.impl;

import com.dhht.dao.CourierMapper;
import com.dhht.model.Courier;
import com.dhht.model.User;
import com.dhht.service.courier.CourierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "courierService")
@Transactional
public class CourierServiceImp implements CourierService {

    @Autowired
    private CourierMapper courierMapper;


    @Override
    public int insertCourier(Courier courier, User user) {
        return 0;
    }
}
