package com.dhht.service.seal.Impl;

import com.dhht.dao.MakedepartmentMapper;
import com.dhht.dao.SealDao;
import com.dhht.model.Count;
import com.dhht.model.DistrictMenus;
import com.dhht.model.Seal;
import com.dhht.model.SealCount;
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

        for (SealCount counts : list) {
            sealAdd = sealAdd + counts.getNewSealNum();
            sealLoss = sealLoss + counts.getLossSealNum();
            sealLogout = sealLogout + counts.getLogoutSealNum();
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
     * 统计每一栏的总量
     *
     * @param list
     * @return
     */
    public List<SealCount> getSum(List<SealCount> list) {
        int sealAdd = 0;
        int sealLoss = 0;
        int sealLogout = 0;

        for (SealCount counts : list) {
            if (counts.getSealType().equals("")) {
                sealAdd = sealAdd + counts.getNewSealNum();
                sealLoss = sealLoss + counts.getLossSealNum();
                sealLogout = sealLogout + counts.getLogoutSealNum();
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
     * 根据status获取对应的num
     * @param Status
     * @param makeDepartmentCode
     * @param districtId
     * @param sealTypeCode
     * @param startTime
     * @param endTime
     * @return
     */
    public SealCount getStatus(List<String> Status,String makeDepartmentCode,String districtId,String sealTypeCode,String startTime, String endTime){
        int newSealNum = 0;
        int lossSealNum = 0;
        int logoutSealNum = 0;

        if (Status.contains("01") && Status.contains("05") && Status.contains("06")) { //只查询 已经制作 已经注销和已经挂失的
            newSealNum = sealDao.countAddSeal(makeDepartmentCode, districtId, sealTypeCode, startTime, endTime);
            lossSealNum = sealDao.countLossSeal(makeDepartmentCode, districtId, sealTypeCode, startTime, endTime);
            logoutSealNum = sealDao.countLogoutSeal(makeDepartmentCode, districtId, sealTypeCode, startTime, endTime);
        } else if (Status.contains("05") && Status.contains("06")) {
            lossSealNum = sealDao.countLossSeal(makeDepartmentCode, districtId, sealTypeCode, startTime, endTime);
            logoutSealNum = sealDao.countLogoutSeal(makeDepartmentCode, districtId, sealTypeCode, startTime, endTime);
        } else if (Status.contains("01") && Status.contains("06")) {
            newSealNum = sealDao.countAddSeal(makeDepartmentCode, districtId, sealTypeCode, startTime, endTime);
            logoutSealNum = sealDao.countLogoutSeal(makeDepartmentCode, districtId, sealTypeCode, startTime, endTime);
        } else if (Status.contains("01") && Status.contains("05")) {
            newSealNum = sealDao.countAddSeal(makeDepartmentCode, districtId, sealTypeCode, startTime, endTime);
            lossSealNum = sealDao.countLossSeal(makeDepartmentCode, districtId, sealTypeCode, startTime, endTime);
        } else if (Status.contains("01")) {
            newSealNum = sealDao.countAddSeal(makeDepartmentCode, districtId, sealTypeCode, startTime, endTime);
        } else if (Status.contains("05")) {
            lossSealNum = sealDao.countLossSeal(makeDepartmentCode, districtId, sealTypeCode, startTime, endTime);
        } else if (Status.contains("06")) {
            logoutSealNum = sealDao.countLogoutSeal(makeDepartmentCode, districtId, sealTypeCode, startTime, endTime);
        } else {
            newSealNum = sealDao.countAddSeal(makeDepartmentCode, districtId, sealTypeCode, startTime, endTime);
            lossSealNum = sealDao.countLossSeal(makeDepartmentCode, districtId, sealTypeCode, startTime, endTime);
            logoutSealNum = sealDao.countLogoutSeal(makeDepartmentCode, districtId, sealTypeCode, startTime, endTime);
        }
        SealCount sealCount = new SealCount();
        sealCount.setNewSealNum(newSealNum);
        sealCount.setLossSealNum(lossSealNum);
        sealCount.setLogoutSealNum(logoutSealNum);
        return sealCount;
    }


    /***
     * 根据status和districtId获取对应的num
     * @param Status
     * @param
     * @param sealTypeCode
     * @param startTime
     * @param endTime
     * @return
     */
    public List<SealCount> getStatusAndDistrictId(List<String> Status,List<DistrictMenus> districtIds,String sealTypeCode,String startTime, String endTime){

//
//        if (Status.contains("01") && Status.contains("05") && Status.contains("06")) { //只查询 已经制作 已经注销和已经挂失的
//            List<SealCount> newSealNum = sealDao.countByDistrictId(districtIds, sealTypeCode, startTime, endTime);
//            List<SealCount> lossSealNum = sealDao.countByDistrictId(districtIds, sealTypeCode, startTime, endTime);
//            List<SealCount> logoutSealNum = sealDao.countByDistrictId(districtIds, sealTypeCode, startTime, endTime);
//        } else if (Status.contains("05") && Status.contains("06")) {
//            List<SealCount> lossSealNum = sealDao.countByDistrictId(districtIds, sealTypeCode, startTime, endTime);
//            List<SealCount> logoutSealNum = sealDao.countByDistrictId(districtIds, sealTypeCode, startTime, endTime);
//        } else if (Status.contains("01") && Status.contains("06")) {
//            List<SealCount> newSealNum = sealDao.countByDistrictId(districtIds, sealTypeCode, startTime, endTime);
//            List<SealCount> logoutSealNum = sealDao.countByDistrictId(districtIds, sealTypeCode, startTime, endTime);
//        } else if (Status.contains("01") && Status.contains("05")) {
//            List<SealCount> newSealNum = sealDao.countByDistrictId(districtIds, sealTypeCode, startTime, endTime);
//            List<SealCount> lossSealNum = sealDao.countByDistrictId(districtIds, sealTypeCode, startTime, endTime);
//        } else if (Status.contains("01")) {
//            List<SealCount> newSealNum = sealDao.countByDistrictId(districtIds, sealTypeCode, startTime, endTime);
//        } else if (Status.contains("05")) {
//            List<SealCount> lossSealNum = sealDao.countByDistrictId(districtIds, sealTypeCode, startTime, endTime);
//        } else if (Status.contains("06")) {
//            List<SealCount> logoutSealNum = sealDao.countByDistrictId(districtIds, sealTypeCode, startTime, endTime);
//        } else {
//            List<SealCount>  newSealNum = sealDao.countByDistrictId(districtIds, sealTypeCode, startTime, endTime);
//            List<SealCount> lossSealNum = sealDao.countByDistrictId(districtIds, sealTypeCode, startTime, endTime);
//            List<SealCount> logoutSealNum = sealDao.countByDistrictId(districtIds, sealTypeCode, startTime, endTime);
//        }
//
//        return sealCount;
        return null;
    }

    /**
     * 根据制作单位
     *
     * @param makeDepartmentCodes
     * @param districtId
     * @param sealTypeCodes
     * @param Status
     * @param startTime
     * @param endTime
     * @return
     */

    @Override
    public List<SealCount> countByDepartment(List<String> makeDepartmentCodes, String districtId, List<String> sealTypeCodes, List<String> Status, String startTime, String endTime) {
        int newSealNum = 0;
        int lossSealNum = 0;
        int logoutSealNum = 0;
        List<SealCount> counts = new ArrayList<>();

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
                if (sealTypeCodes.size() != 0) {
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

                             SealCount Num=getStatus(Status,makeDepartmentCode, districtId,sealTypeCode , startTime, endTime);
                             newSealNum=Num.getNewSealNum();
                             lossSealNum=Num.getLossSealNum();
                             logoutSealNum=Num.getLogoutSealNum();
                            if (newSealNum != 0 || lossSealNum != 0 || logoutSealNum != 0) {
                                SealCount sealCount = new SealCount(countName,sealType,newSealNum,lossSealNum,logoutSealNum);
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
                    SealCount Num=getStatus(Status,makeDepartmentCode, districtId,sealTypeCode , startTime, endTime);
                    newSealNum=Num.getNewSealNum();
                    lossSealNum=Num.getLossSealNum();
                    logoutSealNum=Num.getLogoutSealNum();
                    if (newSealNum != 0 || lossSealNum != 0 || logoutSealNum != 0) {
                        SealCount sealCount = new SealCount(countName,sealType,newSealNum,lossSealNum,logoutSealNum);
                        count.add(sealCount);
                    }
                }
            }
            count.add(subtotal(count));//把小计放入队列
            counts.addAll(count);

        }
        return getSum(counts);
    }

//    /**
//     * 根据区域查找印章
//     * @param districtIds
//     * @param sealTypeCodes
//     * @param Status
//     * @param startTime
//     * @param endTime
//     * @return
//     */
//    @Override
//    public List<SealCount> countByDistrictId(List<String> districtIds, List<String> sealTypeCodes, List<String> Status, String startTime, String endTime) {
//        int newSealNum = 0;
//        int lossSealNum = 0;
//        int logoutSealNum = 0;
//        List<SealCount> counts = new ArrayList<>();
//        for (String districtId : districtIds) { //根据传入的code进行遍历  判断省市区
//            String districtIds1[] = StringUtil.DistrictUtil(districtId);
//            String districtId1= null;
//            if(districtIds1[1].equals("00")&&districtIds1[2].equals("00")){  //输入为省
//                districtId1 = districtIds1[0];
//                List<DistrictMenus> list = districtService.selectOneDistrict(districtId1);
//                List<DistrictMenus> districtIdList = list.get(0).getChildren();
//                for (String sealTypeCode1 : sealTypeCodes) {
//                    String sealType = "";
//                    switch (sealTypeCode1) {
//                        case "01":
//                            sealType = "法定名称章";
//                            break;
//                        case "02":
//                            sealType = "财务专用章";
//                            break;
//                        case "03":
//                            sealType = "发票专用章";
//                            break;
//                        case "04":
//                            sealType = "合同专用章";
//                            break;
//                        case "05":
//                            sealType = "法人代表专用章";
//                            break;
//                        case "06":
//                            sealType = "公章";
//                            break;
//                        case "07":
//                            sealType = "内设机构章";
//                            break;
//                        case "08":
//                            sealType = "分支机构章";
//                            break;
//                        case "99":
//                            sealType = "其他类型章";
//                            break;
//                    }
////                    List<SealCount> Num=getStatusAndDistrictId(Status, districtIdList,sealTypeCode1 , startTime, endTime);
//                    if (Status.contains("01") && Status.contains("05") && Status.contains("06")) { //只查询 已经制作 已经注销和已经挂失的
//                        List<SealCount> newSealNumList = sealDao.countByDistrictId(districtIdList, sealTypeCode1, startTime, endTime);
//                        List<SealCount> lossSealNumList = sealDao.countByDistrictId(districtIdList, sealTypeCode1, startTime, endTime);
//                        List<SealCount> logoutSealNumList = sealDao.countByDistrictId(districtIdList, sealTypeCode1, startTime, endTime);
//                    } else if (Status.contains("05") && Status.contains("06")) {
//                        List<SealCount> lossSealNumList = sealDao.countByDistrictId(districtIdList, sealTypeCode1, startTime, endTime);
//                        List<SealCount> logoutSealNumList = sealDao.countByDistrictId(districtIdList, sealTypeCode1, startTime, endTime);
//                    } else if (Status.contains("01") && Status.contains("06")) {
//                        List<SealCount> newSealNumList = sealDao.countByDistrictId(districtIdList, sealTypeCode1, startTime, endTime);
//                        List<SealCount> logoutSealNumList = sealDao.countByDistrictId(districtIdList, sealTypeCode1, startTime, endTime);
//                    } else if (Status.contains("01") && Status.contains("05")) {
//                        List<SealCount> newSealNumList = sealDao.countByDistrictId(districtIdList, sealTypeCode1, startTime, endTime);
//                        List<SealCount> lossSealNumList = sealDao.countByDistrictId(districtIdList, sealTypeCode1, startTime, endTime);
//                    } else if (Status.contains("01")) {
//                        List<SealCount> newSealNumList = sealDao.countByDistrictId(districtIdList, sealTypeCode1, startTime, endTime);
//                    } else if (Status.contains("05")) {
//                        List<SealCount> lossSealNumList = sealDao.countByDistrictId(districtIdList, sealTypeCode1, startTime, endTime);
//                    } else if (Status.contains("06")) {
//                        List<SealCount> logoutSealNumList = sealDao.countByDistrictId(districtIdList, sealTypeCode1, startTime, endTime);
//                    } else {
//                        List<SealCount>  newSealNumList = sealDao.countByDistrictId(districtIdList, sealTypeCode1, startTime, endTime);
//                        List<SealCount> lossSealNumList = sealDao.countByDistrictId(districtIdList, sealTypeCode1, startTime, endTime);
//                        List<SealCount> logoutSealNumList = sealDao.countByDistrictId(districtIdList, sealTypeCode1, startTime, endTime);
//                    }
//                    newSealNum=newSealNumList
//                    lossSealNum=Num.getLossSealNum();
//                    logoutSealNum=Num.getLogoutSealNum();
//                    if (newSealNum != 0 || lossSealNum != 0 || logoutSealNum != 0) {
//                        SealCount sealCount = new SealCount(countName,sealType,newSealNum,lossSealNum,logoutSealNum);
//                        count.add(sealCount);
//                    }
//
//                }
//
//
//            }else if(!districtIds1[1].equals("00")&&districtIds1[2].equals("00")){  //输入为市
//                districtId1 = districtIds1[0]+districtIds1[1];
//            }else {
//                districtId1 = districtId;
//            }
//        }
//return null;
//    }




}

