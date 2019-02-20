package com.dhht.service.evaluate.Impl;

import com.dhht.dao.EvaluateMapper;
import com.dhht.dao.MakedepartmentMapper;
import com.dhht.model.*;
import com.dhht.service.evaluate.EvaluateService;
import com.dhht.service.make.MakeDepartmentService;
import com.dhht.service.order.OrderService;
import com.dhht.util.DateUtil;
import com.dhht.util.ResultUtil;
import com.dhht.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.xml.ws.Action;
import java.util.List;
import java.util.UUID;

/**
 * @author 徐正平
 * @Date 2018/12/29 9:14
 */
@Service(value = "evaluateService")
@Transactional
public class EvaluateServiceImpl implements EvaluateService {
    @Autowired
    private EvaluateMapper evaluateMapper;
    @Autowired
    private MakeDepartmentService makeDepartmentService;
    @Autowired
    private OrderService orderService;

    @Override
    public int insert(Evaluate evaluate, WeChatUser user,String sealId) {
        evaluate.setId(UUIDUtil.generate());
        if(user == null){
            return ResultUtil.isNoLoginUser;
        }
        MakeDepartmentSimple makedepartment = makeDepartmentService.selectByDepartmentCode(evaluate.getMakeDepartmentId());
        evaluate.setFlag(makedepartment.getFlag());
        evaluate.setUserId(user.getId());
        evaluate.setUserName(user.getTelphone());
        evaluate.setEvaluateTime(DateUtil.getCurrentTime());
        int result = evaluateMapper.insertSelective(evaluate);
        int orderResult = orderService.updateEvaluationStatus(sealId,false);
        if(result>0&&orderResult>0){
           return ResultUtil.evaluationOk;
        }
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        return ResultUtil.evaluationError;
    }

    @Override
    public List<Evaluate> selectEvaluateList(Evaluate evaluate) {
        return evaluateMapper.selectEvaluateList(evaluate);
    }

    @Override
    public int deleteEvaluate(String id) {
        return evaluateMapper.deleteByPrimaryKey(id);
    }
}
