package com.dhht.dao;

import com.dhht.model.Evaluate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluateMapper {
    int deleteByPrimaryKey(String id);

    int insert(Evaluate record);

    int insertSelective(Evaluate record);

    Evaluate selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Evaluate record);

    int updateByPrimaryKey(Evaluate record);
    List<Evaluate> selectEvaluateList(Evaluate evaluate);
}