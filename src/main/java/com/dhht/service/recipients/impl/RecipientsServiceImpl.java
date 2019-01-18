package com.dhht.service.recipients.impl;

import com.dhht.common.AccessResult;
import com.dhht.dao.DistrictMapper;
import com.dhht.dao.RecipientsMapper;
import com.dhht.model.District;
import com.dhht.model.Recipients;
import com.dhht.model.User;
import com.dhht.service.recipients.RecipientsService;
import com.dhht.util.ResultUtil;
import com.dhht.util.StringUtil;
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
    @Autowired
    private DistrictMapper districtMapper;

    @Override
    public int insertRecipients(Recipients recipients,String telPhone) {
        recipients.setId(UUIDUtil.generate());
        recipients.setLoginTelphone(telPhone);
        recipients.setDistrictName(getDistrictName(recipients.getDistrictId()));
        int recipientsMapperInsert  = recipientsMapper.insertSelective(recipients);
        if(recipientsMapperInsert<0){
            return ResultUtil.isFail;
        }else{
            return ResultUtil.isSuccess;
        }
    }
    public    String getDistrictName(String districtId){
        String districtIds[] = StringUtil.DistrictUtil(districtId);
        String   districtName = null;
        if(districtIds[1].equals("00")&&districtIds[2].equals("00")){
            districtId = districtIds[0]+"0101";
            districtName =districtMapper.selectByDistrictId(districtId).getProvinceName();
        }else if(!districtIds[1].equals("00")&&districtIds[2].equals("00")){
            districtId = districtIds[0]+districtIds[1]+"01";
            District district = districtMapper.selectByDistrictId(districtId);
            districtName =district.getProvinceName()+"/"+district.getCityName();
        }else {
            District district = districtMapper.selectByDistrictId(districtId);
            districtName = district.getProvinceName()+"/"+district.getCityName()+"/"+district.getDistrictName();
        }
        return districtName;
    }


    @Override
    public int updateRecipients(Recipients recipients,String telPhone) {
        recipients.setDistrictName(getDistrictName(recipients.getDistrictId()));
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
    public List<Recipients> recipientsList(String loginTelphone){

        List<Recipients> recipientsList = recipientsMapper.selectAllByTelphone(loginTelphone);
        return recipientsList;

    }
}
