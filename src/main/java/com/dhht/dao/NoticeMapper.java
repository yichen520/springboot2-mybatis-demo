package com.dhht.dao;

import com.dhht.model.Notice;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeMapper {
    int insert(Notice notice);

    List<Notice> selectByUserName(@Param("userName") String userName);

    int delete (@Param("id") String id);

    Notice selectById(@Param("id") String id);

    int update(Notice notice);

    List<Notice> selectNoticeByNum(@Param("pageNum") Integer pageNum,@Param("districtId") String districtId);

}