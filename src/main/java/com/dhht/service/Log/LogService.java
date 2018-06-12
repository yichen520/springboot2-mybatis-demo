package com.dhht.service.Log;

import com.dhht.model.Log;
import com.dhht.model.Users;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface LogService {
    PageInfo<Log> selectAllLog(int pageNum,int pageSize);
    int insertLog(int logName, Users users,boolean result, String content);
}
