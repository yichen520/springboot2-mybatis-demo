package com.dhht.dao;

import com.dhht.model.Courier;
import org.springframework.stereotype.Repository;

@Repository
public interface CourierMapper {
    int deleteByPrimaryKey(String id);

    int insert(Courier record);

    int insertSelective(Courier record);

    Courier selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Courier record);

    int updateByPrimaryKey(Courier record);
}