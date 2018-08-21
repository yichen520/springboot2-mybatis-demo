package com.dhht.service.seal.Impl;

import com.dhht.annotation.DeleteTempFile;
import com.dhht.annotation.Sync;
import com.dhht.common.ImageGenerate;
import com.dhht.dao.*;
import com.dhht.face.AFRTest;
import com.dhht.model.*;
import com.dhht.model.pojo.SealVO;

import com.dhht.service.employee.EmployeeService;
import com.dhht.service.make.MakeDepartmentService;
import com.dhht.service.recordDepartment.RecordDepartmentService;
import com.dhht.service.seal.SealService;
import com.dhht.service.tools.FileService;
import com.dhht.service.tools.FileStoreService;
import com.dhht.service.tools.impl.FileLocalStoreServiceImpl;
import com.dhht.sync.SyncDataType;
import com.dhht.sync.SyncOperateType;
import com.dhht.util.DateUtil;
import com.dhht.util.ResultUtil;
import com.dhht.util.StringUtil;
import com.dhht.util.UUIDUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@Service("sealService")
@Transactional
public class SealServiceImpl implements SealService {

    @Autowired
    private SealDao sealDao;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private SealAgentMapper sealAgentMapper;

    @Autowired
    private RecordDepartmentMapper recordDepartmentMapper;

    @Autowired
    private UseDepartmentDao useDepartmentDao;

    @Autowired
    private MakeDepartmentService makeDepartmentService;

    @Autowired
    private RecordDepartmentService recordDepartmentService;

    @Autowired
    private FaceCompareRecordMapper faceCompareRecordMapper;

    //相似度参数
    @Value("${face.similarity}")
    private float similarity  ;

    @Resource(name="localStoreFileService")
    private FileStoreService localStoreFileService;

    @Resource(name="fastDFSStoreServiceImpl")
    private FileStoreService fastDFSStoreServiceImpl;

    @Autowired
    private FileService fileService;


    @Override
    public UseDepartment isrecord(String useDepartmentCode) {
        return useDepartmentDao.selectByCode(useDepartmentCode);
    }





    /**
     * 6位简单密码
     *
     * @return
     */
    public static String createRandomCode(String districtId) {
        //印章编码
        String vcode = "";
        for (int i = 0; i < 8; i++) {
            vcode = vcode + (int) (Math.random() * 9);
        }
        String sealVcode = districtId + vcode;
        return sealVcode;
    }


    @Override
    public int insert(Seal seal) {
        //增加文件上传，操作记录和印章本身表
        return 1;
    }


