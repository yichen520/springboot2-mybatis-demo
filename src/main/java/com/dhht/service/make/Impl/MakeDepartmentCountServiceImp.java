package com.dhht.service.make.Impl;

import com.dhht.dao.MakedepartmentMapper;
import com.dhht.model.DistrictMenus;
import com.dhht.model.MakedepartmentCount;
import com.dhht.service.District.DistrictService;
import com.dhht.service.make.MakeDepartmentCuontService;
import com.dhht.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 2018/7/7 create by fyc
 */
@Service(value = "makeDepartmentCountService")
@Transactional
public class MakeDepartmentCountServiceImp implements MakeDepartmentCuontService{
    @Autowired
    private MakedepartmentMapper makedepartmentMapper;
    @Autowired
    private DistrictService districtService;

    /**
     * 统计区域下所有的所有的制作单位.
     * @param districtId
     * @return
     */
    @Override
    public List<MakedepartmentCount> countAllDepartment(String districtId,String startTime,String endTime) {
        List<DistrictMenus> districtList = getDistrictList(districtId);
        List<MakedepartmentCount> makedepartmentCounts = new ArrayList<>();
        for (DistrictMenus districtMenus : districtList){
            String id =  StringUtil.getDistrictId(districtMenus.getDistrictId());
            int departmentAll = makedepartmentMapper.countAllDepartment(id);
            int departmentWork = makedepartmentMapper.countWorkDepartment(id);
            int departmentDel = makedepartmentMapper.countDeleteDepartment(id);
            int add = countByTime(id,startTime,endTime).get("add");
            int del = countByTime(id,startTime,endTime).get("del");
            makedepartmentCounts.add(new MakedepartmentCount(districtMenus.getDistrictName(),departmentAll,departmentWork,departmentDel,add,del));
        }
        return getSum(makedepartmentCounts);
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
     * 统计每一栏的总量
     * @param list
     * @return
     */
    public List<MakedepartmentCount> getSum(List<MakedepartmentCount> list){
        int departmentAllSum = 0;
        int departmentWorkSum = 0;
        int departmentDelSum = 0;
        int departmentAdd = 0;
        int departmentDel = 0;

        for(MakedepartmentCount makedepartmentCounts : list){
           departmentAllSum = departmentAllSum+makedepartmentCounts.getDepartmentAllSum();
           departmentDelSum = departmentDelSum+makedepartmentCounts.getDepartmentDelSum();
           departmentWorkSum =departmentWorkSum +makedepartmentCounts.getDepartmentWorkSum();
           departmentAdd = departmentAdd+makedepartmentCounts.getAddCount();
           departmentDel = departmentDel+makedepartmentCounts.getDeleteCount();
           }

        MakedepartmentCount makedepartmentCount = new MakedepartmentCount("总计",departmentAllSum,departmentWorkSum,departmentDelSum,departmentAdd,departmentDel);
        list.add(makedepartmentCount);
        return list;
    }

    /**
     * 追加时间条件查询
     * @param districtId
     * @param startTime
     * @param endTime
     * @return
     */
    public HashMap<String,Integer> countByTime(String districtId,String startTime,String endTime ){
        HashMap<String,Integer> hashMap = new HashMap<>();
        Integer departmentAdd = 0;
        Integer departmentDel = 0;

        if(startTime==null&&endTime==null){
            departmentAdd = makedepartmentMapper.countWorkDepartment(districtId);
            departmentDel = makedepartmentMapper.countDeleteDepartment(districtId);
        }else if(startTime==null&&endTime!=null){
            departmentAdd = makedepartmentMapper.countAddDepartmentByEndTime(districtId,endTime);
            departmentDel = makedepartmentMapper.countDelDepartmentByEndTime(districtId,endTime);
        }else if(startTime!=null&&endTime==null){
            departmentAdd = makedepartmentMapper.countAddDepartmentByStartTime(districtId,startTime);
            departmentDel = makedepartmentMapper.countDelDepartmentByStartTime(districtId,startTime);
        }else {
            departmentAdd = makedepartmentMapper.countAddDepartmentByTime(districtId,startTime,endTime);
            departmentDel = makedepartmentMapper.countDelDepartmentByTime(districtId,startTime,endTime);
        }

        hashMap.put("add",departmentAdd);
        hashMap.put("del",departmentDel);
        return hashMap;
    }


}
