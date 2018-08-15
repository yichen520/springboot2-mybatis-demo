package com.dhht.service.examine.impl;

import com.dhht.dao.*;
import com.dhht.model.*;
import com.dhht.model.pojo.ExamineItemsDetail;
import com.dhht.service.District.DistrictService;
import com.dhht.service.examine.MinitorService;
import com.dhht.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

import java.util.*;

@Service("minitorService")
@Transactional
public class MinitorServiceImpl implements MinitorService {

    @Autowired
    private ExamineDetailMapper examineDetailMapper;
    @Autowired
    private ExamineMapper examineMapper;
    @Autowired
    private DistrictService districtService;
    @Autowired
    private MakePunishRecordMapper makePunishRecordMapper;
    @Autowired
    private EmployeePunishRecordMapper employeePunishRecordMapper;

    @Override
    public List<Examine> info(String districtId, String name, String remark) {
        return   examineMapper.selectExamine(districtId,name,remark);
    }


    /**
     * 添加检查
     * @param examine
     * @return
     */
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

    /**
     * 删除
     * @param id
     * @return
     */
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

    /**
     * 更新
     * @param examine
     * @return
     */
    @Override
    public boolean update(Examine examine) {
        if (examineMapper.updateByPrimaryKeySelective(examine)== 1){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 获得检查项
     * @param id
     * @return
     */
    @Override
    public List<ExamineDetail> items(String id) {
        return examineDetailMapper.selectByExamineId(id);
    }

    /**
     * 删除检查项
     * @param id
     * @return
     */
    @Override
    public boolean itemdelete(String id)  {

            if (examineDetailMapper.deleteByPrimaryKey(id)== 1){
                return true;
            }else {
                return false;
            }
    }

    /**
     * 更新检查项
     * @param examineDetail
     * @return
     */
    @Override
    public boolean itemupdate(ExamineDetail examineDetail) {
        if (examineDetailMapper.updateByPrimaryKeySelective(examineDetail)== 1){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 获取检查表格
     * @param districtId
     * @return
     */
    @Override
    public List<Examine> selectExamineForm(String districtId) {
        String dis1 = districtId.substring(0,2)+"0000";
        String dis2 = districtId.substring(0,4)+"00";
        return examineMapper.selectExamineForm(dis1,dis2,districtId);
    }

    /**
     * 检查统计
     * @param map
     * @param httpServletRequest
     * @return
     */
    @Override
    public List<ExamineCount> countExamine(Map map, HttpServletRequest httpServletRequest) {
        String month = (String)map.get("month");
        List<String>  list =(List<String>)map.get("districts");
        User user= (User)httpServletRequest.getSession().getAttribute("user");
        if (list == null){
            String str = user.getDistrictId();
            list =new ArrayList<>();
            list.add(str);
        }
        List<DistrictMenus> districtIds =  districtService.selectDistrictByArray(list);
        List<ExamineCount> examineCounts = new ArrayList<>();

        int countAllSum=0;
        for (DistrictMenus districtId : districtIds) {
            int countSum=0;
            if (districtId.getChildren() != null){
                for (DistrictMenus districtchilrenId : districtId.getChildren()) {
                    ExamineCount examineCount = examineMapper.selectExamineCountByDistrict(districtchilrenId.getDistrictId(),month);
                    if (examineCount.getCountNum()!=0){
                        countSum = countSum+examineCount.getCountNum();
                        examineCounts.add(examineCount);
                    }
                }
            }
            countAllSum = countAllSum + countSum;
            ExamineCount examineCount = new ExamineCount(districtId.getDistrictName()+"(小计)",countSum);
            examineCounts.add(examineCount);
        }
        ExamineCount all = new ExamineCount("总计",countAllSum);
        examineCounts.add(all);
        return examineCounts;
    }

    /**
     * 处罚统计
     * @param map
     * @param httpServletRequest
     * @return
     */
    @Override
    public List<ExamineCount> countPunish(Map map, HttpServletRequest httpServletRequest) {
        String month = (String)map.get("month");
        List<String>  list =(List<String>)map.get("districts");
        User user= (User)httpServletRequest.getSession().getAttribute("user");
        if (list == null){
            String str = user.getDistrictId();
            list = new ArrayList<>();
            list.add(str);
        }
        List<DistrictMenus> districtIds =  districtService.selectDistrictByArray(list);
        List<ExamineCount> examineCounts = new ArrayList<>();
        int countAllSum=0;
        for (DistrictMenus districtId : districtIds) {
            int countSum=0;
            if (districtId.getChildren() != null){
                for (DistrictMenus districtchilrenId : districtId.getChildren()) {
                    ExamineCount examineCount = makePunishRecordMapper.selectPunishCountByDistrict(districtchilrenId.getDistrictId(),month);
                    if (examineCount.getCountNum()!=0){
                        countSum = countSum+examineCount.getCountNum();
                        examineCounts.add(examineCount);
                    }
                }
            }
            countAllSum = countAllSum + countSum;
            ExamineCount examineCount = new ExamineCount(districtId.getDistrictName()+"(小计)",countSum);
            examineCounts.add(examineCount);
        }
        ExamineCount all = new ExamineCount("总计",countAllSum);
        examineCounts.add(all);
        return examineCounts;
    }

    /**
     * 从业人员处罚统计
     * @param map
     * @param httpServletRequest
     * @return
     */
    @Override
    public List<ExamineCount> countemployeePunish(Map map, HttpServletRequest httpServletRequest) {
        String month = (String)map.get("month");
        List<String>  list =(List<String>)map.get("districts");
        User user= (User)httpServletRequest.getSession().getAttribute("user");
        if (list == null){

            String str = user.getDistrictId();
            list = new ArrayList<>();
            list.add(str);
        }
        List<DistrictMenus> districtIds =  districtService.selectDistrictByArray(list);
        List<ExamineCount> examineCounts = new ArrayList<>();
        int countAllSum =  0;
        for (DistrictMenus districtId : districtIds) {
            int countSum = 0 ;
            if (districtId.getChildren() != null){
                for (DistrictMenus districtchilrenId : districtId.getChildren()) {
                    ExamineCount examineCount = employeePunishRecordMapper.selectPunishCountByDistrict(districtchilrenId.getDistrictId(),month);
                    if (examineCount.getCountNum()!=0){
                        countSum = countSum+examineCount.getCountNum();
                        examineCounts.add(examineCount);
                    }
                }
            }

              countAllSum = countAllSum + countSum;
              ExamineCount examineCount = new ExamineCount(districtId.getDistrictName()+"(小计)",countSum);
              examineCounts.add(examineCount);
        }
        ExamineCount all = new ExamineCount("总计",countAllSum);
        examineCounts.add(all);
        return examineCounts;
    }

    @Override
    public boolean itemadd(List<ExamineDetail> examineDetails) {
            for (ExamineDetail examineDetail : examineDetails) {
                if( examineDetail.getId()==null) {
                    examineDetail.setId(UUIDUtil.generate());
                    examineDetailMapper.insertSelective(examineDetail);
                }
            }
            return true;
    }

    @Override
    public List<ExamineItemsDetail> itemsWithKey(String id) {
        return examineDetailMapper.selectByitemsWithKey(id);
    }
}