        /**
         * 印章备案
         * @param seal
         * @param user
         * @param districtId
         * @param agentTelphone
         * @param agentName
         * @param certificateNo
         * @param certificateType
         * @param agentPhotoId
         * @param idcardFrontId
         * @param idcardReverseId
         * @param proxyId
         * @param idCardPhotoId
         * @param confidence
         * @param fieldPhotoId
         * @return
         */
        @Override
        public int sealRecord(Seal seal, User user, String districtId, String agentTelphone,
                              String agentName, String certificateNo, String certificateType,
                              String agentPhotoId, String idcardFrontId, String idcardReverseId,  String proxyId,String idCardPhotoId,int confidence,
                             String fieldPhotoId ) {

            String sealcode = createRandomCode(districtId);
            List<Seal> list = sealDao.selectByCodeAndType(seal.getUseDepartmentCode());
            if(seal.getSealTypeCode().equals("05")) {
                if (list.size() != 0) {
                    return ResultUtil.isHaveSeal;    //该公司的法务印章已经存在
                }
            }
            seal.setSealCode(sealcode);
            UseDepartment useDepartment = useDepartmentDao.selectByCode(seal.getUseDepartmentCode());  //根据usedepartment查询对应的使用公司
            if(useDepartment==null){
                return ResultUtil.isNoDepartment;
            }
            String telphone = user.getTelphone();
            Employee employee = employeeService.selectByPhone(telphone); //获取从业人员
            if(employee==null){
                return ResultUtil.isNoEmployee;
            }
            MakeDepartmentSimple makedepartment = makeDepartmentService.selectByDepartmentCode(employee.getEmployeeDepartmentCode()); //获取制作单位
            RecordDepartment recordDepartment = recordDepartmentMapper.selectBydistrict(districtId);//获取备案单位
           if(makedepartment==null ||recordDepartment==null){
               return ResultUtil.isFail;
           }

            //印章信息
            String sealId = UUIDUtil.generate();
            seal.setId(sealId);
            seal.setSealName(useDepartment.getName());
            seal.setSealStatusCode("04");
            seal.setIsRecord(true);
            seal.setRecordDate(DateUtil.getCurrentTime());
            seal.setIsMake(false);
            seal.setIsDeliver(false);
            seal.setIsLoss(false);
            seal.setIsPersonal(false);
            seal.setIsLogout(false);
            seal.setDistrictId(useDepartment.getDistrictId());
            seal.setMakeDepartmentCode(makedepartment.getDepartmentCode());
            seal.setMakeDepartmentName(makedepartment.getDepartmentName());
            seal.setRecordDepartmentCode(recordDepartment.getDepartmentCode());
            seal.setRecordDepartmentName(recordDepartment.getDepartmentName());
            seal.setIsRecord(true);
            seal.setRecordDate(DateUtil.getCurrentTime());


            //操作记录
            SealOperationRecord sealOperationRecord = new SealOperationRecord();
            sealOperationRecord.setId(UUIDUtil.generate());
            sealOperationRecord.setSealId(sealId);
            sealOperationRecord.setEmployeeId(employee.getEmployeeId());
            sealOperationRecord.setEmplyeeName(employee.getEmployeeName());
            sealOperationRecord.setEmployeeCode(employee.getEmployeeCode());
            sealOperationRecord.setOperateType("00");
            sealOperationRecord.setOperateTime(DateUtil.getCurrentTime());
            int sealOperationRecordInsert = sealDao.insertSealOperationRecord(sealOperationRecord); //保存操作记录

            //认证合一记录
            FaceCompareRecord faceCompareRecord = new FaceCompareRecord();
            String checkFace = UUIDUtil.generate();
            faceCompareRecord.setId(checkFace);
            faceCompareRecord.setCertificateNo(certificateNo);
            faceCompareRecord.setCertificatePhotoId(idCardPhotoId);
            faceCompareRecord.setConfidence(confidence);
            faceCompareRecord.setFacePhotoId(fieldPhotoId);
            faceCompareRecord.setFiledPhotoId(fieldPhotoId);
            faceCompareRecord.setName(agentName);
            faceCompareRecord.setRecordTime(DateUtil.getCurrentTime());
            int faceCompareRecordInsert = faceCompareRecordMapper.insert(faceCompareRecord);


            //经办人信息
            SealAgent sealAgent = new SealAgent();
            String saId = UUIDUtil.generate();
            sealAgent.setId(saId);
            sealAgent.setName(agentName);
            sealAgent.setTelphone(agentTelphone);
            sealAgent.setAgentPhotoId(agentPhotoId);
            sealAgent.setBusinessType("00");
            sealAgent.setCertificateNo(certificateNo);
            sealAgent.setCertificateType(certificateType);
            sealAgent.setIdcardFrontId(idcardFrontId);
            sealAgent.setIdcardReverseId(idcardReverseId);
            sealAgent.setFaceCompareRecordId(checkFace);
            if(proxyId!=null) {
                sealAgent.setProxyId(proxyId);
            }


            int sealAgentInsert = sealAgentMapper.insert(sealAgent);


            seal.setAgentId(saId);
            int sealInsert = sealDao.insert(seal);


            if(sealInsert>0&&sealOperationRecordInsert>0&&sealAgentInsert>0&&faceCompareRecordInsert>0){
                String sealType = "";
                switch (seal.getSealTypeCode()) {
                    case "01":
                        sealType = "单位专用章";
                        break;
                    case "02":
                        sealType = "财务专用章";
                        break;
                    case "03":
                        sealType = "税务专用章";
                        break;
                    case "04":
                        sealType = "合同专用章";
                        break;
                    case "05":
                        sealType = "法人代表人名章";
                        break;
                    case "06":
                        sealType = "其他类型印章";
                        break;
                }
                Map<String,String> map = new HashMap<>();
                map.put("useDepartment",seal.getUseDepartmentName());
                map.put("sealType",sealType);
                map.put("sealCode",sealcode);
                map.put("centerImage",seal.getSealCenterImage());
                String localPath = new ImageGenerate().seal(map);
                byte[] fileDate = new FileLocalStoreServiceImpl().readFile(localPath);
                FileInfo fileInfo = fileService.save(fileDate,DateUtil.getCurrentTime()+seal.getUseDepartmentName()+sealType,"png","",FileService.CREATE_TYPE_UPLOAD,user.getId(),user.getUserName());
                String moulageId=fileInfo.getId();
                SealMaterial sealMaterial = new SealMaterial();
                sealMaterial.setId(UUIDUtil.generate());
                sealMaterial.setSealCode(sealcode);
                sealMaterial.setType("04");
                sealMaterial.setFilePath(moulageId);
                int sealMaterialInsert = sealDao.insertSealMaterial(sealMaterial);
                if(sealMaterialInsert<0){
                    return ResultUtil.isError;
                }
                fileService.register(moulageId,"印模图像注册");
                fileService.register(idCardPhotoId,"认证合一身份证照片注册 ");
                fileService.register(fieldPhotoId,"认证合一现场照片注册");

                SyncEntity syncEntity =  ((SealServiceImpl) AopContext.currentProxy()).getSyncDate(sealOperationRecord, SyncDataType.SEAL, SyncOperateType.RECORD);
                SyncEntity syncEntity1 =  ((SealServiceImpl) AopContext.currentProxy()).getSyncDate(sealAgent, SyncDataType.SEAL, SyncOperateType.RECORD);
                SyncEntity syncEntity2 =  ((SealServiceImpl) AopContext.currentProxy()).getSyncDate(seal, SyncDataType.SEAL, SyncOperateType.RECORD);
                SyncEntity syncEntity3 =  ((SealServiceImpl) AopContext.currentProxy()).getSyncDate(faceCompareRecord, SyncDataType.SEAL, SyncOperateType.RECORD);
                SyncEntity syncEntity4 =  ((SealServiceImpl) AopContext.currentProxy()).getSyncDate(sealMaterial, SyncDataType.SEAL, SyncOperateType.RECORD);
                return ResultUtil.isSuccess;
            }else{
                return ResultUtil.isError;
            }


        }


