package com.dhht.service.recipients.impl;

import com.dhht.dao.RecipientsMapper;
import com.dhht.model.Recipients;
import com.dhht.model.User;
import com.dhht.service.recipients.RecipientsService;
import com.dhht.util.ResultUtil;
import com.dhht.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.transform.Result;
import java.util.List;

@Service("ecipientsService")
@Transactional
public class RecipientsServiceImpl implements RecipientsService {

    @Autowired
    private RecipientsMapper recipientsMapper;

    @Override
    public int insertRecipients(Recipients recipients,User user) {
        recipients.setId(UUIDUtil.generate());
        recipients.setLoginTelphone(user.getTelphone());
        int recipientsMapperInsert  = recipientsMapper.insertSelective(recipients);
        if(recipientsMapperInsert<0){
            return ResultUtil.isFail;
        }else{
            return ResultUtil.isSuccess;
        }
    }

    @Override
    public int updateRecipients(Recipients recipients,User user) {
        int recipientsUpdate = recipientsMapper.updateByPrimaryKeySelective(recipients);
        if(recipientsUpdate<0){
            return ResultUtil.isFail;
        }else{
            return ResultUtil.isSuccess;
        }
    }

    @Override
    public int deleteRecipients(String id) {
        int recipientsDelete = recipientsMapper.deleteByPrimaryKey(id);
        if(recipientsDelete<0){
            return ResultUtil.isFail;
        }else{
            return ResultUtil.isSuccess;
        }
    }

    @Override
    public List<Recipients> recipientsList(User user){
        String loginTelPhone = user.getTelphone();
        List<Recipients> recipientsList = recipientsMapper.selectAllByTelphone(loginTelPhone);
        return recipientsList;

    }
}
