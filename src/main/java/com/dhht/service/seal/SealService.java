package com.dhht.service.seal;

import com.dhht.model.*;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface SealService {

   UseDepartment isrecord(String useDepartmentCode);

   int insert(Seal seal);

   //印章备案
   int sealRecord(Seal seal, User user, String districtId, String operatorTelphone, String operatorName, String operatorCertificateCode, String operatorCrtificateType, String operatorPhoto, String idCardScanner, String proxy);

   //印章主界面
   PageInfo<Seal> sealInfo(String recordCode, String useDepartmentName, String useDepartmentCode, String status, int pageNum, int pageSize);

   //印模上传
   int sealUpload(Seal seal,File sealData,String sealScanner);

   //印章个人化
   int sealPersonal(Seal seal);

}