    /**
     * 印章主页面
     *
     * @param useDepartmentName
     * @param useDepartmentCode
     * @param status
     * @return
     */
    @Override
    public PageInfo<Seal> sealInfo( User user,String useDepartmentName, String useDepartmentCode, String status, int pageNum, int pageSize) {


        String telphone = user.getTelphone();
        if(telphone==null){
            return null;
        }
        PageHelper.startPage(pageNum, pageSize);
        Employee employee = employeeService.selectByPhone(telphone);

        Seal seal = new Seal();
        seal.setUseDepartmentCode(useDepartmentCode);
        seal.setUseDepartmentName(useDepartmentName);
        seal.setMakeDepartmentCode(employee.getEmployeeDepartmentCode());
        List<Seal> list = new ArrayList<Seal>();

        if (status.equals("01")) {  //待制作
            seal.setIsRecord(true);
            list = sealDao.selectByCodeAndName(seal);
        } else if (status.equals("02")) {  //待个人化
            seal.setIsRecord(true);
            seal.setIsMake(true);
            list = sealDao.selectByCodeAndName(seal);
        } else if (status.equals("03")) {  //待交付
            seal.setIsRecord(true);
            seal.setIsMake(true);
            list = sealDao.selectByCodeAndName(seal);
        }else if(status.equals("00")){    //未交付
            list = sealDao.selectUndelivered(seal);
        }else if(status.equals("04")){    //已备案
            seal.setIsRecord(true);
            list = sealDao.selectIsRecord(seal);
        }else if(status.equals("05")){  //已经挂失
            seal.setIsRecord(true);
            seal.setIsMake(true);
            seal.setIsDeliver(true);
            seal.setIsLoss(true);
            list = sealDao.selectIsLoss(seal);
        }else if (status.equals("06")){   //已注销
            seal.setIsRecord(true);
            seal.setIsMake(true);
            seal.setIsDeliver(true);
            seal.setIsLogout(true);
            list = sealDao.selectIsLogout(seal);
        }

        PageInfo<Seal> result = new PageInfo<>(list);
        return result;
    }

