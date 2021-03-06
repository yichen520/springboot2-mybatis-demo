package com.dhht.dao;

import com.dhht.model.SysLog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface LogDao {
    //添加日志
    int saveLog(SysLog log);

    //获取日志
    List<SysLog> selectAllLog();

    //模糊查询
    List<SysLog> find(@Param("type") String type, @Param("result") String result,@Param("name") String name ,@Param("ip") String ip, @Param("startTime") String startTime ,@Param("endTime") String endTime);
 }
