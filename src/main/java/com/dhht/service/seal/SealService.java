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
   PageInfo<Seal> sealInfo( String useDepartmentName, String useDepartmentCode, String status, int pageNum, int pageSize);

   //印模上传
   int sealUpload(User user ,Seal seal, String electronicSealURL, String sealScannerURL);

   //印章个人化
   int sealPersonal(Seal seal,User user);

   //印章交付
   boolean deliver(User user,Seal  seal,SealGetPerson sealGetPerson);

   //印章挂失
   int loss (User user,Seal seal, String operatorPhoto,  String proxy ,String businessScanner,SealOperationRecord sealOperationRecord,String recordCode);

   //印章注销
   int logout (User user,Seal seal, String operatorPhoto,  String proxy ,String businessScanner,SealOperationRecord sealOperationRecord);

}

