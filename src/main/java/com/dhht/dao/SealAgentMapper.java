package com.dhht.dao;

import com.dhht.model.SealAgent;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface SealAgentMapper {
    int deleteByPrimaryKey(String id);

    int insert(SealAgent record);

    int insertSelective(SealAgent record);

    SealAgent selectByPrimaryKey(String id);

    SealAgent selectSealAgentByGetterId(@Param("getterId") String getterId);

    int updateByPrimaryKeySelective(SealAgent record);

    int updateByPrimaryKey(SealAgent record);

}