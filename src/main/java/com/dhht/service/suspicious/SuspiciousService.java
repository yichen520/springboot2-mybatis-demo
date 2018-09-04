package com.dhht.service.suspicious;


import com.dhht.model.Incidence;
import com.dhht.model.Suspicious;
import com.dhht.model.User;
import com.dhht.model.pojo.IncidencePO;
import com.dhht.model.pojo.SuspiciousPO;

import java.util.List;

public interface SuspiciousService {

    int insertsuspicious(Suspicious suspicious, User user);

    int updatesuspicious(Suspicious suspicious, User user);

    int deletesuspicious(String id);

    List<Suspicious> selectInfo(SuspiciousPO map);

}
