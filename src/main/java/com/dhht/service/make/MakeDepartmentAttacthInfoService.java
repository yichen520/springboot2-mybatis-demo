package com.dhht.service.make;

import com.dhht.model.MakeDepartmentAttachInfo;
import com.dhht.model.User;

public interface MakeDepartmentAttacthInfoService {

    int insert(MakeDepartmentAttachInfo makeDepartmentAttachInfo, User user);

    int updateById(MakeDepartmentAttachInfo makeDepartmentAttachInfo,User user);

    int deleteByMakeDepartmentFlag(String makeDepartmentFlag);

    MakeDepartmentAttachInfo selectById(String id);

    MakeDepartmentAttachInfo selectByMakeDepartmentFlag(User user);
}
