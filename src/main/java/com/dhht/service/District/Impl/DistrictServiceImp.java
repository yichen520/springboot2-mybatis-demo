package com.dhht.service.District.Impl;

import com.dhht.common.JsonObjectBO;
import com.dhht.dao.DistrictMapper;
import com.dhht.dao.MakedepartmentMapper;
import com.dhht.model.District;
import com.dhht.model.DistrictMenus;
import com.dhht.model.MakeDepartmentSimple;
import com.dhht.model.Resource;
import com.dhht.service.District.DistrictService;
import com.dhht.util.StringUtil;
import org.omg.CORBA.Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 2018/6/15 create by fyc
 */
@Service(value = "DistrictService")
@Transactional
public class DistrictServiceImp implements DistrictService{
    @Autowired
    private DistrictMapper districtMapper;

    @Autowired
    private MakedepartmentMapper makedepartmentMapper;


    @Override
    public List<DistrictMenus> selectAllDistrict() {
        List<DistrictMenus> list = findDistrictList(districtMapper.selectAllDistrict());
        List<DistrictMenus> districtMenus = findParent(list);
        setAllChildren(districtMenus,list);
        return districtMenus;
    }

    /**
     * 生成区域列表
     * @param id
     * @return
     */
    @Override
    public List<DistrictMenus> selectOneDistrict(String id) {
        String districtIds[] = StringUtil.DistrictUtil(id);
        String districtId = null;
        if(districtIds[1].equals("00")&&districtIds[2].equals("00")){
           districtId = districtIds[0];
        }else if(!districtIds[1].equals("00")&&districtIds[2].equals("00")){
            districtId = districtIds[0]+districtIds[1];
        }else {
            districtId = id;
        }
        List<DistrictMenus> list = findDistrictList(districtMapper.selectById(districtId));
        List<DistrictMenus> districtMenus = findOneParent(list,id);
        setAllChildren(districtMenus,list);
        return districtMenus;
    }

    @Override
    public JsonObjectBO insert(String districtId, String parentId, String districtName) {
        return null;
    }

    @Override
    public JsonObjectBO delete(String districtId) {
        return null;
    }

    /**
     * 生成区域下带制作单位的列表
     * @param id
     * @return
     */
    @Override
    public List<DistrictMenus> selectMakeDepartmentMenus(String id) {
        String districtIds[] = StringUtil.DistrictUtil(id);
        String districtId = null;
        if(districtIds[1].equals("00")&&districtIds[2].equals("00")){
            districtId = districtIds[0];
        }else if(!districtIds[1].equals("00")&&districtIds[2].equals("00")){
            districtId = districtIds[0]+districtIds[1];
        }else {
            districtId = id;
        }
        List<DistrictMenus> list = findDistrictList(districtMapper.selectById(districtId));
        List<DistrictMenus> districtMenus = findOneParent(list,id);
       setMakeDepartmentchildren(districtMenus,list);
        return districtMenus;
    }

    /**
     * 增加区域
     * @param districtId
     * @param parentId
     * @param districtName
     * @return
     */
  /*  @Override
    public JsonObjectBO insert(String districtId, String parentId, String districtName) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        District district = new District();
        District district1 = new District();
        List<String> name = new ArrayList<>();
        if (districtMapper.selectAllById(districtId).size()!=0){
            return jsonObjectBO.error("该区域已经存在,请重新输入");
        }else {
            if (!"".equals(parentId)) {
                String DistrictIds[] = StringUtil.DistrictUtil(parentId);
                if (DistrictIds[1].equals("00") && DistrictIds[2].equals("00")) {
                    district1.setProvinceId(parentId);
                    Map<String, String> map = new HashMap<>();
                    List<District> list = districtMapper.findByDistrictId(district1);
                    for (District l : list) {
                        map.put("provinceName", l.getProvinceName());

                    }
                    district.setProvinceId(parentId);
                    district.setProvinceName(map.get("provinceName"));
                    district.setCityId(districtId);
                    district.setCityName(districtName);
                    int a = districtMapper.insertSelective(district);
                    if (a == 1) {
                        return jsonObjectBO.success("插入区域成功", null);
                    } else {
                        return jsonObjectBO.error("插入失败");
                    }

                } else if (!DistrictIds[1].equals("00") && DistrictIds[2].equals("00")) {
                    district1.setCityId(parentId);
                    Map<String, String> map = new HashMap<>();
                    List<District> list = districtMapper.findByDistrictId(district1);
                    for (District l : list) {
                        map.put("provinceName", l.getProvinceName());
                        map.put("cityName", l.getCityName());
                        map.put("provinceId", l.getProvinceId());
                    }
                    district.setProvinceId(map.get("provinceId"));
                    district.setCityName(map.get("cityName"));
                    district.setProvinceName(map.get("provinceName"));
                    district.setCityId(parentId);
                    district.setDistrictId(districtId);
                    district.setDistrictName(districtName);
                    int a = districtMapper.insertSelective(district);
                    if (a == 1) {
                        return jsonObjectBO.success("插入区域成功", null);
                    } else {
                        return jsonObjectBO.error("插入失败");
                    }
                } else {
                    return null;
                }
            } else {
                district.setProvinceId(districtId);
                district.setProvinceName(districtName);
                districtMapper.insertSelective(district);
                return jsonObjectBO.success("插入成功", null);
            }
        }
    }

    /**
     * 删除
     * 有问题代码!!!!!!!!!!!!!!!!!!!!!!!
     * 删除区域没有问题
     * 删除市和省会出去lock timeout
     * @param districtId
     * @return
     */
 /*   @Override
    public JsonObjectBO delete(String districtId){
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        District district = new District();
        String DistrictIds[] = StringUtil.DistrictUtil(districtId);
        if(DistrictIds[1].equals("00")&&DistrictIds[2].equals("00")){
            district.setProvinceId(districtId);
            int a =districtMapper.delete(district);
            if (a==1) {
                return jsonObjectBO.success("删除成功",null);
            }else{
                return jsonObjectBO.error("删除失败");
            }
        }else if(!DistrictIds[1].equals("00")&&DistrictIds[2].equals("00")){
            district.setCityId(districtId);
            int a =districtMapper.delete(district);
            if (a==1) {
                return jsonObjectBO.success("删除成功",null);
            }else{
                return jsonObjectBO.error("删除失败");
            }
        }else{
            district.setDistrictId(districtId);
            int a =districtMapper.delete(district);
            if (a==1) {
                return jsonObjectBO.success("删除成功",null);
            }else{
                return jsonObjectBO.error("删除失败");
            }
        }
    }*/

