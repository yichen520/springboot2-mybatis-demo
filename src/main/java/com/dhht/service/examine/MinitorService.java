package com.dhht.service.examine;

import com.dhht.model.Examine;
import com.dhht.model.ExamineCount;
import com.dhht.model.ExamineDetail;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/19.
 */
public interface MinitorService {

    boolean add(Examine examine);

    List<Examine> info(String districtId,String name,String remark);

    boolean delete(String id);

    boolean update(Examine examine);

    List<ExamineDetail> items(String id);

    boolean itemdelete (String id);

    boolean itemupdate(ExamineDetail examineDetail);

    List<Examine> selectExamineForm(String districtId);

    List<ExamineCount> countExamine(Map map, HttpServletRequest httpServletRequest);

    List<ExamineCount> countPunish(Map map, HttpServletRequest httpServletRequest);

    List<ExamineCount> countemployeePunish(Map map, HttpServletRequest httpServletRequest);

    boolean itemadd(List<ExamineDetail> examineDetails);

}
