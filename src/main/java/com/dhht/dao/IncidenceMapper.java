package com.dhht.dao;

import com.dhht.model.Incidence;
import com.dhht.model.pojo.IncidencePO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncidenceMapper {
    int deleteByPrimaryKey(String id);

    int insert(Incidence record);

    int insertSelective(Incidence record);

    Incidence selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Incidence record);

    int updateByPrimaryKey(Incidence record);

    List<Incidence> selectInfo(IncidencePO map);

    String selectMaxSerialCode();
}