    /**
     * 生成地区列表
     * @param districtList
     * @return
     */
    public List<DistrictMenus> findDistrictList(List<District> districtList){
        List<DistrictMenus> districtMenus = new ArrayList<DistrictMenus>();
        for (District district:districtList) {
            String DistrictIds[] = StringUtil.DistrictUtil(district.getProvinceId());
            if(isAdd(district.getProvinceId(),districtMenus)&&DistrictIds[1].equals("00")&&DistrictIds[2].equals("00")){
                districtMenus.add(new DistrictMenus(district.getProvinceId(),district.getProvinceName(),0,"00"));
            }
        }
        for (District district:districtList) {
            String DistrictIds[] = StringUtil.DistrictUtil(district.getCityId());
            if(isAdd(district.getCityId(),districtMenus)&&!DistrictIds[1].equals("00")&&DistrictIds[2].equals("00")){
                districtMenus.add(new DistrictMenus(district.getCityId(),district.getCityName(),1,DistrictIds[0]+"0000"));
            }
        }
        for (District district:districtList) {
            String DistrictIds[] = StringUtil.DistrictUtil(district.getDistrictId());
            if(!DistrictIds[1].equals("00")&&!DistrictIds[2].equals("00")){
                districtMenus.add(new DistrictMenus(district.getDistrictId(),district.getDistrictName(),2,DistrictIds[0]+DistrictIds[1]+"00"));
            }
        }
        return districtMenus;
    }

    /**
     *
     * @param list
     * @return
     */
    public List<DistrictMenus> findParent(List<DistrictMenus> list){
        List<DistrictMenus> districtMenus = new ArrayList<DistrictMenus>();
        for (DistrictMenus d: list) {
            if(d.getParentId().equals("00")){
                districtMenus.add(d);
            }
        }
        return districtMenus;
    }


    /**
     * 查找父节点
     * @param list
     * @param id
     * @return
     */
    public List<DistrictMenus> findOneParent(List<DistrictMenus> list,String id){
        List<DistrictMenus> districtMenus = new ArrayList<DistrictMenus>();
        for(DistrictMenus districtMenu:list){
            if (districtMenu.getDistrictId().toString().equals(id)){
                districtMenus.add(districtMenu);
            }
        }
        return districtMenus;
    }

    //查找地区子节点
    public void setAllChildren(List<DistrictMenus> parent,List<DistrictMenus> districtMenus){
        for (DistrictMenus districtMenu:parent) {
            List<DistrictMenus> list = findInList(districtMenus,districtMenu.getDistrictId());
            if(list.size()>0){
                districtMenu.setChildren(list);
            }
            setAllChildren(list,districtMenus);
        }
    }

    /**
     * 循环添加区域下的刻章店
     * @param parent
     * @param districtMenus
     */
    public void setMakeDepartmentchildren(List<DistrictMenus> parent, List<DistrictMenus> districtMenus){
        for (DistrictMenus districtMenu:parent) {
            List<DistrictMenus> list = findInList(districtMenus,districtMenu.getDistrictId());
            if(list.size()>0){
                districtMenu.setChildren(list);
                for (DistrictMenus d:list) {
                    List<MakeDepartmentSimple> makeDepartmentSimpleList = makedepartmentMapper.selectByDistrict(d.getDistrictId());
                    if(makeDepartmentSimpleList.size()>0){
                        d.setMakeDepartmentSimples(makeDepartmentSimpleList);
                    }
                }
            }
            setMakeDepartmentchildren(list,districtMenus);
        }
    }



    //在给定集合中根据parentID查找数据
    public List<DistrictMenus> findInList(List<DistrictMenus> districtMenus,String id){
        List<DistrictMenus> list = new ArrayList<DistrictMenus>();
        for(DistrictMenus districtMenu:districtMenus){
            if(districtMenu.getParentId().equals(id)){
                list.add(districtMenu);
            }
        }
        return list;
    }


    //在往地区菜单列表添加时判断是否重复
    public boolean isAdd(String id, List<DistrictMenus> list){
        if(list.isEmpty()){
            return true;
        }
        for(DistrictMenus districtMenus:list){
            if(districtMenus.getDistrictId().equals(id)){
                return false;
            }
        }
        return true;
    }




}
