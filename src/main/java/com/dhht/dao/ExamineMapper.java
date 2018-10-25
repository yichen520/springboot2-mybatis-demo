package com.dhht.dao;

import com.dhht.model.Examine;
import com.dhht.model.ExamineCount;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamineMapper {
    int deleteByPrimaryKey(String id);

    int insert(Examine record);

    int insertSelective(Examine record);

    Examine selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Examine record);

    int updateByPrimaryKey(Examine record);

    List<Examine> selectExamine(@Param("dis1")String districtId1,@Param("dis2")String districtI2,@Param("dis3")String districtId,@Param("name")String name,@Param("remark")String remark);

    List<Examine> selectExamineForm(@Param("dis1")String dis1,@Param("dis2")String dis2,@Param("dis3")String dis3);

    ExamineCount selectExamineCountByDistrict(@Param("district")String district,@Param("month")String month) ;

    ExamineCount selectExamineCountByCityDistrict(@Param("district")String district,@Param("month")String month);

    List<Examine> selectExamine1(@Param("dis1")String substring, @Param("name")String name, @Param("remark")String remark);

    List<Examine> selectExamine2(@Param("dis1")String dis1, @Param("dis2")String substring,@Param("name") String name,@Param("remark") String remark);
}