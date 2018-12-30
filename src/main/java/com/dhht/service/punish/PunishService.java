package com.dhht.service.punish;

import com.dhht.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/19.
 */
public interface PunishService {

    boolean insertPunish(User user, MakePunishRecord makePunishRecord);

    boolean insertEmployeePunish(User user, EmployeePunishRecord employeePunish);

    List<MakePunishRecord>  findPunish(String makedepartmentName, String startTime, String endTime, String districtId);

    List<EmployeePunishRecord>  findEmployeePunish(String makedepartmentName, String startTime, String endTime, String districtId);

    boolean sendcode(String phoneNumber, int templateId, ArrayList<String> params);

    boolean sendcode1(String phoneNumber, int templateId, ArrayList<String> params);
}
