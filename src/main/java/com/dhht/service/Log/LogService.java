package com.dhht.service.Log;

import com.dhht.model.SysLog;
import com.dhht.model.Users;
import com.github.pagehelper.PageInfo;

public interface LogService {
    PageInfo<SysLog> selectAllLog(int pageNum, int pageSize);
}
