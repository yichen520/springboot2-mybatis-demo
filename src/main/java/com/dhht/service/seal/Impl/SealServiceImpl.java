package com.dhht.service.seal.Impl;

import com.dhht.dao.RecordDepartmentMapper;
import com.dhht.dao.ResourceMapper;
import com.dhht.dao.SealDao;
import com.dhht.dao.UseDepartmentDao;
import com.dhht.model.*;
import com.dhht.service.employee.EmployeeService;
import com.dhht.service.resource.ResourceService;
import com.dhht.service.seal.SealService;
import com.dhht.util.ResultUtil;
import com.dhht.util.UUIDUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service("sealService")
@Transactional
public class SealServiceImpl implements SealService {
    @Autowired
    private SealDao sealDao;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private RecordDepartmentMapper recordDepartmentMapper;

    @Autowired
    private UseDepartmentDao useDepartmentDao;

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
     *
     * @param seal
     * @param user
     * @param districtId
     * @param operatorTelphone
     * @param operatorName
     * @param operatorCertificateCode
     * @param operatorCrtificateType
     * @param operatorPhoto
     * @param idCardScanner
     * @param proxy
     * @return
     */
    @Override
    public int sealRecord(Seal seal, User user, String districtId, String operatorTelphone,
                          String operatorName, String operatorCertificateCode, String operatorCrtificateType,
                          String operatorPhoto, String idCardScanner, String proxy) {

        String sealcode = createRandomCode(districtId);
        List<Seal> list = sealDao.selectByCodeAndType(seal.getUseDepartmentCode());
        if (list.size() != 0) {
            return ResultUtil.isFail;    //该公司的法务印章已经存在
        }
        seal.setSealCode(sealcode);
        UseDepartment useDepartment = useDepartmentDao.selectByCode(seal.getUseDepartmentCode());  //根据usedepartment查询对应的使用公司
        if(useDepartment==null){
            return ResultUtil.isFail;
        }
        String telphone = user.getTelphone();
        Employee employee = employeeService.selectByPhone(telphone);
//        String legalName = useDepartment.getLegalName();

        seal.setId(UUIDUtil.generate());
        seal.setSealName(useDepartment.getName());
        seal.setIsRecord(true);
        seal.setRecordDate(new Date(System.currentTimeMillis()));
        seal.setIsMake(false);
        seal.setIsLoss(false);
        seal.setIsPersonal(false);
        seal.setIsLogout(false);
        String useDepartmentCode = seal.getUseDepartmentCode();

        //操作记录
        SealOperationRecord sealOperationRecord = new SealOperationRecord();
        sealOperationRecord.setId(UUIDUtil.generate());
        sealOperationRecord.setSealCode(sealcode);
        sealOperationRecord.setDateTime(new Date(System.currentTimeMillis()));
        sealOperationRecord.setEmployeeId(employee.getEmployeeId());   //从业人员登记
        sealOperationRecord.setEmplyeeName(employee.getEmployeeName());
        sealOperationRecord.setOperatorName(operatorName);          //经办人
        sealOperationRecord.setOperatorTelphone(operatorTelphone);
        sealOperationRecord.setOperatorCertificateCode(operatorCertificateCode);
        sealOperationRecord.setOperatorCrtificateType(operatorCrtificateType);
        sealOperationRecord.setFlag("01");
        sealDao.insertSealOperationRecord(sealOperationRecord);
        SealMaterial sealMaterial = new SealMaterial();
        sealMaterial.setId(UUIDUtil.generate());
        sealMaterial.setSealCode(sealcode);
        sealMaterial.setType("01");  //01为用户照片
        sealMaterial.setFilePath(operatorPhoto);
        sealDao.insertSealMaterial(sealMaterial);
        sealMaterial.setId(UUIDUtil.generate());
        sealMaterial.setSealCode(sealcode);
        sealMaterial.setType("02");  //02为身份证扫描件
        sealMaterial.setFilePath(idCardScanner);
        sealDao.insertSealMaterial(sealMaterial);
        if (proxy != null || proxy != "") {
            sealMaterial.setId(UUIDUtil.generate());
            sealMaterial.setSealCode(sealcode);
            sealMaterial.setType("03");  //03为委托书
            sealMaterial.setFilePath(proxy);
            sealDao.insertSealMaterial(sealMaterial);
        }
        seal.setSealCode(sealcode);
        seal.setIsRecord(true);
        seal.setRecordDate(new Date(System.currentTimeMillis()));
        int a = sealDao.insert(seal);
        if (a > 0) {
            return ResultUtil.isSuccess;
        } else {
            return ResultUtil.isFail;
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
    public PageInfo<Seal> sealInfo(String recordCode, String useDepartmentName, String useDepartmentCode, String status, int pageNum, int pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        Seal seal = new Seal();
        seal.setUseDepartmentCode(useDepartmentCode);
        seal.setUseDepartmentName(useDepartmentName);
        List<Seal> list = new ArrayList<Seal>();

        if (status.equals("01")) {
            seal.setIsRecord(true);
            seal.setIsMake(true);
            seal.setRecordDepartmentCode(recordCode);
            list = sealDao.selectByCodeAndName(seal);
        } else if (status.equals("02")) {
            seal.setIsRecord(true);
            seal.setIsMake(true);
            seal.setIsPersonal(true);
            seal.setRecordDepartmentCode(recordCode);
            list = sealDao.selectByCodeAndName(seal);
        } else if (status.equals("03")) {
            seal.setIsRecord(true);
            seal.setIsMake(true);
            seal.setIsPersonal(true);
            seal.setIsDeliver(true);
            seal.setRecordDepartmentCode(recordCode);
            list = sealDao.selectByCodeAndName(seal);
        }

        PageInfo<Seal> result = new PageInfo<>(list);
        return result;
    }

    /**
     * 印模上传
     *
     * @param seal
     * @param electronicSealURL
     * @param sealScannerURL
     * @return
     */
    @Override
    public int sealUpload(User user, Seal seal, String electronicSealURL, String sealScannerURL) {
        Seal seal1 = sealDao.selectByPrimaryKey(seal.getId());
        if(seal1.getIsLogout()){
            return ResultUtil.isFail;
        }
        String flag = "02";
        String sealCode = seal1.getSealCode();
        SealOperationRecord sealOperationRecord = sealDao.SelectByCodeAndFlag(sealCode);
        SealOperationRecord sealOperationRecord1 = sealDao.SelectByCodeAndFlag03(sealCode,flag);
        if(sealOperationRecord1!=null){
            return ResultUtil.isFail;
        }
        String telphone = user.getTelphone();
        Employee employee = employeeService.selectByPhone(telphone);
        sealOperationRecord.setId(UUIDUtil.generate());
        sealOperationRecord.setSealCode(sealCode);
        sealOperationRecord.setDateTime(new Date(System.currentTimeMillis()));
        sealOperationRecord.setEmployeeId(employee.getEmployeeId());   //从业人员登记
        sealOperationRecord.setEmplyeeName(employee.getEmployeeName());
//        sealOperationRecord.setOperatorName("");          //经办人
//        sealOperationRecord.setOperatorTelphone("");
//        sealOperationRecord.setOperatorCertificateCode("");
//        sealOperationRecord.setOperatorCrtificateType("");
        sealOperationRecord.setFlag(flag);
        int c = sealDao.insertSealOperationRecord(sealOperationRecord);

        //把数据存入材料表中
        SealMaterial sealMaterial = new SealMaterial();
        sealMaterial.setId(UUIDUtil.generate());
        sealMaterial.setSealCode(sealCode);
        sealMaterial.setType("04");  //04为电子印模数据文件
        sealMaterial.setFilePath(electronicSealURL);
        int b = sealDao.insertSealMaterial(sealMaterial);
        sealMaterial.setId(UUIDUtil.generate());
        sealMaterial.setSealCode(sealCode);
        sealMaterial.setType("05");  //04为电子印模数据文件
        sealMaterial.setFilePath(sealScannerURL);

        seal1.setIsMake(true);
        seal1.setMakeDate(new Date(System.currentTimeMillis()));
        int a = sealDao.updateByPrimaryKey(seal1);
        if (a < 0 || b < 0 || c < 0) {
            return ResultUtil.isFail;
        } else {
            return ResultUtil.isSuccess;
        }

    }

    /**
     * 印章个人化
     *
     * @param seal
     * @return
     */
    @Override
    public int sealPersonal(Seal seal, User user) {
        Seal seal1 = sealDao.selectByPrimaryKey(seal.getId());
        if(seal1.getIsLogout()){
            return ResultUtil.isFail;
        }
        if(!seal1.getIsMake()){
            return ResultUtil.isFail;
        }
        String flag="03";
        String sealCode = seal1.getSealCode();
        System.out.print(sealCode);
        //根据用户找到对应的从业人员
        String telphone = user.getTelphone();
        Employee employee = employeeService.selectByPhone(telphone);
        SealOperationRecord sealOperationRecord1 = sealDao.SelectByCodeAndFlag03(sealCode,flag);
        if ( sealOperationRecord1 != null) {
            return ResultUtil.isFail;
        } else {
            SealOperationRecord sealOperationRecord = sealDao.SelectByCodeAndFlag(sealCode);
            sealOperationRecord.setId(UUIDUtil.generate());
            sealOperationRecord.setSealCode(sealCode);
            sealOperationRecord.setDateTime(new Date(System.currentTimeMillis()));
            sealOperationRecord.setEmployeeId(employee.getEmployeeId());   //从业人员登记
            sealOperationRecord.setEmplyeeName(employee.getEmployeeName());
            sealOperationRecord.setFlag("03");//个人化
//            sealOperationRecord.setOperatorName(operatorName);
//            sealOperationRecord.setOperatorTelphone(operatorTelphone);
//            sealOperationRecord.setOperatorCertificateCode(operatorCertificateCode);
//            sealOperationRecord.setOperatorCrtificateType(operatorCrtificateType);
            int a = sealDao.insertSealOperationRecord(sealOperationRecord);
            seal1.setIsPersonal(true);
            seal1.setPersonalDate(new Date(System.currentTimeMillis()));
            int b = sealDao.updateByPrimaryKey(seal1);
            seal1.setSealOperationRecord(sealOperationRecord);
            if (a < 0 || b < 0) {
                return ResultUtil.isFail;
            } else {
                return ResultUtil.isSuccess;
            }

        }
    }

    /**
     * 交付
     * @param user
     * @param seal
     * @param sealGetPerson
     * @return
     */
    @Override
    public boolean deliver(User user,Seal  seal,SealGetPerson sealGetPerson) {
        int c = 0;
        Seal seal1 = sealDao.selectByPrimaryKey(seal.getId());
        if(seal1.getIsLogout()){
            return false;
        }
        String sealCode = seal1.getSealCode();
        if(seal1.getIsLogout()){
            return false;
        }
        sealGetPerson.setSealCode(sealCode);
        String telphone = user.getTelphone();
        Employee employee = employeeService.selectByPhone(telphone);
        SealOperationRecord sealOperationRecord = sealDao.SelectByCodeAndFlag(sealCode);
        sealOperationRecord.setId(UUIDUtil.generate());
        sealOperationRecord.setSealCode(sealCode);
        sealOperationRecord.setDateTime(new Date(System.currentTimeMillis()));
        sealOperationRecord.setEmployeeId(employee.getEmployeeId());   //从业人员登记
        sealOperationRecord.setEmplyeeName(employee.getEmployeeName());
        sealOperationRecord.setFlag("04");//交付
        int a = sealDao.insertSealOperationRecord(sealOperationRecord);
        seal1.setIsDeliver(true);
        seal1.setDeliverDate(new Date(System.currentTimeMillis()));
        int b = sealDao.updateByPrimaryKey(seal1);
        if(sealGetPerson.getIsSame()){
            sealGetPerson.setId(UUIDUtil.generate());
            sealGetPerson.setGetDate(new Date(System.currentTimeMillis()));
            sealGetPerson.setGetpersonId(sealOperationRecord.getOperatorCertificateCode());
            sealGetPerson.setGetpersonName(sealOperationRecord.getOperatorName());
            sealGetPerson.setGetpersonTelphone(sealOperationRecord.getOperatorTelphone());
            sealGetPerson.setGetpersonType(sealOperationRecord.getOperatorCrtificateType());
            c = sealDao.insertSealGetperson(sealGetPerson);
        }else{
            c = sealDao.insertSealGetperson(sealGetPerson);
        }

        if(c<0&&b<0&&c<0){
            return false;
        }else{
            return true;
        }
    }

    /**
     * 挂失
     * @param user
     * @param seal
     * @param operatorPhoto
     * @param proxy
     * @param businessScanner
     * @param sealOperationRecord
     * @param recordCode
     * @return
     */
    @Override
    public int  loss (User user,Seal seal, String operatorPhoto,  String proxy ,String businessScanner,SealOperationRecord sealOperationRecord,String recordCode){
        Seal seal1 = sealDao.selectByPrimaryKey(seal.getId());
        if(seal1.getIsLoss()){
             return ResultUtil.isFail;
         }
        String sealCode = seal1.getSealCode();
        String telphone = user.getTelphone();
        Employee employee = employeeService.selectByPhone(telphone);
        SealMaterial sealMaterial = new SealMaterial();
        sealMaterial.setId(UUIDUtil.generate());
        sealMaterial.setSealCode(sealCode);
        sealMaterial.setType("01");
        sealMaterial.setFilePath(operatorPhoto);
        int a =sealDao.insertSealMaterial(sealMaterial);
        sealMaterial.setId(UUIDUtil.generate());
        sealMaterial.setSealCode(sealCode);
        sealMaterial.setType("03");
        sealMaterial.setFilePath(proxy);
        int b =sealDao.insertSealMaterial(sealMaterial);
        sealMaterial.setId(UUIDUtil.generate());
        sealMaterial.setSealCode(sealCode);
        sealMaterial.setType("06");
        sealMaterial.setFilePath(businessScanner);
        int c =sealDao.insertSealMaterial(sealMaterial);
        seal1.setIsLoss(true);
        seal1.setLossDate(new Date(System.currentTimeMillis()));
        RecordDepartment recordDepartment = recordDepartmentMapper.selectByCode(recordCode);
        seal1.setRecordDepartmentCode(recordCode);
        seal1.setRecordDepartmentName(recordDepartment.getDepartmentName());
        int d =sealDao.updateByPrimaryKey(seal1);
        sealOperationRecord.setId(UUIDUtil.generate());
        sealOperationRecord.setSealCode(sealCode);
        sealOperationRecord.setDateTime(new Date(System.currentTimeMillis()));
        sealOperationRecord.setEmployeeId(employee.getEmployeeId());   //从业人员登记
        sealOperationRecord.setEmplyeeName(employee.getEmployeeName());
        sealOperationRecord.setFlag("05");//挂失
        int e =sealDao.insertSealOperationRecord(sealOperationRecord);
        if(a>0&&b>0&&c>0&&d>0&&e>0){
            return ResultUtil.isSuccess;
        }else{
            return ResultUtil.isFail;
        }
    }


    /**
     * 印章注销
     * @param user
     * @param seal
     * @param operatorPhoto
     * @param proxy
     * @param businessScanner
     * @param sealOperationRecord
     * @return
     */
    @Override
    public int  logout (User user,Seal seal, String operatorPhoto,  String proxy ,String businessScanner,SealOperationRecord sealOperationRecord) {
        Seal seal1 = sealDao.selectByPrimaryKey(seal.getId());
        if (seal1.getIsLogout()){
            return ResultUtil.isFail;
        }
        String sealCode = seal1.getSealCode();
        String telphone = user.getTelphone();
        Employee employee = employeeService.selectByPhone(telphone);
        SealMaterial sealMaterial = new SealMaterial();
        sealMaterial.setId(UUIDUtil.generate());
        sealMaterial.setSealCode(sealCode);
        sealMaterial.setType("01");
        sealMaterial.setFilePath(operatorPhoto);
        int a =sealDao.insertSealMaterial(sealMaterial);
        sealMaterial.setId(UUIDUtil.generate());
        sealMaterial.setSealCode(sealCode);
        sealMaterial.setType("03");
        sealMaterial.setFilePath(proxy);
        int b =sealDao.insertSealMaterial(sealMaterial);
        sealMaterial.setId(UUIDUtil.generate());
        sealMaterial.setSealCode(sealCode);
        sealMaterial.setType("06");
        sealMaterial.setFilePath(businessScanner);
        int c =sealDao.insertSealMaterial(sealMaterial);
        seal1.setIsLogout(true);
        seal1.setLossDate(new Date(System.currentTimeMillis()));
        int d =sealDao.updateByPrimaryKey(seal);
        sealOperationRecord.setId(UUIDUtil.generate());
        sealOperationRecord.setSealCode(sealCode);
        sealOperationRecord.setDateTime(new Date(System.currentTimeMillis()));
        sealOperationRecord.setEmployeeId(employee.getEmployeeId());   //从业人员登记
        sealOperationRecord.setEmplyeeName(employee.getEmployeeName());
        sealOperationRecord.setFlag("06");//挂失
        int e =sealDao.insertSealOperationRecord(sealOperationRecord);
        if(a>0&&b>0&&c>0&&d>0&&e>0){
            return ResultUtil.isSuccess;
        }else{
            return ResultUtil.isFail;
        }
    }
    }






