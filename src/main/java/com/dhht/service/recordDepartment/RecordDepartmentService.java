package com.dhht.service.recordDepartment;

import com.dhht.model.*;
import com.dhht.model.pojo.CommonHistoryVO;
import com.dhht.model.pojo.RecordDepartmentHistoryVO;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface RecordDepartmentService {
    PageInfo<RecordDepartment> selectByDistrictId(String Id,int pageSize,int pageNum);

    List<RecordDepartment> selectByDistrictId(String Id);

    PageInfo<RecordDepartment> selectAllRecordDepartMent(int pageSize,int pageNum);

    int insert(RecordDepartment recordDepartment,User updateUser);

    RecordDepartment selectByCode(String code);

    int deleteById(String id);

    int updateById(RecordDepartment recordDepartment,User updateUser);


    RecordDepartment selectByPhone(String phone);

    boolean insertPunish(User user,ExamineRecord examineRecord);

    List<ExamineRecord>  findPunish(String makedepartmentName,String startTime,String endTime,String districtId);
}
