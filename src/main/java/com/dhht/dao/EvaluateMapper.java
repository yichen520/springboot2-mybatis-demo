package com.dhht.dao;

import com.dhht.model.Evaluate;

import java.util.List;

public interface EvaluateMapper {
    int deleteByPrimaryKey(String id);

    int insert(Evaluate record);

    int insertSelective(Evaluate record);

    Evaluate selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Evaluate record);

    int updateByPrimaryKey(Evaluate record);
    List<Evaluate> selectEvaluateList(Evaluate evaluate);
}