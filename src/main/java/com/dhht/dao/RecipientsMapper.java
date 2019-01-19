package com.dhht.dao;


import com.dhht.model.Recipients;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipientsMapper {
    int deleteByPrimaryKey(String id);

    int insert(Recipients record);

    int insertSelective(Recipients record);

    Recipients selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Recipients record);

    int updateByPrimaryKey(Recipients record);

    List<Recipients> selectAllByTelphone(@Param("loginTelphone")  String loginTelphone);

}