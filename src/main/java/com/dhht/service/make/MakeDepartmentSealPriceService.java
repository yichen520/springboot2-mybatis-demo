package com.dhht.service.make;

import com.dhht.model.MakeDepartmentSealPrice;
import com.dhht.model.User;

import java.util.List;

public interface MakeDepartmentSealPriceService {

    int insertSealPriceRecord(List<MakeDepartmentSealPrice> makeDepartmentSealPrices, User user);

    int updateRecordsByIds(List<MakeDepartmentSealPrice> makeDepartmentSealPrices,User user);

    int updateById(MakeDepartmentSealPrice makeDepartmentSealPrice,User user);

    int deleteByUser(User user);

    int deleteByMakeDepartFlag(String flag);

    List<MakeDepartmentSealPrice> selectByUser(User user);

    List<MakeDepartmentSealPrice> selectByMakeDepartmentFlag(String flag);

}
