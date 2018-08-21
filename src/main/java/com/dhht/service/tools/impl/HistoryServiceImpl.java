package com.dhht.service.tools.impl;

import com.dhht.dao.OperatorRecordDetailMapper;
import com.dhht.dao.OperatorRecordMapper;
import com.dhht.model.*;
import com.dhht.service.District.DistrictService;
import com.dhht.service.tools.HistoryService;
import com.dhht.sync.SyncOperateType;
import com.dhht.util.CompareFieldsUtil;
import com.dhht.util.DateUtil;
import com.dhht.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service(value = "showHistoryService")
public class HistoryServiceImpl implements HistoryService {

    @Autowired
    private OperatorRecordMapper operatorRecordMapper;
    @Autowired
    private OperatorRecordDetailMapper operatorRecordDetailMapper;
    @Autowired
    private DistrictService districtService;

    private final static String DISTRICT_ID = "行政区域划分";

    private final static String NULL_VALUE = "nothing";
    /**
     * 查询历史记录
     * @param flag
     * @return
     */
    @Override
    public List<OperatorRecord> showUpdteHistory(String flag,int type) {
        List<OperatorRecord> operatorRecords =  operatorRecordMapper.selectByFlag(flag);
        for(OperatorRecord operatorRecord : operatorRecords){
            List<OperatorRecordDetail> operatorRecordDetails = operatorRecordDetailMapper.selectByOperateId(operatorRecord.getId());
            if(operatorRecordDetails.size()>0){
                operatorRecord.setOperatorRecordDetails(setSpecialValue(operatorRecordDetails,type));
            }
        }
        return operatorRecords;
    }

    /**
     * 添加更改记录
     * @param newData
     * @param oldDate
     * @param operatorRecordId
     * @param ignore
     * @return
     */
    @Override
    public boolean insertUpdateRecord(Object newData, Object oldDate, String operatorRecordId,String[] ignore) {
        List<CompareResult> compareResults = CompareFieldsUtil.compareField(oldDate, newData, ignore);
        OperatorRecordDetail operatorRecordDetail = new OperatorRecordDetail();
        if(compareResults.size()==0){
            operatorRecordDetail.setId(UUIDUtil.generate());
            operatorRecordDetail.setEntityOperateRecordId(operatorRecordId);
            operatorRecordDetail.setPropertyName(NULL_VALUE);
            int o = operatorRecordDetailMapper.insert(operatorRecordDetail);
            if(o<0){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return false;
            }
        }
        for (CompareResult compareResult:compareResults) {
            operatorRecordDetail.setId(UUIDUtil.generate());
            operatorRecordDetail.setEntityOperateRecordId(operatorRecordId);
            try {
                operatorRecordDetail.setOldValue(compareResult.getOldValue().toString());
            }catch (NullPointerException e){
                operatorRecordDetail.setOldValue(" ");
            }
            try {
                operatorRecordDetail.setNewValue(compareResult.getNewValue().toString());
            }catch (NullPointerException e){
                operatorRecordDetail.setNewValue(" ");
            }
            operatorRecordDetail.setPropertyName(compareResult.getPropertyName());
            int o = operatorRecordDetailMapper.insert(operatorRecordDetail);
            if(o<0){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return false;
            }
        }
        return true;
    }

    /**
     * 操作记录存储
     * @param user
     * @param flag
     * @param entityUUid
     * @param entityName
     * @param type
     * @param operateRecordUUid
     * @return
     */
    @Override
    public boolean insertOperateRecord(User user, String flag, String entityUUid, String entityName,int type,String operateRecordUUid) {
        OperatorRecord operatorRecord = new OperatorRecord();
        operatorRecord.setFlag(flag);
        operatorRecord.setId(operateRecordUUid);
        operatorRecord.setOperateUserId(user.getId());
        operatorRecord.setOperateUserRealname(user.getRealName());
        operatorRecord.setOperateEntityId(entityUUid);
        operatorRecord.setOperateEntityName(entityName);
        operatorRecord.setOperateType(type);
        operatorRecord.setOperateTypeName(SyncOperateType.getOperateTypeName(type));
        operatorRecord.setOperateTime(DateUtil.getCurrentTime());
        int i = operatorRecordMapper.insert(operatorRecord);
        if(i>0){
            return true;
        }
        return false;
    }

    /**
     * 设置参数值得设定
     * @param operatorRecordDetails
     * @param type
     * @return
     */
    private List<OperatorRecordDetail> setSpecialValue(List<OperatorRecordDetail> operatorRecordDetails,int type){
        List<OperatorRecordDetail> result = new ArrayList<OperatorRecordDetail>();
        for (OperatorRecordDetail operatorRecordDetail:operatorRecordDetails){
            if(operatorRecordDetail.getPropertyName().equals(NULL_VALUE)){

            }else if (operatorRecordDetail.getPropertyName().equals(DISTRICT_ID)){
                String oldDistrict = districtService.selectByDistrictId(operatorRecordDetail.getOldValue());
                operatorRecordDetail.setOldValue(oldDistrict);
                String newDistrict = districtService.selectByDistrictId(operatorRecordDetail.getNewValue());
                operatorRecordDetail.setNewValue(newDistrict);
                result.add(operatorRecordDetail);
            }else {
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
