package com.dhht.service.index;

import com.dhht.model.IndexCount;
import com.dhht.model.IndexOverview;
import com.dhht.model.User;

import java.util.List;

public interface RecordDepartmentIndexService {

    List<IndexOverview> overview(String districtId);

    List<IndexCount> ranking(String districtId);

    List<IndexCount> polyline(String districtId);

}
