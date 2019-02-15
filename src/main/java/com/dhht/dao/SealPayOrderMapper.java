package com.dhht.dao;

import com.dhht.model.SealPayOrder;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SealPayOrderMapper {

    int insert(SealPayOrder record);

    SealPayOrder selectByPrimaryKey(String id);

    SealPayOrder selectBySealId(String sealId);

    int updateEvaluationStatus(@Param("id") String id);

    int updateRefundStatus (@Param("refundStatus") String refundStatus,@Param("id") String id);

    int updatePayStatus(@Param("payWay") String payWay,@Param("id") String id);
}