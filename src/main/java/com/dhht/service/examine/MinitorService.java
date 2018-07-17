package com.dhht.service.examine;

import com.dhht.model.Examine;
import com.dhht.model.ExamineDetail;

import java.util.List;

/**
 * Created by Administrator on 2018/4/19.
 */
public interface MinitorService {

    boolean add(Examine examine);

    List<Examine> info();

    boolean delete(String id);

    boolean update(Examine examine);

    List<ExamineDetail> items(String id);

    boolean itemdelete (String id);

    boolean itemupdate(ExamineDetail examineDetail);

    List<Examine> selectExamineForm(String districtId);

}
