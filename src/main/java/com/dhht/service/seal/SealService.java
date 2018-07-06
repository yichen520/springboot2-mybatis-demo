package com.dhht.service.seal;

import com.dhht.model.*;

import java.util.List;

public interface SealService {

   UseDepartment isrecord(String useDepartmentCode);

   int insert(Seal seal, Employee employee, String districtId, String operatorTelphone, String operatorName, String operatorCertificateCode, int operatorCrtificateType);

   int sealRecord(Seal seal);

}
