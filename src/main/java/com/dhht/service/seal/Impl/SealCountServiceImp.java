package com.dhht.service.seal.Impl;

import com.dhht.dao.MakedepartmentMapper;
import com.dhht.dao.SealDao;
import com.dhht.model.*;
import com.dhht.service.District.DistrictService;
import com.dhht.service.make.MakeDepartmentCuontService;
import com.dhht.service.seal.SealCuontService;
import com.dhht.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 2018/7/7 create by cy
 */
@Service(value = "sealCountService")
@Transactional
public class SealCountServiceImp implements SealCuontService {
    @Autowired
    private MakedepartmentMapper makedepartmentMapper;

    @Autowired
    private SealDao sealDao;

    @Autowired
    private DistrictService districtService;

    /**
     * 统计区域下所有的所有的制作单位.
     * @param districtId
     * @return
     */


    /**
     * 返回区域列表
     *
     * @param district
     * @return
     */
    public List<DistrictMenus> getDistrictList(String district) {
        List<DistrictMenus> list = districtService.selectOneDistrict(district);
        return list.get(0).getChildren();
    }

    /**
     * 统计每一栏的小计
     *
     * @param list
     * @return
     */
    public SealCount subtotal(List<SealCount> list) {

        int sealAdd = 0;
        int sealLoss = 0;
        int sealLogout = 0;
        if(list.size()!=0) {
            for (SealCount counts : list) {
                sealAdd = sealAdd + counts.getNewSealNum();
                sealLoss = sealLoss + counts.getLossSealNum();
                sealLogout = sealLogout + counts.getLogoutSealNum();
            }
        }
        SealCount count = new SealCount();
        count.setCountName("小计(" + list.get(0).getCountName() + ")");
        count.setSealType("");
        count.setNewSealNum(sealAdd);
        count.setLossSealNum(sealLoss);
        count.setLogoutSealNum(sealLogout);
        return count;
    }

    /**
     * 按照区域的小计
     * @param dis
     * @param list
     * @return
     */
    public SealCount subtotal(String dis,List<SealCount> list) {

        int sealAdd = 0;
        int sealLoss = 0;
        int sealLogout = 0;

        if(list.size()!=0) {
            for (SealCount counts : list) {
                if (!counts.getCountName().contains("小计")) {
                    sealAdd = sealAdd + counts.getNewSealNum();
                    sealLoss = sealLoss + counts.getLossSealNum();
                    sealLogout = sealLogout + counts.getLogoutSealNum();
                }
            }
        }
        SealCount count = new SealCount();
        count.setCountName("小计(" + dis + ")");
        count.setSealType("");
        count.setNewSealNum(sealAdd);
        count.setLossSealNum(sealLoss);
        count.setLogoutSealNum(sealLogout);
        return count;
    }

    /**
     * 统计每一栏的总量
     *
     * @param list
     * @return
     */
    public List<SealCount> getSum(List<SealCount> list) {
        int sealAdd = 0;
        int sealLoss = 0;
        int sealLogout = 0;
        if(list.size()!=0) {
            for (SealCount counts : list) {
                if (counts.getSealType().equals("")) {
                    sealAdd = sealAdd + counts.getNewSealNum();
                    sealLoss = sealLoss + counts.getLossSealNum();
                    sealLogout = sealLogout + counts.getLogoutSealNum();
                }
            }
        }

        SealCount count = new SealCount();
        count.setCountName("总计 ");
        count.setSealType("");
        count.setNewSealNum(sealAdd);
        count.setLossSealNum(sealLoss);
        count.setLogoutSealNum(sealLogout);
        list.add(count);
        return list;
    }


    /**
     * 根据status获取对应的印章数目
     *
     * @param makeDepartmentCode
     * @param sealTypeCode
     * @param startTime
     * @param endTime
     * @return
     */
    public SealCount getStatus(String makeDepartmentCode, String sealTypeCode, String startTime, String endTime) {

        int newSealNum = sealDao.countAddSeal(makeDepartmentCode, sealTypeCode, startTime, endTime);
        int lossSealNum = sealDao.countLossSeal(makeDepartmentCode, sealTypeCode, startTime, endTime);
        int logoutSealNum = sealDao.countLogoutSeal(makeDepartmentCode, sealTypeCode, startTime, endTime);
        SealCount sealCount = new SealCount();
        sealCount.setNewSealNum(newSealNum);
        sealCount.setLossSealNum(lossSealNum);
        sealCount.setLogoutSealNum(logoutSealNum);
        return sealCount;
    }


    /**
     * 区域中获取新增,挂失和注销印章数目
     * @param districtchilrenId
     * @param sealTypeCode
     * @param startTime
     * @param endTime
     * @return
     */
    public SealCount getStatusAndDistrictId(String districtchilrenId, String sealTypeCode, String startTime, String endTime) {

        int newSealNum = sealDao.countAddSealByDistrictId(districtchilrenId, sealTypeCode, startTime, endTime);
        int lossSealNum = sealDao.countLossSealByDistrictId(districtchilrenId, sealTypeCode, startTime, endTime);
        int logoutSealNum = sealDao.countLogoutSealByDistrictId(districtchilrenId, sealTypeCode, startTime, endTime);
        SealCount sealCount = new SealCount();
        sealCount.setNewSealNum(newSealNum);
        sealCount.setLossSealNum(lossSealNum);
        sealCount.setLogoutSealNum(logoutSealNum);
        return sealCount;

    }

