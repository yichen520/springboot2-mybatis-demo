package com.dhht.dao;

import com.dhht.model.SealPayOrder;
import org.springframework.stereotype.Repository;

@Repository
public interface SealPayOrderMapper {
    int deleteByPrimaryKey(String id);

    int insert(SealPayOrder record);

    int insertSelective(SealPayOrder record);

    SealPayOrder selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SealPayOrder record);

    int updateByPrimaryKey(SealPayOrder record);
}