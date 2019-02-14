package com.dhht.model.pojo;

import com.dhht.model.*;
import lombok.Data;

import java.util.List;

@Data
public class SealVerificationPO {
    private Seal seal;
    private SealVerification sealVerification;
    private List<SealAgent> sealAgent;
    private UseDepartment useDepartment;
    private MakeDepartmentSimple makeDepartmentSimple;
    private RecordDepartment recordDepartment;
}
