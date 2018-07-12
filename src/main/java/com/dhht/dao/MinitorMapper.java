package com.dhht.dao;

import com.dhht.model.Minitor;
import com.dhht.model.MinitorExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MinitorMapper {
    int countByExample(MinitorExample example);

    int deleteByExample(MinitorExample example);

    int deleteByPrimaryKey(String id);

    int insert(Minitor record);

    int insertSelective(Minitor record);

    List<Minitor> selectByMinitor(@Param("minitor")Integer minitor);

    List<Minitor> selectByExample(MinitorExample example);

    Minitor selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Minitor record, @Param("example") MinitorExample example);

    int updateByExample(@Param("record") Minitor record, @Param("example") MinitorExample example);

    int updateByPrimaryKeySelective(Minitor record);

    int updateByPrimaryKey(Minitor record);
}