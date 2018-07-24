package com.dhht.service.Log.Impl;

import com.alibaba.fastjson.JSONObject;
import com.dhht.common.JsonObjectBO;
import com.dhht.dao.LogDao;
import com.dhht.model.Dictionary;
import com.dhht.model.SysLog;
import com.dhht.model.User;
import com.dhht.service.Log.LogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(value = "LogService")
@Transactional
public class LogServiceImp implements LogService {
    @Autowired
    private LogDao logDao;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
    @Override
    public PageInfo<SysLog> selectAllLog(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<SysLog> logs = logDao.selectAllLog();
        PageInfo result = new PageInfo(logs);
        return result;
    }

    /**
     * log根据时间模糊查询
     * @return
     */
    @Override
    public PageInfo<SysLog> findLog(Map map) {
        Integer pageSize = (Integer) map.get("pageSize");
        Integer pageNum = (Integer) map.get("pageNum");
        PageHelper.startPage(pageNum, pageSize);

        String type = (String) map.get("type");
        String result = (String) map.get("result");
        String name = (String) map.get("name");
        String ip = (String) map.get("ip");
        String startTime = (String) map.get("startTime");
        String endTime = (String) map.get("endTime");


        List<SysLog> list = logDao.find(type,result,name,ip,startTime,endTime);
        PageInfo<SysLog> sysLogPageInfo = new PageInfo<>(list);
        return sysLogPageInfo;
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
