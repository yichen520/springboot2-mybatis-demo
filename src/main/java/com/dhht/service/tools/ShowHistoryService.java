package com.dhht.service.tools;

import com.dhht.model.OperatorRecord;

import java.util.List;

public interface ShowHistoryService {
    List<OperatorRecord> showUpdteHistory(String flag,int type);
}
