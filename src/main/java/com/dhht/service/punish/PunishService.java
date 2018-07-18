package com.dhht.service.punish;

import com.dhht.model.*;

import java.util.List;

/**
 * Created by Administrator on 2018/4/19.
 */
public interface PunishService {

    boolean insertPunish(User user, MakePunishRecord makePunishRecord);

    List<MakePunishRecord>  findPunish(String makedepartmentName, String startTime, String endTime, String districtId);
}
