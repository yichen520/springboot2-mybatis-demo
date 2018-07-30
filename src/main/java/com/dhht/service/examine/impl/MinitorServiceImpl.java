package com.dhht.service.examine.impl;

import com.dhht.dao.*;
import com.dhht.model.*;
import com.dhht.service.District.DistrictService;
import com.dhht.service.examine.MinitorService;
import com.dhht.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.*;

@Service("minitorService")
@Transactional
public class MinitorServiceImpl implements MinitorService {

    @Resource
    private ExamineDetailMapper examineDetailMapper;
    @Resource
    private ExamineMapper examineMapper;
    @Resource
    private DistrictService districtService;
    @Resource
    private MakePunishRecordMapper makePunishRecordMapper;
    @Resource
    private EmployeePunishRecordMapper employeePunishRecordMapper;
    @Resource
    @Override
    public List<Examine> info(String districtId,String name,String remark) {
      return   examineMapper.selectExamine(districtId,name,remark);
    }


    @Override
    public boolean add(Examine examine) {
        examine.setId(UUIDUtil.generate());
        examineMapper.insertSelective(examine);
        if (examine.getExamineDetails()!= null){
            for (int i = 0; i < examine.getExamineDetails().size(); i++) {
                ExamineDetail examineDetail = examine.getExamineDetails().get(i);
                examineDetail.setId(UUIDUtil.generate());
                examineDetail.setExamineTypeId(examine.getId());
                examineDetailMapper.insertSelective(examineDetail);
            }
        }
      return true;
    }

    @Override
    public boolean delete(String id) {

        if (examineMapper.deleteByPrimaryKey(id)== 1){
            List<ExamineDetail> examineDetails = examineDetailMapper.selectByExamineId(id);
            for (ExamineDetail examineDetail:examineDetails) {
                examineDetailMapper.deleteByPrimaryKey(examineDetail.getId());
            }
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean update(Examine examine) {
        if (examineMapper.updateByPrimaryKeySelective(examine)== 1){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public List<ExamineDetail> items(String id) {
        return examineDetailMapper.selectByExamineId(id);
    }

    @Override
    public boolean itemdelete(String id)  {

            if (examineDetailMapper.deleteByPrimaryKey(id)== 1){
                return true;
            }else {
                return false;
            }
    }

    @Override
    public boolean itemupdate(ExamineDetail examineDetail) {
        if (examineDetailMapper.updateByPrimaryKeySelective(examineDetail)== 1){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public List<Examine> selectExamineForm(String districtId) {
        String dis1 = districtId.substring(0,2)+"0000";
        String dis2 = districtId.substring(0,4)+"00";
        return examineMapper.selectExamineForm(dis1,dis2,districtId);
    }

    @Override
    public List<ExamineCount> countExamine(Map map, HttpServletRequest httpServletRequest) {
        String month = (String)map.get("month");
        List<String>  list =(List<String>)map.get("districts");
        User user= (User)httpServletRequest.getSession().getAttribute("user");
        if (list == null){
            String str = user.getDistrictId().substring(0,2);
            list=new ArrayList<String>();
            list.add(str+"0000");
        }
        List<DistrictMenus> districtIds =  districtService.selectDistrictByArray(list);
        List<ExamineCount> examineCounts = new ArrayList<>();
        for (DistrictMenus districtId : districtIds) {
            if (districtId.getChildren() != null){
                for (DistrictMenus districtchilrenId : districtId.getChildren()) {
                    ExamineCount examineCount = examineMapper.selectExamineCountByDistrict(districtchilrenId.getDistrictId(),month);
                    if (examineCount.getCountNum()!=0){
                        examineCounts.add(examineCount);
                    }
                }
            }
            String dis = districtId.getDistrictId().substring(0,4);
            ExamineCount examineCount = examineMapper.selectExamineCountByCityDistrict(dis,month);
            if (examineCount.getCountNum()!=0){
                examineCounts.add(examineCount);
            }
        }
        return examineCounts;
    }

    @Override
    public List<ExamineCount> countPunish(Map map, HttpServletRequest httpServletRequest) {
        String month = (String)map.get("month");
        List<String>  list =(List<String>)map.get("districts");
        User user= (User)httpServletRequest.getSession().getAttribute("user");
        if (list == null){
            String str = user.getDistrictId().substring(0,2);
            list=new ArrayList<String>();
            list.add(str+"0000");
        }
        List<DistrictMenus> districtIds =  districtService.selectDistrictByArray(list);
        List<ExamineCount> examineCounts = new ArrayList<>();
        for (DistrictMenus districtId : districtIds) {
            if (districtId.getChildren() != null){
                for (DistrictMenus districtchilrenId : districtId.getChildren()) {
                    ExamineCount examineCount = makePunishRecordMapper.selectPunishCountByDistrict(districtchilrenId.getDistrictId(),month);
                    if (examineCount.getCountNum()!=0){
                        examineCounts.add(examineCount);
                    }
                }
            }
            String dis = districtId.getDistrictId().substring(0,4);
            ExamineCount examineCount =makePunishRecordMapper.selectPunishCountByCityDistrict(dis,month);
            if (examineCount.getCountNum()!=0){
                examineCounts.add(examineCount);
            }
        }
        return examineCounts;
    }

    @Override
    public List<ExamineCount> countemployeePunish(Map map, HttpServletRequest httpServletRequest) {
        String month = (String)map.get("month");
        List<String>  list =(List<String>)map.get("districts");
        User user= (User)httpServletRequest.getSession().getAttribute("user");
        if (list == null){
            String str = user.getDistrictId().substring(0,2);
            list=new ArrayList<String>();
            list.add(str+"0000");
        }
        List<DistrictMenus> districtIds =  districtService.selectDistrictByArray(list);
        List<ExamineCount> examineCounts = new ArrayList<>();
        for (DistrictMenus districtId : districtIds) {
            if (districtId.getChildren() != null){
                for (DistrictMenus districtchilrenId : districtId.getChildren()) {
                    ExamineCount examineCount = employeePunishRecordMapper.selectPunishCountByDistrict(districtchilrenId.getDistrictId(),month);
                    if (examineCount.getCountNum()!=0){
                        examineCounts.add(examineCount);
                    }
                }
            }
            String dis = districtId.getDistrictId().substring(0,4);
            ExamineCount examineCount =employeePunishRecordMapper.selectPunishCountByCityDistrict(dis,month);
            if (examineCount.getCountNum()!=0){
                examineCounts.add(examineCount);
            }
        }
        return examineCounts;
    }

    @Override
    public boolean itemadd(List<ExamineDetail> examineDetails) {

            for (int i = 0; i < examineDetails.size(); i++) {
                if( examineDetails.get(i).getId()==null) {
                    examineDetails.get(i).setId(UUIDUtil.generate());
                    examineDetailMapper.insertSelective(examineDetails.get(i));
                }
            }
            return true;
    }
}
