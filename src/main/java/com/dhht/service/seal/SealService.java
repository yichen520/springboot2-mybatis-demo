package com.dhht.service.seal;

import com.dhht.common.JsonObjectBO;
import com.dhht.model.*;
import com.dhht.model.pojo.SealVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface SealService {

   UseDepartment isrecord(String useDepartmentCode);

   int insert(Seal seal);

   //印章备案
   int sealRecord(Seal seal, User user, String districtId, String operatorTelphone, String operatorName, String operatorCertificateCode, String operatorCertificateType, String operatorPhoto,String PositiveIdCardScanner, String ReverseIdCardScanner, String proxy);

   //印章主界面
   PageInfo<Seal> sealInfo( User user,String useDepartmentName, String useDepartmentCode, String status, int pageNum, int pageSize);

   //印模上传
   int sealUpload(User user ,String id, String electronicSealURL, String sealScannerURL);

   //印章个人化
   int sealPersonal(String id,User user);

   //印章交付
   boolean deliver(User user,String id,SealGetPerson sealGetPerson,String proxy,String operatorPhoto, String PositiveIdCardScanner, String ReverseIdCardScanner);

   //印章挂失
   int loss (User user,String id, String operatorPhoto,  String proxy ,String businessScanner,SealOperationRecord sealOperationRecord,String localDistrictId);

   //印章注销
   int logout (User user,String id, String operatorPhoto,  String proxy ,String businessScanner,SealOperationRecord sealOperationRecord);

   //详情查看
   SealVo selectDetailById(String id);

   PageInfo<Seal> seal( User user,String useDepartmentName, String useDepartmentCode, String status, int pageNum, int pageSize);

   // 人证合一
   FaceCompareResult checkface(String fileAURL, String fileBURl);

   public PageInfo<Seal> Infoseal( User user,String useDepartmentName, String useDepartmentCode, String status, int pageNum, int pageSize);

}

