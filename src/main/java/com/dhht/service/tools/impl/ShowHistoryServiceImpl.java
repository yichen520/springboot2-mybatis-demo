package com.dhht.service.tools.impl;

import com.dhht.dao.OperatorRecordDetailMapper;
import com.dhht.dao.OperatorRecordMapper;
import com.dhht.model.OperatorRecord;
import com.dhht.model.OperatorRecordDetail;
import com.dhht.service.tools.ShowHistoryService;
import com.dhht.util.DictionaryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service(value = "showHistoryService")
public class ShowHistoryServiceImpl implements ShowHistoryService {

    @Autowired
    private OperatorRecordMapper operatorRecordMapper;
    @Autowired
    private OperatorRecordDetailMapper operatorRecordDetailMapper;
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
            if(operatorRecordDetail.getPropertyName().equals("nothing")){}else {
                String value = DictionaryUtil.getDictionaryValue(type,operatorRecordDetail.getPropertyName());
                operatorRecordDetail.setPropertyName(value);
                result.add(operatorRecordDetail);
            }
        }
        return result;
    }
}
