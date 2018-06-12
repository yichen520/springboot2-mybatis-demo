package com.dhht.dao;

import com.dhht.model.Log;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface LogDao {
    //添加日志
    int saveLog(Log log);

    //获取日志
    List<Log> selectAllLog();
}
