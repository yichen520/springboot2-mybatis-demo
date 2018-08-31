package com.dhht.dao;

import com.dhht.model.SMSCode;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


/**
 * Created by Administrator on 2018/4/19.
 */
@Repository
public interface SMSCodeDao {
    SMSCode getSMSCodeByPhone(@Param("phone")String phone);
    void save(SMSCode sMSCode);
    void update(SMSCode sMSCode);
    SMSCode getSms(@Param("phone")String phone);

}