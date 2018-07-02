package com.dhht.service.recordDepartment.Impl;

import com.dhht.dao.RecordDepartmentMapper;
import com.dhht.dao.RecordPoliceMapper;
import com.dhht.dao.UserDao;
import com.dhht.model.RecordDepartment;
import com.dhht.model.RecordPolice;
import com.dhht.model.User;
import com.dhht.service.recordDepartment.RecordDepartmentService;
import com.dhht.service.tools.SmsSendService;
import com.dhht.service.user.UserService;
import com.dhht.util.MD5Util;
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
    private SmsSendService smsSendService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private RecordPoliceMapper recordPoliceMapper;

    private String code;

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
    public Boolean insert(RecordDepartment recordDepartment) {
        if(isInsert(recordDepartment)){
            return false;
        }
        recordDepartment.setId(UUIDUtil.generate());
        User user = setUserByType(recordDepartment,1);
        int r = recordDepartmentMapper.insert(recordDepartment);
        int u = userDao.addUser(user);
        if(r+u==2){
            smsSendService.sendMessage(user.getTelphone(),code);
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

    /**
     * 根据Id删除备案单位
     * @param id
     * @return
     */
    @Override
    public boolean deleteById(String id) {
        RecordDepartment recordDepartment = recordDepartmentMapper.selectById(id);
        try {
            User user = setUserByType(recordDepartment,3);
            if(user ==null){
                int re = recordDepartmentMapper.deleteById(id);
                if(re==1) {
                    return true;
                }else {
                    return false;
                }
            }
            int r = recordDepartmentMapper.deleteById(id);
            int u = userDao.deleteByTelphone(recordDepartment.getTelphone());
            if (r + u == 2) {
                return true;
            }else {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return false;
            }
        } catch (Exception e){
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        return false;
    }
    }

    /**
     * 根据id修改备案单位
     * @param recordDepartment
     * @return
     */
    @Override
    public boolean updateById(RecordDepartment recordDepartment) {
        try {
            int u = userDao.update(setUserByType(recordDepartment, 2));
            int r = recordDepartmentMapper.updateById(recordDepartment);
            if (r + u == 2) {
                return true;
            }else {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return false;
            }
        }catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
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
                    code = createRandomVcode();
                    String password = MD5Util.toMd5(code);
                    user.setPassword(password);
                    user.setRoleId("BADW");
                    user.setTelphone(recordDepartment.getTelphone());
                    user.setDistrictId(recordDepartment.getDepartmentAddress());
                    break;
                 //修改user
                case 2:
                    RecordDepartment oldDate = recordDepartmentMapper.selectById(recordDepartment.getId());
                    user = userDao.findByTelphone(oldDate.getTelphone());
                    user.setUserName("BADW"+recordDepartment.getTelphone());
                    user.setRealName(recordDepartment.getDepartmentName());
                    //user.setRoleId("BADW");
                    user.setTelphone(recordDepartment.getTelphone());
                    user.setDistrictId(recordDepartment.getDepartmentAddress());
                    break;
                 //删除user
                case 3:
                    user = userDao.findByTelphone(recordDepartment.getTelphone());
                default:
                    break;
            }
            return user;
        }



    /**
     * 根据备案单户获取民警列表
     * @param recordDepartment
     * @param type
     * @return
     */
     /*public List<RecordPolice> setRecordPolice(RecordDepartment recordDepartment,int type){
        //RecordPolice recordPolice = new RecordPolice();
         List<RecordPolice> list = new ArrayList<>();
         switch (type){
             case 1:
                 RecordDepartment oldDate = recordDepartmentMapper.selectById(recordDepartment.getId());
                 list = recordPoliceMapper.selectByOfficeCode(oldDate.getDepartmentCode());
                 for (RecordPolice r:list) {
                     r.setOfficeDistrict(recordDepartment.getDepartmentAddress());
                     r.setOfficeName(recordDepartment.getDepartmentName());
                     r.setOfficeCode(recordDepartment.getDepartmentCode());
                 }
                 break;
             case 2:
                 list = recordPoliceMapper.selectByOfficeCode(recordDepartment.getDepartmentCode());
             default:
                  break;
         }
         return list;
     }*/


}
