package com.dhht.service.examine.impl;

import com.dhht.dao.ExamineDetailMapper;
import com.dhht.dao.ExamineMapper;
import com.dhht.dao.MinitorMapper;
import com.dhht.model.Examine;
import com.dhht.model.ExamineDetail;
import com.dhht.service.examine.MinitorService;
import com.dhht.util.StringUtil;
import com.dhht.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("minitorService")
@Transactional
public class MinitorServiceImpl implements MinitorService {

    @Autowired
    private ExamineDetailMapper examineDetailMapper;
    @Autowired
    private ExamineMapper examineMapper;

    @Override
    public List<Examine> info() {
      return   examineMapper.selectExamine();
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
}
