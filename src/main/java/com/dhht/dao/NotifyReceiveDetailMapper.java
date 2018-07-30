package com.dhht.dao;

import com.dhht.model.NotifyReceiveDetail;

import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotifyReceiveDetailMapper {


    int deleteByNotifyId(@Param("notifyId") String notifyId);

    int insert(NotifyReceiveDetail notifyReceiveDetail);

    int countNewNotify(@Param("receiveUserId") String receiveUserId);

    NotifyReceiveDetail selectByPrimaryKey(String id);

    int updateByPrimaryKey(NotifyReceiveDetail record);

    List<NotifyReceiveDetail> selectNotifyIdByUserId(@Param("receiveUserId") String receiveUserId);

    int isRead(@Param("readTime") Date time,@Param("receiveUserId") String receiveUserId);

    Integer countReadById(@Param("nofityId") String nofityId);

    Integer countAllById(@Param("nofityId") String nofityId);
}