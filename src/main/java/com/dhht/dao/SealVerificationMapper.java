package com.dhht.dao;

import com.dhht.model.SealVerification;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SealVerificationMapper {
    int deleteByPrimaryKey(String id);

    int insert(SealVerification record);

    int insertSelective(SealVerification record);

    SealVerification selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SealVerification record);

    int updateByPrimaryKey(SealVerification record);

    SealVerification selectBySealId(String sealId);

    SealVerification selectBySealIdAndFlag(@Param("sealId") String sealId,@Param("flag") String flag);

    SealVerification selectBySealIdAndReason(@Param("id") String id,@Param("rejectReason") String rejectReason);

    int updateBySealIdAndFlag(SealVerification record);
}