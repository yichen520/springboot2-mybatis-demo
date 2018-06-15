package com.dhht.service.Log.Impl;

import com.dhht.dao.LogDao;
import com.dhht.model.Dictionary;
import com.dhht.model.SysLog;
import com.dhht.model.Users;
import com.dhht.service.Log.LogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
@Service(value = "LogService")
public class LogServiceImp implements LogService {
    @Autowired
    private LogDao logDao;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
    @Override
    public PageInfo<SysLog> selectAllLog(int pageNum, int pageSize) {
        List<SysLog> logs = logDao.selectAllLog();
        PageHelper.startPage(pageNum, pageSize);
        PageInfo<SysLog> result = new PageInfo(logs);
        return result;
    }

    @Override
    public int insertLog(int operateType, Users users,boolean result,String content) {
        SysLog log = new SysLog();
        log.setLogType(getNameByType(operateType));
        log.setLogUser(users.getRealName());
        if(result) {
            log.setLogResult("成功");
        }else {
            log.setLogResult("失败");
        }
        log.setLogTime(simpleDateFormat.format(new Date()).toString());
        log.setLogContent(content);
        return logDao.saveLog(log);
    }


    //根据操作类型获取操作名称
    public String getNameByType(int operateType){
        switch (operateType) {
            case 1: return Dictionary.GLY_OPERATION_1;
            case 2: return Dictionary.GLY_OPERATION_2;
            case 3: return Dictionary.GLY_OPERATION_3;
            case 4: return Dictionary.GLY_OPERATION_4;
            case 5: return Dictionary.GLY_OPERATION_5;
            case 6: return Dictionary.GLY_OPERATION_6;
            case 7: return Dictionary.GLY_OPERATION_7;
            case 8: return Dictionary.GLY_OPERATION_8;
            case 9: return Dictionary.GLY_OPERATION_9;
            case 10: return Dictionary.GLY_OPERATION_10;
            case 11: return Dictionary.GLY_OPERATION_11;
            case 12: return Dictionary.GLY_OPERATION_12;
            case 13: return Dictionary.GLY_OPERATION_13;
            case 14: return Dictionary.GLY_OPERATION_14;
            case 15: return Dictionary.GLY_OPERATION_15;
            case 16: return Dictionary.GLY_OPERATION_16;
            case 17: return Dictionary.GLY_OPERATION_17;
            default:
                return "";
        }
    }


}
