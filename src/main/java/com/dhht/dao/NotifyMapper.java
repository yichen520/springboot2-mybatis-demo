package com.dhht.dao;

import com.dhht.model.Notify;

import java.util.Date;
import java.util.List;

import com.dhht.model.NotifyReceiveDetail;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotifyMapper {

    int deleteById(@Param("id") String id);

    int insert(Notify notify);

    Notify selectByPrimaryKey(String id);

    int updateByPrimaryKey(Notify record);

    int recallNotify(@Param("id") String id, @Param("recallTime") Date recallTime,@Param("recallResult") String recallResult);

    List<Notify> selectNotifyDetail(List<NotifyReceiveDetail> notifyIds);

    List<Notify> selectNotifyBySendUser(@Param("userName") String userName);
}