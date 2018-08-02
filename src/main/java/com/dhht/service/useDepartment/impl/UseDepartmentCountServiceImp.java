package com.dhht.service.useDepartment.impl;

import com.dhht.dao.UseDepartmentDao;
import com.dhht.model.Count;
import com.dhht.model.DistrictMenus;
import com.dhht.service.District.DistrictService;
import com.dhht.service.useDepartment.UseDepartmentCountService;
import com.dhht.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * create by fyc 2018/7/24
 */
@Service(value = "UseDepartmentCountService")
@Transactional
public class UseDepartmentCountServiceImp implements UseDepartmentCountService {
    @Autowired
    private UseDepartmentDao useDepartmentDao;
    @Autowired
    private DistrictService districtService;
    /**
     * 统计使用单位
     * @param districtId
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public List<Count> countAllUseDepartment(String districtId, String startTime, String endTime) {
        List<DistrictMenus> districtList = getDistrictList(districtId);
        List<Count> counts = new ArrayList<>();
        for (DistrictMenus districtMenus : districtList){
            String id =  StringUtil.getDistrictId(districtMenus.getDistrictId());
            int useDepartmentAll = useDepartmentDao.countAllDepartment(id);
            int useDepartmentWork = useDepartmentDao.countWorkDepartment(id);
            int useDepartmentDel = useDepartmentDao.countDelDepartment(id);
            int add = countByTime(id,startTime,endTime).get("add");
            int del = countByTime(id,startTime,endTime).get("del");
            counts.add(new Count(districtMenus.getDistrictName(),useDepartmentAll,useDepartmentWork,useDepartmentDel,add,del));
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
        if(list.get(0).getChildren()==null){
            return list;
        }
        return list.get(0).getChildren();
    }

    /**
     * 统计
     * @param districtId
     * @param startTime
     * @param endTime
     * @return
     */
    public HashMap<String,Integer> countByTime(String districtId, String startTime, String endTime){
        HashMap<String,Integer> hashMap = new HashMap<>();
        Integer UseDepartmentAdd = 0;
        Integer UseDepartmentDel = 0;

        if(startTime==null&&endTime==null){
            UseDepartmentAdd = useDepartmentDao.countWorkDepartment(districtId);
            UseDepartmentDel = useDepartmentDao.countDelDepartment(districtId);
        }else if(startTime==null&&endTime!=null){
            UseDepartmentAdd = useDepartmentDao.countAddByEndTime(districtId,endTime);
            UseDepartmentDel = useDepartmentDao.countDelByEndTime(districtId,endTime);
        }else if(startTime!=null&&endTime==null){
            UseDepartmentAdd = useDepartmentDao.countAddByStartTime(districtId,startTime);
            UseDepartmentDel = useDepartmentDao.countDelByStartTime(districtId,startTime);
        }else {
            UseDepartmentAdd = useDepartmentDao.countAddByTime(districtId,startTime,endTime);
            UseDepartmentDel = useDepartmentDao.countDelByTime(districtId,startTime,endTime);
        }
        hashMap.put("add",UseDepartmentAdd);
        hashMap.put("del",UseDepartmentDel);
        return hashMap;
    }

    /**
     * 统计每一栏总数
     * @param counts
     * @return
     */
     public List<Count> getSum(List<Count> counts){
         int useDepartmentAllSum = 0;
         int useDepartmentWorkSum = 0;
         int useDepartmentDelSum = 0;
         int useDepartmentAdd = 0;
         int useDepartmentDel = 0;

         for(Count count : counts){
             useDepartmentAllSum = useDepartmentAllSum+ count.getAllSum();
             useDepartmentDelSum = useDepartmentDelSum+ count.getDelSum();
             useDepartmentWorkSum =useDepartmentWorkSum + count.getWorkSum();
             useDepartmentAdd = useDepartmentAdd+ count.getAddCount();
             useDepartmentDel = useDepartmentDel+ count.getDeleteCount();
         }

         Count count = new Count("总计",useDepartmentAllSum,useDepartmentWorkSum,useDepartmentDelSum,useDepartmentAdd,useDepartmentDel);
         counts.add(count);
         return counts;

     }

}
