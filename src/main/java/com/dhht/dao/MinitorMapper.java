package com.dhht.dao;

import com.dhht.model.Examine;
import com.dhht.model.ExamineDetail;
import com.dhht.model.MinitorExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MinitorMapper {
    int countByExample(MinitorExample example);

    int deleteByExample(MinitorExample example);

    int deleteByPrimaryKey(String id);

    int insert(Examine record);

    int insertSelective(Examine record);

    List<Examine> selectByMinitor();

    List<Examine> selectByExample(MinitorExample example);

    Examine selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Examine record, @Param("example") MinitorExample example);

    int updateByExample(@Param("record") Examine record, @Param("example") MinitorExample example);

    int updateByPrimaryKeySelective(Examine record);

    int updateByPrimaryKey(Examine record);

   int insertExamineItem(ExamineDetail examineDetail);
}