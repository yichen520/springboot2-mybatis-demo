//package com.dhht.service.seal.Impl;
//
//import com.dhht.dao.MakedepartmentMapper;
//import com.dhht.dao.SealDao;
//import com.dhht.model.Count;
//import com.dhht.model.DistrictMenus;
//import com.dhht.service.District.DistrictService;
//import com.dhht.service.make.MakeDepartmentCuontService;
//import com.dhht.util.StringUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
///**
// * 2018/7/7 create by cy
// */
//@Service(value = "sealCountService")
//@Transactional
//public class SealCountServiceImp implements MakeDepartmentCuontService{
//    @Autowired
//    private SealDao sealDao;5e
//    @Autowired
//    private DistrictService districtService;
//
//    /**
//     * 统计区域下所有的所有的制作单位.
//     * @param districtId
//     * @return
//     */
//    @Override
//    public List<Count> countAllDepartment(String districtId, String startTime, String endTime) {
//        List<DistrictMenus> districtList = getDistrictList(districtId);
//        List<Count> counts = new ArrayList<>();
//        for (DistrictMenus districtMenus : districtList){
//            String id =  StringUtil.getDistrictId(districtMenus.getDistrictId());
//            int departmentAll = makedepartmentMapper.countAllDepartment(id);
//            int departmentWork = makedepartmentMapper.countWorkDepartment(id);
//            int departmentDel = makedepartmentMapper.countDeleteDepartment(id);
//            int add = countByTime(id,startTime,endTime).get("add");
//            int del = countByTime(id,startTime,endTime).get("del");
//            counts.add(new Count(districtMenus.getDistrictName(),departmentAll,departmentWork,departmentDel,add,del));
//        }
//        return getSum(counts);
//    }
//
//    /**
//     * 返回区域列表
//     * @param district
//     * @return
//     */
//    public List<DistrictMenus> getDistrictList(String district){
//        List<DistrictMenus> list = districtService.selectOneDistrict(district);
//        return list.get(0).getChildren();
//    }
//
//    /**
//     * 统计每一栏的总量
//     * @param list
//     * @return
//     */
//    public List<Count> getSum(List<Count> list){
//        int departmentAllSum = 0;
//        int departmentWorkSum = 0;
//        int departmentDelSum = 0;
//        int departmentAdd = 0;
//        int departmentDel = 0;
//
//        for(Count counts : list){
//           departmentAllSum = departmentAllSum+ counts.getAllSum();
//           departmentDelSum = departmentDelSum+ counts.getDelSum();
//           departmentWorkSum =departmentWorkSum + counts.getWorkSum();
//           departmentAdd = departmentAdd+ counts.getAddCount();
//           departmentDel = departmentDel+ counts.getDeleteCount();
//           }
//
//        Count count = new Count("总计",departmentAllSum,departmentWorkSum,departmentDelSum,departmentAdd,departmentDel);
//        list.add(count);
//        return list;
//    }
//
//    /**
//     * 追加时间条件查询
//     * @param districtId
//     * @param startTime
//     * @param endTime
//     * @return
//     */
//    public HashMap<String,Integer> countByTime(String districtId,String startTime,String endTime ){
//        HashMap<String,Integer> hashMap = new HashMap<>();
//        Integer departmentAdd = 0;
//        Integer departmentDel = 0;
//
//        if(startTime==null&&endTime==null){
//            departmentAdd = makedepartmentMapper.countWorkDepartment(districtId);
//            departmentDel = makedepartmentMapper.countDeleteDepartment(districtId);
//        }else if(startTime==null&&endTime!=null){
//            departmentAdd = makedepartmentMapper.countAddDepartmentByEndTime(districtId,endTime);
//            departmentDel = makedepartmentMapper.countDelDepartmentByEndTime(districtId,endTime);
//        }else if(startTime!=null&&endTime==null){
//            departmentAdd = makedepartmentMapper.countAddDepartmentByStartTime(districtId,startTime);
//            departmentDel = makedepartmentMapper.countDelDepartmentByStartTime(districtId,startTime);
//        }else {
//            departmentAdd = makedepartmentMapper.countAddDepartmentByTime(districtId,startTime,endTime);
//            departmentDel = makedepartmentMapper.countDelDepartmentByTime(districtId,startTime,endTime);
//        }
//
//        hashMap.put("add",departmentAdd);
//        hashMap.put("del",departmentDel);
//        return hashMap;
//    }
//
//
//}