    /**
     * 获取制作单位
     *
     * @param districtIds
     * @return
     */
    public List<String> getMakeDepartmentCode(User user,List<String> districtIds) {
        List<String> makeDepartmentCode = new ArrayList<>();
        if(districtIds!=null&&districtIds.size()!=0){
        for (String id : districtIds) {                //遍历传入的districtId
            String districtId1[] = StringUtil.DistrictUtil(id);
            String districtId = null;
            if (districtId1[1].equals("00") && districtId1[2].equals("00")) {   //省
                districtId = districtId1[0];
                List<String> a = sealDao.selectLikeDistrictId(districtId);
                makeDepartmentCode.addAll(a);

            } else if (!districtId1[1].equals("00") && districtId1[2].equals("00")) {
                districtId = districtId1[0] + districtId1[1];
                List<String> a = sealDao.selectLikeDistrictId(districtId);
                makeDepartmentCode.addAll(a);
            } else {
                districtId = id;
                List<String> a = sealDao.selectDistrictId(districtId);
                makeDepartmentCode.addAll(a);
            }
        }
        }else{
            String districtId = user.getDistrictId().substring(0,4);
            List<String> a = sealDao.selectLikeDistrictId(districtId);
            makeDepartmentCode.addAll(a);
        }
        return makeDepartmentCode;
    }

    /**
     * 根据制作单位
     *
     * @param districtIds
     * @param sealTypeCodes
     * @param startTime
     * @param endTime
     * @return
     */

    @Override
    public List<SealCount> countByDepartment(User user,List<String> districtIds, List<String> sealTypeCodes, String startTime, String endTime) {
        int newSealNum = 0;
        int lossSealNum = 0;
        int logoutSealNum = 0;
        List<SealCount> counts = new ArrayList<>();
        List<String> makeDepartmentCodes = getMakeDepartmentCode(user,districtIds);
        if(makeDepartmentCodes.size()!=0&&makeDepartmentCodes!=null) {
            for (String makeDepartmentCode : makeDepartmentCodes) { //根据传入的code进行遍历
                List<SealCount> count = new ArrayList<>();
                String countName = makedepartmentMapper.selectByDepartmentCode(makeDepartmentCode).getDepartmentName(); //更加code查找name
//            sealCount.setCountName(countName);  //存入count对象
                List<Seal> seals = sealDao.selectByMakeDepartmentCode(makeDepartmentCode); //根据code查找seal中的所有印章
                Set<String> set = new HashSet<>();
                for (Seal seal : seals) {
                    set.add(seal.getSealTypeCode());
                }
                Iterator<String> iterator = set.iterator();
                while (iterator.hasNext()) {
                    String sealTypeCode = iterator.next();  //每个seal中的所有的sealtypecode的集合
                    if (sealTypeCodes != null && sealTypeCodes.size() != 0) {
                        for (String sealTypeCode1 : sealTypeCodes) {
                            if (sealTypeCode.equals(sealTypeCode1)) {
                                String sealType = "";
                                switch (sealTypeCode) {
                                    case "01":
                                        sealType = "法定名称章";
                                        break;
                                    case "02":
                                        sealType = "财务专用章";
                                        break;
                                    case "03":
                                        sealType = "发票专用章";
                                        break;
                                    case "04":
                                        sealType = "合同专用章";
                                        break;
                                    case "05":
                                        sealType = "法人代表专用章";
                                        break;
                                    case "06":
                                        sealType = "公章";
                                        break;
                                    case "07":
                                        sealType = "内设机构章";
                                        break;
                                    case "08":
                                        sealType = "分支机构章";
                                        break;
                                    case "99":
                                        sealType = "其他类型章";
                                        break;
                                }

                                SealCount Num = getStatus(makeDepartmentCode, sealTypeCode, startTime, endTime);
                                newSealNum = Num.getNewSealNum();
                                lossSealNum = Num.getLossSealNum();
                                logoutSealNum = Num.getLogoutSealNum();
                                if (newSealNum != 0 || lossSealNum != 0 || logoutSealNum != 0) {
                                    SealCount sealCount = new SealCount(countName, sealType, newSealNum, lossSealNum, logoutSealNum);
                                    count.add(sealCount);
                                }
                            }
                        }
                    } else { //当前端输入当type不存在当时候
                        String sealType = "";
                        switch (sealTypeCode) {

                            case "01":
                                sealType = "法定名称章";
                                break;
                            case "02":
                                sealType = "财务专用章";
                                break;
                            case "03":
                                sealType = "发票专用章";
                                break;
                            case "04":
                                sealType = "合同专用章";
                                break;
                            case "05":
                                sealType = "法人代表专用章";
                                break;
                            case "06":
                                sealType = "公章";
                                break;
                            case "07":
                                sealType = "内设机构章";
                                break;
                            case "08":
                                sealType = "分支机构章";
                                break;
                            case "99":
                                sealType = "其他类型章";
                                break;
                        }
                        SealCount Num = getStatus(makeDepartmentCode, sealTypeCode, startTime, endTime);
                        newSealNum = Num.getNewSealNum();
                        lossSealNum = Num.getLossSealNum();
                        logoutSealNum = Num.getLogoutSealNum();
                        if (newSealNum != 0 || lossSealNum != 0 || logoutSealNum != 0) {
                            SealCount sealCount = new SealCount(countName, sealType, newSealNum, lossSealNum, logoutSealNum);
                            count.add(sealCount);
                        }
                    }
                }
                count.add(subtotal(count));//把小计放入队列
                counts.addAll(count);

            }
        }
        return getSum(counts);
    }


