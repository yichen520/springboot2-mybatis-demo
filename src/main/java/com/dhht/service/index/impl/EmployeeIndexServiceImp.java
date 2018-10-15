package com.dhht.service.index.impl;

import com.dhht.dao.SealDao;
import com.dhht.model.Employee;
import com.dhht.model.IndexCount;
import com.dhht.model.User;
import com.dhht.service.employee.EmployeeService;
import com.dhht.service.index.EmployeeIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


@Service("EmployeeIndexService")
public class EmployeeIndexServiceImp implements EmployeeIndexService {

    @Autowired
    private SealDao sealDao;
    @Autowired
    private EmployeeService employeeService;

    private  static  final String SEAL_TYPE[] = {"01","02","03","04","05","99"};

    /**
     * 返回从业人员饼图数据
     * @param user
     * @return
     */
    @Override
    public List<IndexCount> piechart(User user) {
        List<IndexCount> result = new ArrayList<>();
        String makeDepartmentCode = getEmployee(user).getEmployeeDepartmentCode();
        List<IndexCount> sealTypes = getSealType(makeDepartmentCode);
        for(IndexCount sealType : sealTypes ){
            int sealCount = sealDao.indexCountPieChart(makeDepartmentCode,sealType.getTemp());
            int sealSum = getSealSum(makeDepartmentCode);
            double percent = getSealPercent(sealSum,sealCount);
            IndexCount indexCount = new IndexCount(sealType.getTemp(),sealCount,percent);
            result.add(indexCount);
        }
        return countZeroPercent(result);
    }

    /**
     * 获取从业人员
     * @param user
     * @return
     */
    public Employee getEmployee(User user){
        String phone = user.getTelphone();
        Employee employee = employeeService.selectByPhone(phone);
        return employee;
    }

    /**
     * 获取制作单位制作的印章总数
     * @param makeDepartmentCode
     * @return
     */
    public int getSealSum(String makeDepartmentCode){
        int sealSum = sealDao.indexCountAllSealByMakeDepartment(makeDepartmentCode);
        return sealSum;
    }

    /**
     * 获取制作单位做过的各类型印章
     * @param makeDepartmentCode
     * @return
     */
    public List<IndexCount> getSealType(String makeDepartmentCode){
        List<IndexCount> sealType = sealDao.indexCountSealTypeByMakeDepartment(makeDepartmentCode);
        return sealType;
    }

    /**
     * 获取每种类型印章的占比
     * @param sealSum
     * @param sealCount
     * @return
     */
    public double getSealPercent(int sealSum,int sealCount){
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        double d = ((double)sealCount)/sealSum;
        String temp  = decimalFormat.format(d);
        Double percent = Double.valueOf(temp);
        return percent;
    }

    /**
     * 对比统计没有做过类型的章
     * @param indexCounts
     * @return
     */
    public List<IndexCount> countZeroPercent(List<IndexCount> indexCounts){
        for(IndexCount indexCount : indexCounts){
            boolean isZero = true;
            String tempType = "";
            for(String type : SEAL_TYPE){
                tempType = type;
                if(type.equals(indexCount.getComment())){
                    isZero =false;
                    break;
                }
            }
            if(isZero){
                IndexCount temp = new IndexCount(tempType,0,0);
                indexCounts.add(temp);
            }
        }
        return indexCounts;
    }

}
