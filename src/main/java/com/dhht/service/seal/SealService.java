package com.dhht.service.seal;

import com.dhht.common.JsonObjectBO;
import com.dhht.model.*;
import com.dhht.model.pojo.*;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

public interface SealService {

   UseDepartment isrecord(String useDepartmentCode);

   int insert(Seal seal);

   //印章申请
   int sealRecord(List<Seal> seals, User user,String useDepartmentCode, String districtId, String agentTelphone,
                  String agentName, String certificateNo, String certificateType,
                  String agentPhotoId, String idcardFrontId, String idcardReverseId,  String proxyId,String idCardPhotoId,int confidence,
                  String fieldPhotoId,String entryType,String captcha);

   //印章主界面
   PageInfo<Seal> sealInfo( User user,String useDepartmentName, String useDepartmentCode, String status, int pageNum, int pageSize,String sealType,String recordDepartmentName,String sealCode);

   //上传
   int sealUpload(User user ,String id,  String sealedCardId, String imageDataId);

   //印章个人化
   int sealPersonal(String id,User user);

   //印章交付
   int deliver(User user,String id,String useDepartmentCode,String proxyId,String name,
                   String certificateType,String certificateNo,String agentTelphone,String agentPhotoId,String idcardFrontId,String idcardReverseId,
                   String entryType,int confidence,String fieldPhotoId,String idCardPhotoId);

   //印章挂失
   int loss (User user,String id,String name, String agentPhotoId,  String proxyId ,String certificateNo,String certificateType,
             String localDistrictId,String businesslicenseId,String idcardFrontId,String idcardReverseId,String agentTelphone,String entryType,String idCardPhotoId,
             int confidence,String fieldPhotoId);

   //印章注销
   int logout (User user,String id,String name, String agentPhotoId,  String proxyId ,String certificateNo,String certificateType,
               String localDistrictId,String businesslicenseId,String idcardFrontId,String idcardReverseId,String agentTelphone,String entryType,String idCardPhotoId,
               int confidence,String fieldPhotoId);

   //详情查看
   SealVO selectDetailById(String id);

   PageInfo<Seal> seal( User user,String useDepartmentName, String useDepartmentCode, String status, int pageNum, int pageSize);

   // 人证合一
   FaceCompareResult faceCompare(String idCardId, String fieldId);

   PageInfo<Seal> Infoseal( User user,String useDepartmentName, String useDepartmentCode, String status, int pageNum, int pageSize);

   //图片下载
   FileInfoVO download(String id);

   //是否是法人
   boolean isLegalPerson(String certificateNo,String name,String useDepartmentCode);

   //查找最新的印章信息
   Seal selectLastSeal();


   //制作单位查询
   PageInfo<Seal> mdSeal(User user, String useDepartmentName, String useDepartmentCode, String status, int pageNum, int pageSize);

   //印章核验
   int verifySeal(User user,String id,String rejectReason,String rejectRemark,String verify_type_name);

   //待承接的印章信息
   List<Seal> waitUndertake(String MakedepartmentCode);

   int newsealRecord(User user, String sealId);

   //印章备案信息查询
   List<Seal> selectSealByDistrictId(String districtId);

   //承接
   int underTake(User user, String sealId);


    List<Seal> portalSealInfo( String useDepartmentName, String useDepartmentCode, String status, String sealType, String recordDepartmentName, String sealCode);


    int checkSealCode(String sealCode,String useDepartmentCode,String sealTypeCode);

   //承接中的退回
   int makeDepartmentUntread(String sealId, SealVerification sealVerification);

   //印章取消
   int cencalSeal(String sealId);





}