    /**
     * 印模上传
     * @param user
     * @param id
     * @param sealedCardId
     * @param imageDataId
     * @return
     */
    @Override
    public int sealUpload(User user, String id, String sealedCardId, String imageDataId) {
        Seal seal1 = sealDao.selectByPrimaryKey(id);
        seal1.setSealStatusCode("01");
        String operateType = "01";
        String sealCode = seal1.getSealCode();
        SealOperationRecord sealOperationRecord1 = sealDao.SelectByCodeAndFlag03(sealCode,operateType);  //查找印模上传的
        if(sealOperationRecord1!=null){
            return ResultUtil.isFail;
        }
        String telphone = user.getTelphone();
        Employee employee = employeeService.selectByPhone(telphone);

        SealOperationRecord sealOperationRecord = new SealOperationRecord();
        sealOperationRecord.setId(UUIDUtil.generate());
        sealOperationRecord.setSealId(id);
        sealOperationRecord.setEmployeeCode(employee.getEmployeeCode());
        sealOperationRecord.setEmployeeId(employee.getEmployeeId());   //从业人员登记
        sealOperationRecord.setEmplyeeName(employee.getEmployeeName());
        sealOperationRecord.setOperateType("01");
        sealOperationRecord.setOperateTime(DateUtil.getCurrentTime());
        int insertSealOperationRecord1 = sealDao.insertSealOperationRecord(sealOperationRecord);


        List<SealMaterial> sealMaterialLists = new ArrayList<>();  //批量存入数据库
        SealMaterial sealMaterial = new SealMaterial();
        sealMaterial.setId(UUIDUtil.generate());
        sealMaterial.setSealCode(sealCode);
        sealMaterial.setType("02");  //02为印鉴留存卡
        sealMaterial.setFilePath(sealedCardId);
        SealMaterial sealMaterial1 = new SealMaterial();
        sealMaterial1.setId(UUIDUtil.generate());
        sealMaterial1.setSealCode(sealCode);
        sealMaterial1.setType("03");  //03为印章图像数据
        sealMaterial1.setFilePath(imageDataId);
        sealMaterialLists.add(sealMaterial);
        sealMaterialLists.add(sealMaterial1);
        int insertSealMaterial = sealDao.insertSealMateriallist(sealMaterialLists);

        seal1.setIsMake(true);
        seal1.setMakeDate(DateUtil.getCurrentTime());
//        seal1.setDistrictId(seal1.getDistrictId());
        int updateByPrimaryKey1 = sealDao.updateByPrimaryKey(seal1);
        if (insertSealOperationRecord1 < 0 || insertSealMaterial < 0 || updateByPrimaryKey1 < 0) {
            return ResultUtil.isFail;
        } else {
            SyncEntity syncEntity =  ((SealServiceImpl) AopContext.currentProxy()).getSyncDate(sealOperationRecord, SyncDataType.SEAL, SyncOperateType.UPLOAD);
            SyncEntity syncEntity1 =  ((SealServiceImpl) AopContext.currentProxy()).getSyncDate(sealMaterial, SyncDataType.SEAL, SyncOperateType.UPLOAD);
            SyncEntity syncEntity2 =  ((SealServiceImpl) AopContext.currentProxy()).getSyncDate(sealMaterial1, SyncDataType.SEAL, SyncOperateType.UPLOAD);
            SyncEntity syncEntity3 =  ((SealServiceImpl) AopContext.currentProxy()).getSyncDate(seal1, SyncDataType.SEAL, SyncOperateType.UPLOAD);
            return ResultUtil.isSuccess;
        }

    }

    /**
     * 印章个人化
     *
     * @param
     * @return
     */
    @Override
    public int sealPersonal(String id, User user) {
        Seal seal1 = sealDao.selectByPrimaryKey(id);
        seal1.setSealStatusCode("02");
        if(seal1.getIsLogout()){
            return ResultUtil.isFail;
        }
        if(!seal1.getIsMake()){
            return ResultUtil.isFail;
        }
        String operateType="02";
        String sealCode = seal1.getSealCode();
        //根据用户找到对应的从业人员
        String telphone = user.getTelphone();
        Employee employee = employeeService.selectByPhone(telphone);
        SealOperationRecord sealOperationRecord1 = sealDao.SelectByCodeAndFlag03(id,operateType);
        if ( sealOperationRecord1 != null) {
            return ResultUtil.isFail;
        } else {
            SealOperationRecord sealOperationRecord = new SealOperationRecord();
            sealOperationRecord.setId(UUIDUtil.generate());
            sealOperationRecord.setSealId(id);
            sealOperationRecord.setOperateTime(DateUtil.getCurrentTime());
            sealOperationRecord.setEmployeeId(employee.getEmployeeId());   //从业人员登记
            sealOperationRecord.setEmplyeeName(employee.getEmployeeName());
            sealOperationRecord.setEmployeeCode(employee.getEmployeeCode());
            sealOperationRecord.setOperateType("02");
            int insertSealOperationRecord1 = sealDao.insertSealOperationRecord(sealOperationRecord);
            seal1.setIsPersonal(true);
            seal1.setPersonalDate(DateUtil.getCurrentTime());
            int updateByPrimaryKey1 = sealDao.updateByPrimaryKey(seal1);
            if (insertSealOperationRecord1 < 0 || updateByPrimaryKey1 < 0) {
                return ResultUtil.isFail;
            } else {
                SyncEntity syncEntity =  ((SealServiceImpl) AopContext.currentProxy()).getSyncDate(sealOperationRecord, SyncDataType.SEAL, SyncOperateType.PERSONAL);
                SyncEntity syncEntity1 =  ((SealServiceImpl) AopContext.currentProxy()).getSyncDate(seal1, SyncDataType.SEAL, SyncOperateType.PERSONAL);
                return ResultUtil.isSuccess;
            }
        }
    }

