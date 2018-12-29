package com.dhht.service.evaluate.Impl;

import com.dhht.dao.EvaluateMapper;
import com.dhht.model.Evaluate;
import com.dhht.model.User;
import com.dhht.service.evaluate.EvaluateService;
import com.dhht.util.DateUtil;
import com.dhht.util.ResultUtil;
import com.dhht.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    public int insert(Evaluate evaluate, User user) {
        evaluate.setId(UUIDUtil.generate());
        if(user == null){
            return ResultUtil.isNoLoginUser;
        }
        evaluate.setUserId(user.getId());
        evaluate.setUserName(user.getUserName());
        evaluate.setEvaluateTime(DateUtil.getCurrentTime());
        int result = evaluateMapper.insertSelective(evaluate);
        return result;
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
