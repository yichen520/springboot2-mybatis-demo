package com.dhht.service.seal;

import com.dhht.common.JsonObjectBO;
import com.dhht.model.*;
import com.dhht.model.pojo.FileInfoVO;
import com.dhht.model.pojo.SealVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface SealService {

   UseDepartment isrecord(String useDepartmentCode);

   int insert(Seal seal);

   //印章备案
   int sealRecord(List<Seal> seals, User user,String useDepartmentCode, String districtId, String agentTelphone,
                  String agentName, String certificateNo, String certificateType,
                  String agentPhotoId, String idcardFrontId, String idcardReverseId,  String proxyId,String idCardPhotoId,int confidence,
                  String fieldPhotoId,String entryType);

   //印章主界面
   PageInfo<Seal> sealInfo( User user,String useDepartmentName, String useDepartmentCode, String status, int pageNum, int pageSize);

   //印模上传
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

   //挂失和注销的经办人信息
   SealVO lossAndLogoutDetail(String id);

   //制作单位查询
   PageInfo<Seal> mdSeal(User user, String useDepartmentName, String useDepartmentCode, String status, int pageNum, int pageSize);

   //印章核验
   int verifySeal(String id,String rejectReason,String rejectRemark,String verify_type_name);

}

