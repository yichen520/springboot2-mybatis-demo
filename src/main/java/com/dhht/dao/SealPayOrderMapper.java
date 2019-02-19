package com.dhht.dao;

import com.dhht.model.SealPayOrder;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface SealPayOrderMapper {

    int insert(SealPayOrder record);

    SealPayOrder selectByPrimaryKey(String id);

    SealPayOrder selectBySealId(String sealId);

    int updateEvaluationStatus(@Param("id") String id,@Param("isEvaluation") boolean isEvaluation);

    int updateRefundStatus (@Param("refundStatus") String refundStatus,@Param("id") String id);

    int updatePayStatus(@Param("payWay") String payWay,@Param("id") String id,@Param("payJsOrderId") String payJsOrderId,@Param("payDate") Date payDate);
}