    /**
     * 根据区域查询
     * @param user
     * @param districts
     * @param sealTypeCodes
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public List<SealCount> countByDistrictId(User user, List<String> districts, List<String> sealTypeCodes, String startTime, String endTime) {

        int newSealNum = 0;
        int lossSealNum = 0;
        int logoutSealNum = 0;
        if (districts == null) { //如果传入的为空  则传入的是当前区域的
            String str = user.getDistrictId().substring(0, 2);
            districts = new ArrayList<String>();
           districts.add(str + "0000");
//            districts.add(str);
        }
        List<DistrictMenus> districtIds = districtService.selectDistrictByArray(districts);
        List<SealCount> sealCounts = new ArrayList<>();
        for (DistrictMenus districtId : districtIds) {
            List<SealCount> sealCount = new ArrayList<>();
            if (districtId.getChildren() != null) {   //表示这里是市
                for (DistrictMenus districtchilrenId : districtId.getChildren()) {   //遍历当前市下的区

                    List<Seal> seals = sealDao.selectByDistrictId(districtchilrenId.getDistrictId());
                    Set<String> set = new HashSet<>();
                    for (Seal seal : seals) {
                        set.add(seal.getSealTypeCode());
                    }

                    if (sealTypeCodes!=null&&sealTypeCodes.size()!=0 ) {
                        for (String sealTypeCode : sealTypeCodes) {
                            String sealType = "";
                            switch (sealTypeCode) {
                                case "01":
                                    sealType = "法定名称章";
                                    break;
                                case "02":
                                    sealType = "财务专用章";
                                    break;
                                case "03":
                                    sealType = "发票专用章";
                                    break;
                                case "04":
                                    sealType = "合同专用章";
                                    break;
                                case "05":
                                    sealType = "法人代表专用章";
                                    break;
                                case "06":
                                    sealType = "公章";
                                    break;
                                case "07":
                                    sealType = "内设机构章";
                                    break;
                                case "08":
                                    sealType = "分支机构章";
                                    break;
                                case "99":
                                    sealType = "其他类型章";
                                    break;
                            }

                            SealCount Num = getStatusAndDistrictId(districtchilrenId.getDistrictId(), sealTypeCode, startTime, endTime);
                            newSealNum = Num.getNewSealNum();
                            lossSealNum = Num.getLossSealNum();
                            logoutSealNum = Num.getLogoutSealNum();
                            if (newSealNum != 0 || lossSealNum != 0 || logoutSealNum != 0) {
                                sealCount.add(new SealCount(districtchilrenId.getDistrictName(), sealType, newSealNum, lossSealNum, logoutSealNum));
                            }
                        }
                    } else {  //如果传入的sealtypecode为null
                        Iterator<String> iterator = set.iterator();
                        while (iterator.hasNext()) {
                            String sealTypeCode = iterator.next();
                            String sealType = "";
                            switch (sealTypeCode) {
                                case "01":
                                    sealType = "法定名称章";
                                    break;
                                case "02":
                                    sealType = "财务专用章";
                                    break;
                                case "03":
                                    sealType = "发票专用章";
                                    break;
                                case "04":
                                    sealType = "合同专用章";
                                    break;
                                case "05":
                                    sealType = "法人代表专用章";
                                    break;
                                case "06":
                                    sealType = "公章";
                                    break;
                                case "07":
                                    sealType = "内设机构章";
                                    break;
                                case "08":
                                    sealType = "分支机构章";
                                    break;
                                case "99":
                                    sealType = "其他类型章";
                                    break;
                            }
                            SealCount Num = getStatusAndDistrictId(districtchilrenId.getDistrictId(), sealTypeCode, startTime, endTime);
                            newSealNum = Num.getNewSealNum();
                            lossSealNum = Num.getLossSealNum();
                            logoutSealNum = Num.getLogoutSealNum();
                            if (newSealNum != 0 || lossSealNum != 0 || logoutSealNum != 0) {
                                sealCount.add(new SealCount(districtchilrenId.getDistrictName(), sealType, newSealNum, lossSealNum, logoutSealNum));
                            }

                        }
                    }

                }


            }
            String dis = districtId.getDistrictName();
            sealCount.add(subtotal(dis,sealCount));//把小计放入队列
            sealCounts.addAll(sealCount);

        }


        return getSum(sealCounts);

    }
}



