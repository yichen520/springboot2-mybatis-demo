package com.dhht.dao;

import com.dhht.model.SealAgent;
import org.springframework.stereotype.Repository;


@Repository
public interface SealAgentMapper {
    int deleteByPrimaryKey(String id);

    int insert(SealAgent record);

    int insertSelective(SealAgent record);

    SealAgent selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SealAgent record);

    int updateByPrimaryKey(SealAgent record);

}