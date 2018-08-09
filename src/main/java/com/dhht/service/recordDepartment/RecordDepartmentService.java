package com.dhht.service.recordDepartment;

import com.dhht.model.ExamineRecord;
import com.dhht.model.OfficeCheck;
import com.dhht.model.RecordDepartment;
import com.dhht.model.User;
import com.dhht.model.pojo.CommonHistoryVO;
import com.dhht.model.pojo.RecordDepartmentHistoryVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface RecordDepartmentService {
    PageInfo<RecordDepartment> selectByDistrictId(String Id,int pageSize,int pageNum);

    List<RecordDepartment> selectByDistrictId(String Id);

    PageInfo<RecordDepartment> selectAllRecordDepartMent(int pageSize,int pageNum);

    int insert(RecordDepartment recordDepartment);

    RecordDepartment selectByCode(String code);

    int deleteById(String id);

    int updateById(RecordDepartment recordDepartment);

    List<RecordDepartment> showMore(String flag);

    List<CommonHistoryVO> showHistory(String flag);


    RecordDepartment selectByPhone(String phone);

    boolean insertPunish(User user,ExamineRecord examineRecord);

    List<ExamineRecord>  findPunish(String makedepartmentName,String startTime,String endTime,String districtId);
}
