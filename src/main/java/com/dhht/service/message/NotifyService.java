package com.dhht.service.message;

import com.dhht.model.Notify;
import com.dhht.model.User;

import java.util.List;
import java.util.Map;

public interface NotifyService {
    int insertNotify(Notify notify, User user);

    int recallNotify(String id,String result);

    int countNewNotify(String receiveUserId);

    List<Notify> selectNotifyDetail(String receiveUserId);

    List<Notify> selectNotifyBySendUser(String userName);

    String notifyReadCount(String notifyId);

}