    /**
     * 交付
     * @param user
     * @param
     * @param
     * @return
     */
    @Override
    public boolean deliver(User user,String id,String proxyId,String name,
                        String certificateType,String certificateNo,String agentTelphone,boolean isSame) {
        Seal seal1 = sealDao.selectByPrimaryKey(id);
        seal1.setSealStatusCode("03");
        if (seal1.getIsLogout()) {
            return false;
        }
        String sealCode = seal1.getSealCode();
        if (seal1.getIsLoss()) {
            return false;
        }

        String telphone = user.getTelphone();
        Employee employee = employeeService.selectByPhone(telphone);
        SealOperationRecord sealOperationRecord = new SealOperationRecord();
        sealOperationRecord.setId(UUIDUtil.generate());
        sealOperationRecord.setSealId(id);
        sealOperationRecord.setOperateTime(DateUtil.getCurrentTime());
        sealOperationRecord.setEmployeeId(employee.getEmployeeId());   //从业人员登记
        sealOperationRecord.setEmplyeeName(employee.getEmployeeName());
        sealOperationRecord.setEmployeeCode(employee.getEmployeeCode());
        sealOperationRecord.setOperateType("03");
        int insertSealOperationRecord = sealDao.insertSealOperationRecord(sealOperationRecord);

        seal1.setIsDeliver(true);
        seal1.setDeliverDate(DateUtil.getCurrentTime());

        if (isSame) {
            SealAgent sealAgent = sealAgentMapper.selectByPrimaryKey(seal1.getAgentId());
            String saId = UUIDUtil.generate();
            sealAgent.setId(saId);
            sealAgent.setBusinessType("01");
            seal1.setGetterId(saId);
            int sealAgentResult = sealAgentMapper.insert(sealAgent);
            int updateByPrimaryKey = sealDao.updateByPrimaryKey(seal1);
            if (insertSealOperationRecord > 0 && updateByPrimaryKey > 0 && sealAgentResult > 0) {
                return true;
            } else {
                return false;
            }
        } else {
            SealAgent sealAgent1 = new SealAgent();
            String saId = UUIDUtil.generate();
            sealAgent1.setId(saId);
            sealAgent1.setName(name);
            sealAgent1.setBusinessType("01");
            sealAgent1.setProxyId(proxyId);
//            sealAgent1.setIdcardReverseId(idcardReverseId);
//            sealAgent1.setIdcardFrontId(idcardFrontId);
            sealAgent1.setCertificateType(certificateType);
            sealAgent1.setCertificateNo(certificateNo);
//            sealAgent1.setAgentPhotoId(agentPhotoId);
            sealAgent1.setTelphone(agentTelphone);
            seal1.setGetterId(saId);
            int sealAgentResult1 = sealAgentMapper.insert(sealAgent1);
            int updateByPrimaryKey = sealDao.updateByPrimaryKey(seal1);
            if (insertSealOperationRecord > 0 && updateByPrimaryKey > 0 && sealAgentResult1 > 0) {
                return true;
            } else {
                return false;
            }

        }
    }

