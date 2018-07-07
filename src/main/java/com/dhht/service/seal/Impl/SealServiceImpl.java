package com.dhht.service.seal.Impl;

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
    private UseDepartmentDao useDepartmentDao;

    @Override
   public UseDepartment isrecord(String useDepartmentCode){
     return useDepartmentDao.selectByCode(useDepartmentCode);
   }


    public int insert(Seal seal, Employee employee, String districtId, String operatorTelphone, String operatorName, String operatorCertificateCode, int operatorCrtificateType) {
        return 0;
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
        String sealVcode = districtId+vcode;
        return sealVcode;
    }



   /**
    *
    */
   @Override
   public int insert(Seal seal){
       //增加文件上传，操作记录和印章本身表
       return 1;
   }


    /**
     * 印章备案
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
    public int sealRecord(Seal seal,User user,String districtId,String operatorTelphone,
                          String operatorName,String operatorCertificateCode,String operatorCrtificateType,
                          String operatorPhoto,String idCardScanner,String proxy){

        String sealcode = createRandomCode(districtId);
        List<Seal> list = sealDao.selectByCodeAndType(seal.getUseDepartmentCode());
        if(list.size()!=0){
            return ResultUtil.isFail;    //该公司的法务印章已经存在
        }
        seal.setSealCode(sealcode);
        UseDepartment useDepartment = useDepartmentDao.selectByCode(seal.getUseDepartmentCode());  //根据usedepartment查询对应的使用公司
        String telphone = user.getTelphone();
        Employee employee = employeeService.selectByPhone(telphone);
        String legalName =useDepartment.getLegalName();
        //增加文件上传，操作记录和印章本身表
        seal.setId(UUIDUtil.generate());
        seal.setSealName(useDepartment.getName());
        String useDepartmentCode = seal.getUseDepartmentCode();
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
        sealDao.insertSealOperationRecord(sealOperationRecord);
        SealMaterial sealMaterial = new SealMaterial();
        sealMaterial.setSealCode(sealcode);
        sealMaterial.setType("01");  //01为用户照片
        sealMaterial.setFilePath(operatorPhoto);
        sealDao.insertSealMaterial(sealMaterial);
        sealMaterial.setSealCode(sealcode);
        sealMaterial.setType("02");  //02为身份证扫描件
        sealMaterial.setFilePath(idCardScanner);
        sealDao.insertSealMaterial(sealMaterial);
        if(proxy!=null||proxy!=""){
            sealMaterial.setSealCode(sealcode);
            sealMaterial.setType("03");  //03为委托书
            sealMaterial.setFilePath(proxy);
            sealDao.insertSealMaterial(sealMaterial);
        }
        seal.setSealCode(sealcode);
        seal.setIsRecord(true);
        seal.setRecordDate(new Date(System.currentTimeMillis()));
        int a = sealDao.insert(seal);
        if(a>0){
            return ResultUtil.isSuccess;
        }else{
            return ResultUtil.isFail;
        }

    }

    /**
     * 印章主页面
     * @param useDepartmentName
     * @param useDepartmentCode
     * @param status
     * @return
     */
    @Override
    public PageInfo<Seal> sealInfo(String recordCode,String useDepartmentName, String useDepartmentCode, String status, int pageNum, int pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        Seal seal = new Seal();
        seal.setUseDepartmentCode(useDepartmentCode);
        seal.setUseDepartmentName(useDepartmentName);
        List<Seal> list = new ArrayList<Seal>();

        if(status.equals("01")){
            seal.setIsRecord(true);
            seal.setIsMake(true);
            seal.setRecordDepartmentCode(recordCode);
            list = sealDao.selectByCodeAndName(seal);
        }else if (status.equals("02")){
            seal.setIsRecord(true);
            seal.setIsMake(true);
            seal.setIsPersonal(true);
            seal.setRecordDepartmentCode(recordCode);
            list = sealDao.selectByCodeAndName(seal);
        }else if (status.equals("03")){
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

    @Override
    public int sealUpload(Seal seal, File sealData, String sealScanner) {
        return 0;
    }

    /**
     * 印章个人化
     * @param seal
     * @return
     */
    @Override
    public int sealPersonal(Seal seal) {
//        String sealCode = seal.getSealCode();
//        if(seal.getIsPersonal()){
//            return ResultUtil.isFail;
//        }else{
//            SealOperationRecord sealOperationRecord = new SealOperationRecord();
//            sealOperationRecord.setSealCode(sealCode);
//            sealOperationRecord.setDateTime(new Date(System.currentTimeMillis()));
//            sealOperationRecord.setEmployeeId(employee.getEmployeeId());   //从业人员登记
//            sealOperationRecord.setEmplyeeName(employee.getEmployeeName());
//            sealOperationRecord.setOperatorName(operatorName);          //经办人
//            sealOperationRecord.setOperatorTelphone(operatorTelphone);
//            sealOperationRecord.setOperatorCertificateCode(operatorCertificateCode);
//            sealOperationRecord.setOperatorCrtificateType(operatorCrtificateType);
//            sealDao.insertSealOperationRecord(sealOperationRecord);
        return 1;

        }
    }



