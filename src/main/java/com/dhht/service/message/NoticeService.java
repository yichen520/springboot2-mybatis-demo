package com.dhht.service.message;

import com.dhht.model.Notice;
import com.dhht.model.NoticeSimple;
import com.dhht.model.User;

import java.util.List;
import java.util.Map;

public interface NoticeService {
    int insert(Notice notice, User user);

    List<Notice> selectByUserName(String userName);

    int delete(String id);

    int update(Map map);

    List<NoticeSimple> selectByNum(String district);

    List<NoticeSimple> selectNoticeList(String district);

    Notice selectNoticeDetail(String id);
}
