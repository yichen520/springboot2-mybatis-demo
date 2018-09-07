package com.dhht.service.index.impl;

import com.dhht.dao.EmployeeDao;
import com.dhht.dao.MakedepartmentMapper;
import com.dhht.dao.SealDao;
import com.dhht.dao.UseDepartmentDao;
import com.dhht.model.*;
import com.dhht.service.index.RecordDepartmentIndexService;
import com.dhht.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("recordDepartmentIndexService")
public class recordDepartmentIndexServiceImp implements RecordDepartmentIndexService {

    @Autowired
    private MakedepartmentMapper makedepartmentMapper;
    @Autowired
    private UseDepartmentDao useDepartmentDao;
    @Autowired
    private SealDao sealDao;
    @Autowired
    private EmployeeDao employeeDao;


    /**
     * 数据总览
     * @param districtId
     * @return
     */
    @Override
    public  List<IndexOverview> overview(String districtId) {
        List<IndexOverview> result = new ArrayList<>();
        result.add(sealCount(districtId));
        result.add(useDepartmentCount(districtId));
        result.add(makeDepartmentCount(districtId));
        result.add(employeeCount(districtId));
        return result;
    }

    /**
     * 印章制作排行
     * @param districtId
     * @return
     */
    @Override
    public List<IndexCount> ranking(String districtId) {
        districtId = StringUtil.getDistrictId(districtId);
        List<IndexCount> result = new ArrayList<>();
        List<IndexCount> sealCounts = getSealCount();
        List<MakeDepartmentSimple> makeDepartmentSimpleList = getMakepartmentList(districtId);
        for(IndexCount indexCount:sealCounts){
            for (MakeDepartmentSimple makeDepartmentSimple : makeDepartmentSimpleList){
                if(indexCount.getComment().equals(makeDepartmentSimple.getDepartmentCode())){
                    IndexCount count = new IndexCount(makeDepartmentSimple.getDepartmentName(),indexCount.getValue());
                    result.add(count);
                }
            }
        }
        return result;
    }

    /**
     * 折线数据
     * @param districtId
     * @return
     */
    @Override
    public List<IndexCount> polyline(String districtId) {
        districtId = StringUtil.getDistrictId(districtId);
        List<IndexCount> result = new ArrayList<>();
        int month = getMonth();
        for(Integer i=1;i<=month;i++){
            int value = sealDao.indexCountPolyline(i,districtId);
            IndexCount indexCount = new IndexCount(i.toString(),value);
            result.add(indexCount);
        }
        return result;
    }

    /**
     * 统计制作单位
     * @param districtId 区域Id
     * @return
     */
    public IndexOverview makeDepartmentCount(String districtId){
        districtId = StringUtil.getDistrictId(districtId);
        int makeDepartmentSum = makedepartmentMapper.countAllDepartment(districtId);
        int makeDepartmentAdd = makedepartmentMapper.indexCountAdd(districtId);
        int makeDepartmentDel = makedepartmentMapper.indexCountDel(districtId);
        IndexOverview makeDepartmentIndexOverview = new IndexOverview(makeDepartmentSum,makeDepartmentAdd,makeDepartmentDel);
        return makeDepartmentIndexOverview;
    }

    /**
     * 使用单位统计
     * @param districtId
     * @return
     */
    public IndexOverview useDepartmentCount(String districtId){
        districtId = StringUtil.getDistrictId(districtId);
        int useDepartmentSum = useDepartmentDao.countAllDepartment(districtId);
        int usDepartmentAdd = useDepartmentDao.indexCountAdd(districtId);
        int useDepartmentDel = useDepartmentDao.indexCountDel(districtId);
        IndexOverview useDepartmentIndexOverview = new IndexOverview(useDepartmentSum,usDepartmentAdd,useDepartmentDel);
        return useDepartmentIndexOverview;
    }

    /**
     * 从业人员统计
     * @param districtId
     * @return
     */
    public IndexOverview employeeCount(String districtId){
        districtId = StringUtil.getDistrictId(districtId);
        int employeeSum = employeeDao.countAllEmployee(districtId);
        int employeeAdd = employeeDao.indexCountAdd(districtId);
        int employeeDel = employeeDao.indexCountDel(districtId);
        IndexOverview employeeIndexOverview = new IndexOverview(employeeSum,employeeAdd,employeeDel);
        return employeeIndexOverview;
    }

    /**
     * 印章信息统计
     * @param districtId
     * @return
     */
    public IndexOverview sealCount(String districtId){
        districtId = StringUtil.getDistrictId(districtId);
        int sealSum = sealDao.indexCountSum(districtId);
        int sealAdd = sealDao.indexCountAdd(districtId);
        int sealDel = sealDao.indexCountDel(districtId);
        IndexOverview sealIndexOverview = new IndexOverview(sealSum,sealAdd,sealDel);
        return sealIndexOverview;
    }

    /**
     * 获取该备案单位下的制作单位
     * @param districtId
     * @return
     */
    public List<MakeDepartmentSimple> getMakepartmentList(String districtId){
        List<MakeDepartmentSimple> makedepartments = makedepartmentMapper.selectByDistrict(districtId);
        return makedepartments;
    }

    /**
     * 获取印章统计数据
     * @return
     */
    public List<IndexCount> getSealCount(){
        List<IndexCount> indexCounts = sealDao.indexCountSealByDepartment();
        return indexCounts;
    }

    /**
     * 获取当前月
     * @return
     */
    public int getMonth(){
        Date date = new Date();
        return date.getMonth()+1;
    }


}
