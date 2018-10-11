package com.dhht.service.seal;

import com.dhht.model.SealCode;

import java.util.List;

public interface SealCodeService {
    List<SealCode> selectRecordDepartmentCode();

    String selectSealCode(String code);


}
