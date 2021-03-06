package com.dhht.service.message;

import com.dhht.model.Notify;
import com.dhht.model.User;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

public interface NotifyService {
    int insertNotify(Notify notify, User user);

    int recallNotify(String id,String result);

    int countNewNotify(String receiveUserId);

    PageInfo<Notify> selectNotifyDetail(String receiveUserId,int pageNum,int pageSize);

    List<Notify> selectNotifyBySendUser(String userName);

    String notifyReadCount(String notifyId);

}
