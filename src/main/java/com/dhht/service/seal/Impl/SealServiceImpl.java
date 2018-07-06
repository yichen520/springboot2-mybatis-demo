package com.dhht.service.seal.Impl;

import com.dhht.dao.ResourceMapper;
import com.dhht.dao.SealDao;
import com.dhht.dao.UseDepartmentDao;
import com.dhht.model.*;
import com.dhht.service.resource.ResourceService;
import com.dhht.service.seal.SealService;
import com.dhht.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service("sealService")
public class SealServiceImpl implements SealService {
    @Autowired
    private SealDao sealDao;

    @Autowired
    private UseDepartmentDao useDepartmentDao;

    @Override
   public UseDepartment isrecord(String useDepartmentCode){
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
        String sealVcode = districtId+vcode;
        return sealVcode;
    }

   @Override
   @Transactional
   public int insert(Seal seal,Employee employee,String districtId,String operatorTelphone,String operatorName,String operatorCertificateCode,int operatorCrtificateType){
       List<Seal> list = sealDao.selectByCodeAndType(seal.getUseDepartmentCode());
       if(list!=null){
           return ResultUtil.isFail;
       }
        //增加文件上传，操作记录和印章本身表
       String sealcode = createRandomCode(districtId);
       String useDepartmentCode = seal.getUseDepartmentCode();
       SealOperationRecord sealOperationRecord = new SealOperationRecord();
       sealOperationRecord.setSealCode(sealcode);
       sealOperationRecord.setDateTime(new Date(System.currentTimeMillis()));
       sealOperationRecord.setEmployeeId(employee.getEmployeeId());
       sealOperationRecord.setEmplyeeName(employee.getEmployeeName());
       sealOperationRecord.setOperatorName(operatorName);
       sealOperationRecord.setOperatorTelphone(operatorTelphone);
       sealOperationRecord.setOperatorCertificateCode(operatorCertificateCode);
       sealOperationRecord.setOperatorCrtificateType(operatorCrtificateType);
       sealDao.insertSealOperationRecord(sealOperationRecord);


       return 1;
   }

    @Override
    public int sealRecord(Seal seal) {
        return 0;
    }


}