    /**
     * 挂失
     */
    @Override
    public int  loss (User user,String id, String agentPhotoId,  String proxyId ,String certificateNo,String certificateType,
            String localDistrictId,String businesslicenseId,String idcardFrontId,String idcardReverseId){
        Seal seal1 = sealDao.selectByPrimaryKey(id);
        seal1.setSealStatusCode("05");
        if(seal1.getIsLoss()){
             return ResultUtil.isFail;
         }
        String telphone = user.getTelphone();
        String sealCode = seal1.getSealCode();
        Employee employee = employeeService.selectByPhone(telphone);


        SealAgent sealAgent = new SealAgent();
        String saId = UUIDUtil.generate();
        sealAgent.setId(saId);
        sealAgent.setTelphone(telphone);
        sealAgent.setAgentPhotoId(agentPhotoId);
        sealAgent.setCertificateNo(certificateNo);
        sealAgent.setCertificateType(certificateType);
        sealAgent.setIdcardFrontId(idcardFrontId);
        sealAgent.setIdcardReverseId(idcardReverseId);
        sealAgent.setProxyId(proxyId);
        sealAgent.setBusinessType("02");
        sealAgent.setName(employee.getEmployeeName());
        int sealAgentResult = sealAgentMapper.insert(sealAgent);


        seal1.setIsLoss(true);
        seal1.setLossDate(DateUtil.getCurrentTime());
        seal1.setLossPersonId(saId);
        System.out.println(localDistrictId);
        RecordDepartment recordDepartment = recordDepartmentMapper.selectByDistrictIdVersion(localDistrictId);
        if(recordDepartment==null){
            return ResultUtil.isFail;
        }
        seal1.setRecordDepartmentCode(recordDepartment.getDepartmentCode());
        seal1.setRecordDepartmentName(recordDepartment.getDepartmentName());
        int updateByPrimaryKey =sealDao.updateByPrimaryKey(seal1);

        SealOperationRecord sealOperationRecord = new SealOperationRecord();
        sealOperationRecord.setId(UUIDUtil.generate());
        sealOperationRecord.setOperateType("04");
        sealOperationRecord.setEmployeeCode(employee.getEmployeeCode());
        sealOperationRecord.setOperateTime(DateUtil.getCurrentTime());
        sealOperationRecord.setEmplyeeName(employee.getEmployeeName());
        sealOperationRecord.setEmployeeId(employee.getEmployeeId());
        sealOperationRecord.setSealId(id);
        int insertSealOperationRecord =sealDao.insertSealOperationRecord(sealOperationRecord);

        //营业执照
        SealMaterial sealMaterial = new SealMaterial();
        sealMaterial.setId(UUIDUtil.generate());
        sealMaterial.setType("01");
        sealMaterial.setSealCode(sealCode);
        sealMaterial.setFilePath(businesslicenseId);
        sealDao.insertSealMaterial(sealMaterial);


        if(sealAgentResult>0&&updateByPrimaryKey>0&&insertSealOperationRecord>0){
            SyncEntity syncEntity1 =  ((SealServiceImpl) AopContext.currentProxy()).getSyncDate(sealAgent, SyncDataType.SEAL, SyncOperateType.LOSS);
            SyncEntity syncEntity2 =  ((SealServiceImpl) AopContext.currentProxy()).getSyncDate(sealOperationRecord, SyncDataType.SEAL, SyncOperateType.LOSS);
            SyncEntity syncEntity3 =  ((SealServiceImpl) AopContext.currentProxy()).getSyncDate(seal1, SyncDataType.SEAL, SyncOperateType.LOSS);
            return ResultUtil.isSuccess;
        }else{
            return ResultUtil.isFail;
        }
    }


    /**
     * 印章注销
     * @param user
     */
    @Override
    public int  logout (User user,String id, String agentPhotoId,  String proxyId ,String certificateNo,String certificateType,String businesslicenseId,String idcardFrontId,String idcardReverseId) {
        Seal seal1 = sealDao.selectByPrimaryKey(id);
        seal1.setSealStatusCode("06");
        if (seal1.getIsLogout()){
            return ResultUtil.isFail;
        }
        String sealCode = seal1.getSealCode();
        String telphone = user.getTelphone();
        Employee employee = employeeService.selectByPhone(telphone);
        SealAgent sealAgent = new SealAgent();
        String saId = UUIDUtil.generate();
        sealAgent.setId(saId);
        sealAgent.setTelphone(telphone);
        sealAgent.setAgentPhotoId(agentPhotoId);
        sealAgent.setCertificateNo(certificateNo);
        sealAgent.setCertificateType(certificateType);
        sealAgent.setIdcardFrontId(idcardFrontId);
        sealAgent.setIdcardReverseId(idcardReverseId);
        sealAgent.setProxyId(proxyId);
        sealAgent.setBusinessType("03");
        sealAgent.setName(employee.getEmployeeName());
        int sealAgentResult = sealAgentMapper.insert(sealAgent);

        //缺少营业执照
        SealMaterial sealMaterial = new SealMaterial();
        sealMaterial.setId(UUIDUtil.generate());
        sealMaterial.setType("01");
        sealMaterial.setSealCode(sealCode);
        sealMaterial.setFilePath(businesslicenseId);
        sealDao.insertSealMaterial(sealMaterial);

        seal1.setIsLogout(true);
        seal1.setLogoutDate(DateUtil.getCurrentTime());
        seal1.setLogoutPersonId(saId);


        int updateByPrimaryKey =sealDao.updateByPrimaryKey(seal1);
        SealOperationRecord sealOperationRecord = new SealOperationRecord();
        sealOperationRecord.setId(UUIDUtil.generate());
        sealOperationRecord.setOperateType("05");
        sealOperationRecord.setEmployeeCode(employee.getEmployeeCode());
        sealOperationRecord.setOperateTime(DateUtil.getCurrentTime());
        sealOperationRecord.setEmplyeeName(employee.getEmployeeName());
        sealOperationRecord.setEmployeeId(employee.getEmployeeId());
        sealOperationRecord.setSealId(id);
        int insertSealOperationRecord =sealDao.insertSealOperationRecord(sealOperationRecord);

        if(sealAgentResult>0&&insertSealOperationRecord>0&&updateByPrimaryKey>0){
            SyncEntity syncEntity2 =  ((SealServiceImpl) AopContext.currentProxy()).getSyncDate(sealAgent, SyncDataType.SEAL, SyncOperateType.LOGOUT);
            SyncEntity syncEntity3 =  ((SealServiceImpl) AopContext.currentProxy()).getSyncDate(sealOperationRecord, SyncDataType.SEAL, SyncOperateType.LOGOUT);
            SyncEntity syncEntity4 =  ((SealServiceImpl) AopContext.currentProxy()).getSyncDate(seal1, SyncDataType.SEAL, SyncOperateType.LOGOUT);
            return ResultUtil.isSuccess;
        }else{
            return ResultUtil.isFail;
        }
    }

