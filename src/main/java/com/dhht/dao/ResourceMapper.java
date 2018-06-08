package com.dhht.dao;

import com.dhht.model.Resource;

import java.util.List;

public interface ResourceMapper {

    int deleteByPrimaryKey(String id);

    int insert(Resource record);

    int insertSelective(Resource record);

    Resource selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Resource record);

    int updateByPrimaryKey(Resource record);

    List<Resource> selectByParentID(String parentId);


}