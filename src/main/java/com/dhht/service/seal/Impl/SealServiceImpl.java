package com.dhht.service.seal.Impl;

import com.dhht.annotation.Sync;
import com.dhht.dao.RecordDepartmentMapper;
import com.dhht.dao.SealAgentMapper;
import com.dhht.dao.SealDao;
import com.dhht.dao.UseDepartmentDao;
import com.dhht.face.AFRTest;
import com.dhht.model.*;
import com.dhht.model.pojo.SealVO;

import com.dhht.service.employee.EmployeeService;
import com.dhht.service.make.MakeDepartmentService;
import com.dhht.service.recordDepartment.RecordDepartmentService;
import com.dhht.service.seal.SealService;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    //相似度参数
    @Value("${face.similarity}")
    private float similarity  ;

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


    /**
     *
     */
    @Override
    public int insert(Seal seal) {
        //增加文件上传，操作记录和印章本身表
        return 1;
    }



    /**
     * 印章备案
     */
        @Override
        public int sealRecord(Seal seal, User user, String districtId, String agentTelphone,
                              String agentName, String certificateNo, String certificateType,
                              String agentPhotoId, String idcardFrontId, String idcardReverseId,  String proxyId,String faceCompareRecordId) {

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
            MakeDepartmentSimple makedepartment = makeDepartmentService.selectByDepartmentCode(employee.getEmployeeDepartmentCode()); //获取制作单位
            RecordDepartment recordDepartment = recordDepartmentMapper.selectBydistrict(districtId);//获取备案单位
           if(makedepartment==null ||recordDepartment==null){
               return ResultUtil.isFail;
           }
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
            if(proxyId!=null) {
                sealAgent.setProxyId(proxyId);
            }
            if(faceCompareRecordId!=null) {
                sealAgent.setFaceCompareRecordId(faceCompareRecordId);
            }

            int sealAgentInsert = sealAgentMapper.insert(sealAgent);
            seal.setAgentId(saId);
            int sealInsert = sealDao.insert(seal);
            if(sealInsert>0&&sealOperationRecordInsert>0&&sealAgentInsert>0){
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
     *
     * @param
     * @param electronicSealURL
     * @param sealScannerURL
     * @return
     */
    @Override
    public int sealUpload(User user, String id, String electronicSealURL, String sealScannerURL) {
        Seal seal1 = sealDao.selectByPrimaryKey(id);
        seal1.setSealStatusCode("01");
        if(seal1.getIsLogout()){
            return ResultUtil.isFail;
        }
        String operateType = "01";
        String sealCode = seal1.getSealCode();
        SealOperationRecord sealOperationRecord = sealDao.SelectByCodeAndFlag(sealCode);
        SealOperationRecord sealOperationRecord1 = sealDao.SelectByCodeAndFlag03(sealCode,operateType);  //查找印模上传的
        if(sealOperationRecord1!=null){
            return ResultUtil.isFail;
        }
        String telphone = user.getTelphone();
        Employee employee = employeeService.selectByPhone(telphone);
        sealOperationRecord.setId(UUIDUtil.generate());
        sealOperationRecord.setSealId(id);
        sealOperationRecord.setEmployeeCode(employee.getEmployeeCode());
        sealOperationRecord.setEmployeeId(employee.getEmployeeId());   //从业人员登记
        sealOperationRecord.setEmplyeeName(employee.getEmployeeName());
        sealOperationRecord.setOperateType("01");
        sealOperationRecord.setOperateTime(DateUtil.getCurrentTime());
        int insertSealOperationRecord1 = sealDao.insertSealOperationRecord(sealOperationRecord);

        //把数据存入材料表中
        SealMaterial sealMaterial = new SealMaterial();
        sealMaterial.setId(UUIDUtil.generate());
        sealMaterial.setSealCode(sealCode);
        sealMaterial.setType("04");  //04为电子印模数据文件
        sealMaterial.setFilePath(electronicSealURL);
        int insertSealMaterial1 = sealDao.insertSealMaterial(sealMaterial);
        SealMaterial sealMaterial1 = new SealMaterial();
        sealMaterial1.setId(UUIDUtil.generate());
        sealMaterial1.setSealCode(sealCode);
        sealMaterial1.setType("05");  //04为印章扫描件
        sealMaterial1.setFilePath(sealScannerURL);

        seal1.setIsMake(true);
        seal1.setMakeDate(DateUtil.getCurrentTime());
//        seal1.setDistrictId(seal1.getDistrictId());
        int updateByPrimaryKey1 = sealDao.updateByPrimaryKey(seal1);
        if (insertSealOperationRecord1 < 0 || insertSealMaterial1 < 0 || updateByPrimaryKey1 < 0) {
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
        sealMaterial.setType("02");
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
        sealMaterial.setType("02");
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
        List<String> types = new ArrayList<>();
        types.add("01"); //照片
        types.add("07"); //身份证正面
        types.add("08"); //身份证反面
        types.add("03"); //委托
        List<SealMaterial> sealMaterials = sealDao.selectSealMaterialByCode(sealCode,types);
        for (SealMaterial sealMaterial:sealMaterials){
            if(sealMaterial.getType().equals("01")){
                sealVo.setOperationPhoto(sealMaterial.getFilePath());
            }else if(sealMaterial.getType().equals("07")){
                sealVo.setPositiveIdCardScanner(sealMaterial.getFilePath());
            }else if(sealMaterial.getType().equals("08")){
                sealVo.setReverseIdCardScanner(sealMaterial.getFilePath());
            }else{
                sealVo.setProxy(sealMaterial.getFilePath());
            }
        }

        SealOperationRecord sealOperationRecord = sealDao.selectOperationRecordByCode(sealCode);
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


    /**
     * 人证合一
     * @param fileAURL
     * @param fileBURL
     * @return
     */
    @Override
    public FaceCompareResult checkface(String fileAURL, String fileBURL){
        Integer a = (int)AFRTest.compareImage(fileAURL,fileBURL);

        FaceCompareResult confidence = new FaceCompareResult();
        confidence.setFieldPhoto(fileBURL);
        confidence.setSimilarity(a);
        if(a<similarity){
            confidence.setIsPass(true);
            return confidence;
        }else{
            confidence.setIsPass(false);
            return confidence;
        }
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

}






