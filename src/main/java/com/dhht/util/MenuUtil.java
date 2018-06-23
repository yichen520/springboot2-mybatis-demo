package com.dhht.util;

import com.dhht.model.Menus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuUtil {
    /**
     *
     * @param menus 所有是菜单的资源
     * @return 处理数据返回前端需要的部分
     */
    public static List<Map> genMenu(List<Menus> menus) {
        if (menus == null) return null;
        List<Map> r = new ArrayList<>();
        for (Menus menu : menus) {
            Map m = new HashMap();
            m.put("name", menu.getName());
            m.put("icon", menu.getIcon());
            String[] paths = menu.getPath().split("/");
            m.put("path", paths[paths.length - 1]);
            m.put("children", genMenu(menu.getChildren()));
            r.add(m);
        }
        return r;
    }

    public static List<Map> getSimpeResource(List<Menus> menus) {
        if (menus == null) return null;
        List<Map> r = new ArrayList<>();
        for (Menus menu : menus) {
            Map m = new HashMap();
            m.put("id",menu.getId());
            m.put("name", menu.getName());
            m.put("children", getSimpeResource(menu.getChildren()));
            r.add(m);
        }
        return r;
    }


}
