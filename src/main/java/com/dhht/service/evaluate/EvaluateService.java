package com.dhht.service.evaluate;

import com.dhht.model.Evaluate;
import com.dhht.model.User;
import com.dhht.model.WeChatUser;

import java.util.List;

/**
 * @author 徐正平
 * @Date 2018/12/29 9:14
 */
public interface EvaluateService {
     int insert(Evaluate evaluate, WeChatUser user) ;

    List<Evaluate> selectEvaluateList(Evaluate evaluate);

    int deleteEvaluate(String id);
}
