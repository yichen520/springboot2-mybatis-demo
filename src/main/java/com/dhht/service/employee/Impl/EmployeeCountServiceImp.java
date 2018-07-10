package com.dhht.service.employee.Impl;

import com.dhht.dao.EmployeeDao;
import com.dhht.dao.MakedepartmentMapper;
import com.dhht.model.Count;
import com.dhht.model.DistrictMenus;
import com.dhht.service.District.DistrictService;
import com.dhht.service.employee.EmployeeCountService;
import com.dhht.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
@Service(value = "EmployeeCountService")
public class EmployeeCountServiceImp implements EmployeeCountService{
    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private DistrictService districtService;
    /**
     * 统计结果
     * @param districtId
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public List<Count> countAllEmployee(String districtId, String startTime, String endTime) {
        List<DistrictMenus> districtList = getDistrictList(districtId);
        List<Count> counts = new ArrayList<>();
        for (DistrictMenus districtMenus : districtList){
            String id =  StringUtil.getDistrictId(districtMenus.getDistrictId());
            int employeeAll = employeeDao.countAllEmployee(id);
            int employeeWork = employeeDao.countWorkEmployee(id);
            int employeeDel = employeeDao.countDelEmployee(id);
            int add = countByTime(id,startTime,endTime).get("add");
            int del = countByTime(id,startTime,endTime).get("del");
            counts.add(new Count(districtMenus.getDistrictName(),employeeAll,employeeWork,employeeDel,add,del));
        }
        return getSum(counts);
    }

    /**
     * 返回区域列表
     * @param district
     * @return
     */
    public List<DistrictMenus> getDistrictList(String district){
        List<DistrictMenus> list = districtService.selectOneDistrict(district);
        return list.get(0).getChildren();
    }

    /**
     * 统计总和
     * @param districtId
     * @param startTime
     * @param endTime
     * @return
     */
    public HashMap<String,Integer> countByTime(String districtId, String startTime, String endTime ){
        HashMap<String,Integer> hashMap = new HashMap<>();
        Integer employeeAdd = 0;
        Integer employeeDel = 0;

        if(startTime==null&&endTime==null){
            employeeAdd = employeeDao.countWorkEmployee(districtId);
            employeeDel = employeeDao.countDelEmployee(districtId);
        }else if(startTime==null&&endTime!=null){
            employeeAdd = employeeDao.countAddByEndTime(districtId,endTime);
            employeeDel = employeeDao.countDelByEndTime(districtId,endTime);
        }else if(startTime!=null&&endTime==null){
            employeeAdd = employeeDao.countAddByStartTime(districtId,startTime);
            employeeDel = employeeDao.countDelByStartTime(districtId,startTime);
        }else {
            employeeAdd = employeeDao.countAddByTime(districtId,startTime,endTime);
            employeeDel = employeeDao.countDelByTime(districtId,startTime,endTime);
        }

        hashMap.put("add",employeeAdd);
        hashMap.put("del",employeeDel);
        return hashMap;
    }

    /**
     * 统计每一栏的总量
     * @param list
     * @return
     */
    public List<Count> getSum(List<Count> list){
        int employeeAllSum = 0;
        int employeeWorkSum = 0;
        int employeeDelSum = 0;
        int employeeAdd = 0;
        int employeeDel = 0;

        for(Count counts : list){
            employeeAllSum = employeeAllSum+ counts.getAllSum();
            employeeDelSum = employeeDelSum+ counts.getDelSum();
            employeeWorkSum =employeeWorkSum + counts.getWorkSum();
            employeeAdd = employeeAdd+ counts.getAddCount();
            employeeDel = employeeDel+ counts.getDeleteCount();
        }

        Count count = new Count("总计",employeeAllSum,employeeWorkSum,employeeDelSum,employeeAdd,employeeDel);
        list.add(count);
        return list;
    }

}
