package com.dhht.dao;

import com.dhht.model.Suspicious;
import com.dhht.model.pojo.SuspiciousPO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SuspiciousMapper {
    int deleteByPrimaryKey(String id);

    int insert(Suspicious record);

    int insertSelective(Suspicious record);

    Suspicious selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Suspicious record);

    int updateByPrimaryKey(Suspicious record);

    List<Suspicious> selectInfo(SuspiciousPO map);

    List<Suspicious> selectAll(String loginTelphone);
}