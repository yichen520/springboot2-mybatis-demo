package com.dhht.dao;

import com.dhht.model.WeChatUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WeChatUserMapper {
    int deleteByPrimaryKey(String id);

    int insert(WeChatUser record);

    int insertSelective(WeChatUser record);

    WeChatUser selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(WeChatUser record);

    int updateByPrimaryKey(WeChatUser record);

    WeChatUser selectByTelPhone(@Param("telPhone") String telPhone);
}