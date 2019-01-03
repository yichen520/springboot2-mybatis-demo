package com.dhht.service.courier.impl;

import com.dhht.dao.CourierMapper;
import com.dhht.model.Courier;
import com.dhht.model.User;
import com.dhht.service.courier.CourierService;
import com.dhht.util.ResultUtil;
import com.dhht.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(value = "courierService")
@Transactional
public class CourierServiceImp implements CourierService {

    @Autowired
    private CourierMapper courierMapper;


    @Override
    public int insertCourier(Courier courier) {
        courier.setId(UUIDUtil.generate());
        int insertCourier = courierMapper.insertSelective(courier);
        if(insertCourier<0){
            return ResultUtil.isFail;
        }else {
            return ResultUtil.isSuccess;
        }
    }

    @Override
    public List<Courier> courierList(String recipientsId) {
        List<Courier> couriers = courierMapper.selectByRecipients(recipientsId);
        return couriers;
    }


}
