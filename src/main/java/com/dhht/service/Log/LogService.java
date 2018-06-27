package com.dhht.service.Log;

import com.dhht.common.JsonObjectBO;
import com.dhht.model.SysLog;
import com.github.pagehelper.PageInfo;
import org.omg.CORBA.Object;

public interface LogService {
    PageInfo<SysLog> selectAllLog(int pageNum, int pageSize);

    PageInfo<SysLog> findLog(String start, String end, int pageNum, int pageSize);
}
