package com.dhht.dao;

import com.dhht.model.ChipGrant;
import org.springframework.stereotype.Repository;

@Repository
public interface ChipGrantMapper {
    int deleteByPrimaryKey(String id);

    int insert(ChipGrant record);

    int insertSelective(ChipGrant record);

    ChipGrant selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ChipGrant record);

    int updateByPrimaryKey(ChipGrant record);
}