package com.dhht.dao;

import com.dhht.model.PunishLog;
import org.springframework.stereotype.Repository;

@Repository
public interface PunishLogMapper {
    int deleteByPrimaryKey(String id);

    int insert(PunishLog record);

    int insertSelective(PunishLog record);

    PunishLog selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(PunishLog record);

    int updateByPrimaryKey(PunishLog record);
}