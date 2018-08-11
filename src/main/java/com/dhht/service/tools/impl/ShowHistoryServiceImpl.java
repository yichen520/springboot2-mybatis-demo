package com.dhht.service.tools.impl;

import com.dhht.dao.OperatorRecordDetailMapper;
import com.dhht.dao.OperatorRecordMapper;
import com.dhht.model.OperatorRecord;
import com.dhht.model.OperatorRecordDetail;
import com.dhht.service.tools.ShowHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("showHistoryService")
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
    public List<OperatorRecord> showUpdteHistory(String flag) {
        List<OperatorRecord> operatorRecords =  operatorRecordMapper.selectByFlag(flag);
        for(OperatorRecord operatorRecord : operatorRecords){
            List<OperatorRecordDetail> operatorRecordDetails = operatorRecordDetailMapper.selectByOperateId(operatorRecord.getId());
            if(operatorRecordDetails.size()>0){
                operatorRecord.setOperatorRecordDetails(operatorRecordDetails);
            }
        }
        return operatorRecords;
    }

}
