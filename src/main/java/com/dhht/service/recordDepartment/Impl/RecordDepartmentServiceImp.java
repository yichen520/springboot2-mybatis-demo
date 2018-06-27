package com.dhht.service.recordDepartment.Impl;

import com.dhht.dao.RecordDepartmentMapper;
import com.dhht.model.RecordDepartment;
import com.dhht.model.User;
import com.dhht.service.recordDepartment.RecordDepartmentService;
import com.dhht.service.user.UserService;
import com.dhht.util.StringUtil;
import com.dhht.util.UUIDUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 2018/6/26 create by fyc
 */

@Service(value = "RecordDepartmentService")
@Transactional
public class RecordDepartmentServiceImp implements RecordDepartmentService{
    @Autowired
    private RecordDepartmentMapper recordDepartmentMapper;
    @Autowired
    private UserService userService;

    /**
     * 带分页的查询区域下的备案单位
     * @param id
     * @param pageSize
     * @param pageNum
     * @return
     */
    @Override
    public PageInfo<RecordDepartment> selectByDistrictId(String id,int pageSize,int pageNum ) {
        List<RecordDepartment> recordDepartments = new ArrayList<>();
        String districtIds[] = StringUtil.DistrictUtil(id);
        if(districtIds[1].equals("00")&&districtIds[2].equals("00")){
            recordDepartments = recordDepartmentMapper.selectByDistrictId(districtIds[0]);
        }else if(!districtIds[1].equals("00")&&districtIds[2].equals("00")){
            recordDepartments = recordDepartmentMapper.selectByDistrictId(districtIds[0]+districtIds[1]);
        }else {
            recordDepartments = recordDepartmentMapper.selectByDistrictId(id);
        }
        PageHelper.startPage(pageNum,pageSize);
        PageInfo<RecordDepartment> pageInfo = new PageInfo(recordDepartments);
        return pageInfo;
    }

    /**
     * 不带分页的查询区域下的备案单位
     * @param id
     * @return
     */
    @Override
    public List<RecordDepartment> selectByDistrictId(String id) {
        List<RecordDepartment> list = new ArrayList<>();
        String districtIds[] = StringUtil.DistrictUtil(id);
        if(districtIds[1].equals("00")&&districtIds[2].equals("00")){
            list = recordDepartmentMapper.selectByDistrictId(districtIds[0]);
        }else if(!districtIds[1].equals("00")&&districtIds[2].equals("00")){
            list = recordDepartmentMapper.selectByDistrictId(districtIds[0]+districtIds[1]);
        }else {
            list = recordDepartmentMapper.selectByDistrictId(id);
        }
        return list;
    }

    /**
     * 带分页查询所有的备案单位
     * @param pageSize
     * @param pageNum
     * @return
     */
    @Override
    public PageInfo<RecordDepartment> selectAllRecordDepartMent(int pageSize, int pageNum) {
        List<RecordDepartment> recordDepartments= recordDepartmentMapper.selectAllRecordDepartment();
        PageHelper.startPage(pageNum,pageSize);
        PageInfo<RecordDepartment> pageInfo = new PageInfo(recordDepartments);
        return pageInfo;
    }



    /**
     * 添加备案单位
     * @param recordDepartment
     * @return
     */
    @Override
    public Boolean insert(RecordDepartment recordDepartment) {
        recordDepartment.setId(UUIDUtil.generate());
        int r = recordDepartmentMapper.insert(recordDepartment);
        int u = userService.insert(setUserByType(recordDepartment,1)).getCode();
        if(r+u==2){
            return true;
        }
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        return false;
    }

    /**
     * 根据备案单位的编号查询备案单位
     * @param code
     * @return
     */
    @Override
    public RecordDepartment selectByCode(String code) {
        RecordDepartment recordDepartment = recordDepartmentMapper.selectByCode(code);
        return recordDepartment;
    }

    @Override
    public boolean deleteById(String id) {
        RecordDepartment recordDepartment = recordDepartmentMapper.selectById(id);
        int r = recordDepartmentMapper.deleteById(id);
        int u = userService.deleteByTelphone(recordDepartment.getTelphone());
        if(r+u==2){
            return true;
        }
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        return false;
    }

    /**
     * 设置User
     * @param recordDepartment
     * @param type
     * @return
     */
    public User setUserByType(RecordDepartment recordDepartment,int type){
        User user = new User();
        switch (type){
            case 1:
                user.setUserName(recordDepartment.getTelphone());
                user.setRealName(recordDepartment.getDepartmentName());
                user.setRoleId("BADW");
                user.setTelphone(recordDepartment.getTelphone());
                user.setDistrictId(recordDepartment.getDepartmentAddress());
                break;
            default:
                break;
        }
        return user;
    }


}
