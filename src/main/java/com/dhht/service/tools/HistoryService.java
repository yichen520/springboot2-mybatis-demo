package com.dhht.service.tools;

import com.dhht.model.OperatorRecord;
import com.dhht.model.OperatorRecordDetail;
import com.dhht.model.User;

import java.util.List;

public interface HistoryService {
    List<OperatorRecord> showUpdteHistory(String flag,int type);

    boolean insertUpdateRecord(Object newData, Object oldDate, String operatorRecordId,String[] ignore);

    boolean insertOperateRecord(User user, String flag, String entityUUid, String entityName,int type,String operateRecordUUid);
}
