package com.dhht.controller;

import com.dhht.common.JsonObjectBO;
import com.dhht.model.User;
import com.dhht.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2017/8/16.
 */
@RestController
@RequestMapping(value = "/area")
public class AreaController {

    @Autowired
    private UserService userService;



    @RequestMapping("/getTree")
    public JsonObjectBO getTrees(){
        //获取当前登录用户
        User user = new User();

//        List<District> userRegions = new ArrayList<District>();
//        List<District> regions= userService.getRegionsTrees(users.getRegionId());
//        // 机构储存
//        Map<String, District> map = new HashMap<String, District>();
//        for (District region : regions) {
//            map.put(region.getId(), region);
//        }
//        // 循环归类
//        for (District region : regions) {
//            if (StringUtils.isNotBlank(region.getParentId())) {
//                District parentRegion = map.get(region.getParentId());
//                if (null != parentRegion) {
//                    if (null == parentRegion.getChildren()) {
//                        List<District> children = new ArrayList<District>();
//                        parentRegion.setChildren(children);
//                    }
//                    parentRegion.getChildren().add(region);
//                }
//            }
//            if (user.getLevel() == 0) {
//                if (region.getLevel() == 1)
//                    userRegions.add(region);
//            } else {
//                if (user.getLevel() == region.getLevel())
//                    userRegions.add(region);
//            }
//        }
//        return userRegions;
//
//        users.setId();
//        .getTrees(users);
        return null;
    }

}