    /**
     *查看详情
     */
    @Override
    public SealVO selectDetailById(String id) {
        Seal seal = sealDao.selectByPrimaryKey(id);
        SealVO sealVo = new SealVO();
        sealVo.setSeal(seal);
        String sealCode = seal.getSealCode();
        String agentId = seal.getAgentId();
        SealAgent sealAgent = sealDao.selectSealAgentById(agentId);
        sealVo.setOperationPhoto(sealAgent.getAgentPhotoId());
        sealVo.setPositiveIdCardScanner(sealAgent.getIdcardFrontId());
        sealVo.setReverseIdCardScanner(sealAgent.getIdcardReverseId());
        sealVo.setProxy(sealAgent.getProxyId());
        SealOperationRecord sealOperationRecord = sealDao.selectOperationRecordByCode(sealCode);
        SealMaterial sealMaterial = sealDao.selectSealMaterial(sealCode,"04");
        sealVo.setMoulageId(sealMaterial.getFilePath());
        sealVo.setSealOperationRecord(sealOperationRecord);
        return sealVo;
    }
    /**
     * 印章查询
     *
     * @param useDepartmentName
     * @param useDepartmentCode
     * @param status
     * @return
     */
    @Override
    public PageInfo<Seal> seal( User user,String useDepartmentName, String useDepartmentCode, String status, int pageNum, int pageSize) {


        String id = user.getDistrictId();
        String districtIds[] = StringUtil.DistrictUtil(id);
        String districtId = null;
        if(districtIds[1].equals("00")&&districtIds[2].equals("00")){
            districtId = districtIds[0];
        }else if(!districtIds[1].equals("00")&&districtIds[2].equals("00")){
            districtId = districtIds[0]+districtIds[1];
        }else {
            districtId = id;
        }


        Seal seal = new Seal();
        seal.setUseDepartmentCode(useDepartmentCode);
        seal.setUseDepartmentName(useDepartmentName);
        seal.setDistrictId(districtId);
        List<Seal> list = new ArrayList<Seal>();

        if (status.equals("01")) {
            seal.setIsRecord(true);
            seal.setIsMake(true);
//            seal.setRecordDepartmentCode(recordCode);
            PageHelper.startPage(pageNum, pageSize);
            list = sealDao.selectByCodeAndName(seal);
        } else if (status.equals("02")) {   //个人化
            seal.setIsRecord(true);
            seal.setIsMake(true);
//            seal.setIsPersonal(true);
//            seal.setRecordDepartmentCode(recordCode);
            list = sealDao.selectByCodeAndName(seal);
        } else if (status.equals("03")) {  //待交付
            seal.setIsRecord(true);
            seal.setIsMake(true);
            seal.setIsPersonal(true);
//            seal.setIsDeliver(true);
//            seal.setRecordDepartmentCode(recordCode);
            list = sealDao.selectByCodeAndName(seal);
        }else if(status.equals("00")){
            list = sealDao.selectUndelivered(seal);
        }else if(status.equals("04")){   //已备案
            seal.setIsRecord(true);
            list = sealDao.selectIsRecord(seal);
        }else if(status.equals("05")){
            seal.setIsRecord(true);
            seal.setIsMake(true);
            seal.setIsDeliver(true);
            seal.setIsLoss(true);
            list = sealDao.selectIsLoss(seal);
        }else if (status.equals("06")){
            seal.setIsRecord(true);
            seal.setIsMake(true);
            seal.setIsDeliver(true);
            seal.setIsLogout(true);
            list = sealDao.selectIsLogout(seal);
        }

        PageInfo<Seal> result = new PageInfo<>(list);
        return result;
    }


