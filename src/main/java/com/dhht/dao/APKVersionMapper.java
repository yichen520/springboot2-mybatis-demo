package com.dhht.dao;

import com.dhht.model.APKVersion;
import com.dhht.model.APKVersionKey;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface APKVersionMapper {
    int deleteByPrimaryKey(APKVersionKey key);

    int insert(APKVersion record);

    int insertSelective(APKVersion record);

    APKVersion selectByPrimaryKey(APKVersionKey key);

    int updateByPrimaryKeySelective(APKVersion record);

    int updateByPrimaryKey(APKVersion record);

    APKVersion selectNewVersion();

    List<APKVersion> getAllApk();
}