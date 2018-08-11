package com.dhht.dao;

import com.dhht.model.ChipApply;

public interface ChipApplyMapper {
    int deleteByPrimaryKey(String id);

    int insert(ChipApply record);

    int insertSelective(ChipApply record);

    ChipApply selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ChipApply record);

    int updateByPrimaryKey(ChipApply record);
}