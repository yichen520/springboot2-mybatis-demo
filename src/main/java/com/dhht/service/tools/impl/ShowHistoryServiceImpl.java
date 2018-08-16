package com.dhht.service.tools.impl;

import com.dhht.dao.OperatorRecordDetailMapper;
import com.dhht.dao.OperatorRecordMapper;
import com.dhht.model.OperatorRecord;
import com.dhht.model.OperatorRecordDetail;
import com.dhht.service.District.DistrictService;
import com.dhht.service.tools.ShowHistoryService;
import com.dhht.util.DictionaryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service(value = "showHistoryService")
public class ShowHistoryServiceImpl implements ShowHistoryService {

    @Autowired
    private OperatorRecordMapper operatorRecordMapper;
    @Autowired
    private OperatorRecordDetailMapper operatorRecordDetailMapper;
    @Autowired
    private DistrictService districtService;
    /**
     * 新版查询
     * @param flag
     * @return
     */
    @Override
    public List<OperatorRecord> showUpdteHistory(String flag,int type) {
        List<OperatorRecord> operatorRecords =  operatorRecordMapper.selectByFlag(flag);
        for(OperatorRecord operatorRecord : operatorRecords){
            List<OperatorRecordDetail> operatorRecordDetails = operatorRecordDetailMapper.selectByOperateId(operatorRecord.getId());
            if(operatorRecordDetails.size()>0){
                operatorRecord.setOperatorRecordDetails(setChineseValue(operatorRecordDetails,type));
            }
        }
        return operatorRecords;
    }

    /**
     * 设置中文字段名
     * @param operatorRecordDetails
     * @param type
     * @return
     */
    private List<OperatorRecordDetail> setChineseValue(List<OperatorRecordDetail> operatorRecordDetails,int type){
        List<OperatorRecordDetail> result = new ArrayList<OperatorRecordDetail>();
        for (OperatorRecordDetail operatorRecordDetail:operatorRecordDetails){
            if(operatorRecordDetail.getPropertyName().equals("nothing")){

            }else if (operatorRecordDetail.getPropertyName().equals("districtId")){
                String value = DictionaryUtil.getDictionaryValue(type,operatorRecordDetail.getPropertyName());
                operatorRecordDetail.setPropertyName(value);
                String oldDistrict = districtService.selectByDistrictId(operatorRecordDetail.getOldValue());
                operatorRecordDetail.setOldValue(oldDistrict);
                String newDistrict = districtService.selectByDistrictId(operatorRecordDetail.getNewValue());
                operatorRecordDetail.setNewValue(newDistrict);
                result.add(operatorRecordDetail);
            }else {
                String value = DictionaryUtil.getDictionaryValue(type,operatorRecordDetail.getPropertyName());
                operatorRecordDetail.setPropertyName(value);
                operatorRecordDetail.setNewValue(setDateString(operatorRecordDetail.getNewValue()));
                operatorRecordDetail.setOldValue(setDateString(operatorRecordDetail.getOldValue()));
                result.add(operatorRecordDetail);
            }
        }
        return result;
    }

    /**
     * 判断是否是时间格式
     * @param value
     * @return
     */
    public static String setDateString(String value){
        SimpleDateFormat startTime = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        SimpleDateFormat endTime = new SimpleDateFormat("yyyy-MM-dd");

        try {
           if(value.contains("CST")){
               Date date = startTime.parse(value);
               String newDate = endTime.format(date);
               return newDate;
           }
        }catch (Exception e){
            return value;
        }
        return value;
        }
}