    /**
     * 数据同步
     * @param object
     * @param dataType
     * @param operateType
     * @return
     */
    @Sync()
    public SyncEntity getSyncDate(Object object,int dataType,int operateType){
        SyncEntity syncEntity = new SyncEntity();
        syncEntity.setObject(object);
        syncEntity.setDataType(dataType);
        syncEntity.setOperateType(operateType);
        return syncEntity;
    }

    @Override
    public PageInfo<Seal> Infoseal( User user,String useDepartmentName, String useDepartmentCode, String status, int pageNum, int pageSize) {
        String  districtId = user.getDistrictId().substring(0,2);

        Seal seal = new Seal();
        seal.setUseDepartmentCode(useDepartmentCode);
        seal.setUseDepartmentName(useDepartmentName);
        seal.setDistrictId(districtId);
        List<Seal> list = new ArrayList<Seal>();

        if (status.equals("01")) {
            seal.setIsRecord(true);
            seal.setIsMake(true);
            PageHelper.startPage(pageNum, pageSize);
            list = sealDao.selectByCodeAndName(seal);
        } else if (status.equals("02")) {   //个人化
            seal.setIsRecord(true);
            seal.setIsMake(true);
            list = sealDao.selectByCodeAndName(seal);
        } else if (status.equals("03")) {  //待交付
            seal.setIsRecord(true);
            seal.setIsMake(true);
            seal.setIsPersonal(true);
//            seal.setIsDeliver(true);
//            seal.setRecordDepartmentCode(recordCode);
            list = sealDao.selectByCodeAndName(seal);
        }else if(status.equals("00")){
            list = sealDao.selectIsRecord(seal);
        }else if(status.equals("04")){   //已备案
            seal.setIsRecord(true);
            list = sealDao.selectByCodeAndName(seal);
        }else if(status.equals("05")){
            seal.setIsRecord(true);
            seal.setIsMake(true);
            seal.setIsDeliver(true);
            seal.setIsLoss(true);
            list = sealDao.selectIsLoss(seal);
        }else if (status.equals("06")){
            seal.setIsRecord(true);
            seal.setIsMake(true);
            seal.setIsDeliver(true);
            seal.setIsLogout(true);
            list = sealDao.selectIsLogout(seal);
        }

        PageInfo<Seal> result = new PageInfo<>(list);
        return result;
    }

    /**
     * 人证合一
     * @param idCardPhotoId
     * @param fieldPhotoId
     * @return
     */
    @Override
    //@DeleteTempFile
    public FaceCompareResult faceCompare(String idCardPhotoId, String fieldPhotoId){
        FileInfo idCardFileInfo = fileService.getFileInfo(idCardPhotoId);
        FileInfo fieldFileInfo = fileService.getFileInfo(fieldPhotoId);
        if(fieldFileInfo==null||idCardFileInfo==null){
            return null;
        }
        String idCardUrl = fastDFSStoreServiceImpl.getFullPath(idCardFileInfo.getFilePath());
        String fieldUrl = fastDFSStoreServiceImpl.getFullPath(fieldFileInfo.getFilePath());

        String idCardPath = downLoadPicture(idCardUrl);
        String fieldPath = downLoadPicture(fieldUrl);
        if(idCardPath==null||fieldPath==null){
            return null;
        }
        Integer a = AFRTest.compareImage(idCardPath,fieldPath);

        FaceCompareResult confidence = new FaceCompareResult();
        confidence.setFieldPhotoId(fieldPhotoId);
        confidence.setIdCardPhotoId(idCardPhotoId);
        confidence.setIdCardPhotoPath(idCardPath);
        confidence.setFieldPhotoPath(fieldPath);
        confidence.setSimilarity(a);

        if(a<=similarity){
            confidence.setIsPass(false);
        }else {
            confidence.setIsPass(true);
        }
        return confidence;
    }




    /**
     * 比对图片下载，传入文件的url
     * @param fileUrl
     * @return
     */
    public  String downLoadPicture(String fileUrl) {
        try {
            URL url = new URL(fileUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(3 * 1000);
           // conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            InputStream inputStream = conn.getInputStream();
            byte[] fileDate = readInputStream(inputStream);
            String relativePath = localStoreFileService.store(fileDate,"",".png");
            String absolutePath = localStoreFileService.getFullPath(relativePath);
            return absolutePath;
        } catch (IOException ioe) {
            return "";
        } catch (Exception e){
            return "";
        }
    }

    /**
     * 输入流转换
     * @param inputStream
     * @return
     * @throws IOException
     */
    public  byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

}






