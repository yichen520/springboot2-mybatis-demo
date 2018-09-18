package com.dhht.service.make;


import com.dhht.model.Incidence;
import com.dhht.model.User;
import com.dhht.model.pojo.IncidencePO;

import java.util.List;

public interface MakeDepartmentIncidenceService {

    int insertincidence(Incidence incidence, User user);

    int updateincidence(Incidence incidence, User user);

    int deleteincidence(String id);

    List<Incidence> selectInfo(IncidencePO map);

   String  selectMaxSerialCode();
}
