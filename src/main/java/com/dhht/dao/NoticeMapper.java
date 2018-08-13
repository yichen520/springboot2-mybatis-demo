package com.dhht.dao;

import com.dhht.model.Notice;

import java.util.List;

import com.dhht.model.NoticeSimple;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeMapper {
    int insert(Notice notice);

    List<Notice> selectByUserName(@Param("userName") String userName);

    int deleteById (@Param("id") String id);

    Notice selectById(@Param("id") String id);

    int update(Notice notice);

    List<NoticeSimple> selectNoticeByNum(@Param("cityId") String cityId, @Param("provinceId") String provinceId);

    List<NoticeSimple> selectNoticeList( @Param("cityId") String cityId, @Param("provinceId") String provinceId);

    Notice selectNoticeDetail(@Param("id") String id);

}