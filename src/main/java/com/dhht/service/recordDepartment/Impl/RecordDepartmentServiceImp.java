package com.dhht.service.recordDepartment.Impl;

import com.dhht.dao.RecordDepartmentMapper;
import com.dhht.dao.RecordPoliceMapper;
import com.dhht.dao.UserDao;
import com.dhht.model.RecordDepartment;
import com.dhht.model.RecordPolice;
import com.dhht.model.User;
import com.dhht.service.recordDepartment.RecordDepartmentService;
import com.dhht.service.tools.SmsSendService;
import com.dhht.service.user.UserPasswordService;
import com.dhht.service.user.UserService;
import com.dhht.util.MD5Util;
import com.dhht.util.ResultUtil;
import com.dhht.util.StringUtil;
import com.dhht.util.UUIDUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.dhht.service.user.impl.UserServiceImpl.createRandomVcode;

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
        PageHelper.startPage(pageNum,pageSize);
        List<RecordDepartment> recordDepartments = new ArrayList<>();
        String districtIds[] = StringUtil.DistrictUtil(id);
        if(districtIds[1].equals("00")&&districtIds[2].equals("00")){
            recordDepartments = recordDepartmentMapper.selectByDistrictId(districtIds[0]);
        }else if(!districtIds[1].equals("00")&&districtIds[2].equals("00")){
            recordDepartments = recordDepartmentMapper.selectByDistrictId(districtIds[0]+districtIds[1]);
        }else {
            recordDepartments = recordDepartmentMapper.selectByDistrictId(id);
        }

        PageInfo<RecordDepartment> pageInfo = new PageInfo<>(recordDepartments);
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
        PageHelper.startPage(pageNum,pageSize);
        List<RecordDepartment> recordDepartments= recordDepartmentMapper.selectAllRecordDepartment();
        PageInfo<RecordDepartment> pageInfo = new PageInfo<>(recordDepartments);
        return pageInfo;
    }



    /**
     * 添加备案单位
     * @param recordDepartment
     * @return
     */
    @Override
    public int insert(RecordDepartment recordDepartment) {
        if(isInsert(recordDepartment)){
            return ResultUtil.isDistrict;
        }
        if (recordDepartmentMapper.validateCode(recordDepartment.getDepartmentCode())>0){
            return ResultUtil.isHave;
        }
        recordDepartment.setId(UUIDUtil.generate());
        User user = setUserByType(recordDepartment,1);
        recordDepartment.setVersion(1);
        recordDepartment.setFlag(UUIDUtil.generate10());
        recordDepartment.setUpdateTime(new Date(System.currentTimeMillis()));
        int r = recordDepartmentMapper.insert(recordDepartment);
        int u = userService.insert(user);
        if(r+u==3){
            return ResultUtil.isSuccess;
        }else if(u==1){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultUtil.isHave;
        }else {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultUtil.isFail;
        }
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

    /**
     * 根据Id删除备案单位
     * @param id
     * @return
     */
    @Override
    public int deleteById(String id) {
        RecordDepartment recordDepartment = recordDepartmentMapper.selectById(id);
        try {
            User user = setUserByType(recordDepartment,3);
            if(user ==null){
                int re = recordDepartmentMapper.deleteById(id);
                if(re==1) {
                    return ResultUtil.isSuccess;
                }else {
                    return ResultUtil.isFail;
                }
            }
            int r = recordDepartmentMapper.deleteById(id);
            int u = userService.deleteByTelphone(recordDepartment.getTelphone());
            if (r + u == 2) {
                return ResultUtil.isSuccess;
            }else {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ResultUtil.isFail;
            }
        } catch (Exception e){
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        return ResultUtil.isException;
    }
    }

    /**
     * 根据id修改备案单位
     * @param recordDepartment
     * @return
     */
    @Override
    public int updateById(RecordDepartment recordDepartment) {
        try {
            int d = recordDepartmentMapper.deleteById(recordDepartment.getId());
            if(d==0){
                return ResultUtil.isError;
            }
            if(recordDepartmentMapper.validateCode(recordDepartment.getDepartmentCode())>0){
                return ResultUtil.isHave;
            }
            int u = userService.update(setUserByType(recordDepartment, 2));
            recordDepartmentMapper.deleteById(recordDepartment.getId());
            recordDepartment.setVersion(recordDepartment.getVersion()+1);
            recordDepartment.setIsDelete(true);
            recordDepartment.setUpdateTime(new Date(System.currentTimeMillis()));
            recordDepartment.setId(UUIDUtil.generate());
            int r = recordDepartmentMapper.insert(recordDepartment);
            if (r + u == 3) {
                return ResultUtil.isSuccess;
            }else if(u==1){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ResultUtil.isHave;
            }else {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ResultUtil.isFail;
            }
        }catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultUtil.isException;
        }
        //return true;
    }

    /**
     * 判断该区域下是否有备案单位
     * @param recordDepartment
     * @return
     */
    public boolean isInsert(RecordDepartment recordDepartment){
        List<RecordDepartment> list = recordDepartmentMapper.selectByDistrictId(recordDepartment.getDepartmentAddress());
        if(list.size()>0){
            return true;
        }else {
            return false;
        }
    }


        /**
         * 设置User
         * @param recordDepartment
         * @param type
         * @return
         */
        public User setUserByType (RecordDepartment recordDepartment,int type){
            User user = new User();
            switch (type) {
                //添加user
                case 1:
                    user.setId(UUIDUtil.generate());
                    user.setUserName("BADW"+recordDepartment.getTelphone());
                    user.setRealName(recordDepartment.getDepartmentName());
                    user.setRoleId("BADW");
                    user.setTelphone(recordDepartment.getTelphone());
                    user.setDistrictId(recordDepartment.getDepartmentAddress());
                    break;
                 //修改user
                case 2:
                    RecordDepartment oldDate = recordDepartmentMapper.selectById(recordDepartment.getId());
                    user = userService.findByTelphone(oldDate.getTelphone());
                    user.setUserName("BADW"+recordDepartment.getTelphone());
                    user.setRealName(recordDepartment.getDepartmentName());
                    //user.setRoleId("BADW");
                    user.setTelphone(recordDepartment.getTelphone());
                    user.setDistrictId(recordDepartment.getDepartmentAddress());
                    break;
                 //删除user
                case 3:
                    user = userService.findByTelphone(recordDepartment.getTelphone());
                default:
                    break;
            }
            return user;
        }

    @Override
    public List<RecordDepartment> showMore(String flag) {
        return recordDepartmentMapper.selectByFlag(flag);
    }
}
