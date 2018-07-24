package com.dhht.service.Log;


import com.dhht.model.SysLog;
import com.github.pagehelper.PageInfo;
import java.util.Map;

public interface LogService {
    PageInfo<SysLog> selectAllLog(int pageNum, int pageSize);

    PageInfo<SysLog> findLog(Map map);
}
