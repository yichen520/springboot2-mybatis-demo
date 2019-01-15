package com.dhht.service.suspicious;


import com.dhht.model.Incidence;
import com.dhht.model.Suspicious;
import com.dhht.model.User;
import com.dhht.model.WeChatUser;
import com.dhht.model.pojo.IncidencePO;
import com.dhht.model.pojo.SuspiciousPO;

import java.util.List;

public interface SuspiciousService {

    int insertsuspicious(Suspicious suspicious, User user);

    int updatesuspicious(Suspicious suspicious, User user);

    int deletesuspicious(String id);

    List<Suspicious> selectInfo(SuspiciousPO map);

    int weChatInsertSuspicious(Suspicious suspicious, WeChatUser user,String makeDepartmentCode);

    List<Suspicious> selectAll(String loginTelphone);

    Suspicious selectById(String id);

}
