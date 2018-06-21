package com.dhht.service.seal;

import com.dhht.model.Menus;
import com.dhht.model.Resource;
import com.dhht.model.Seal;
import com.dhht.model.UseDepartment;

import java.util.List;

public interface SealService {

   UseDepartment isrecord(String useDepartmentCode);

   int insert(Seal seal);

}
