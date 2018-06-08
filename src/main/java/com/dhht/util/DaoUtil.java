package com.dhht.util;

/**
 *
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * Dao工具类
 *
 * @author zhaoxl
 *
 */
public class DaoUtil {

    /**
     * 获取查询语句前缀
     *
     * @param c
     * @return hql查询语句前缀
     */
    @SuppressWarnings("rawtypes")
    public static String getFindPrefix(Class c) {
        return "from " + c.getSimpleName()+" ";
    }

    /**
     * 获取统计语句前缀
     *
     * @param c
     * @return hql统计语句前缀
     */
    @SuppressWarnings("rawtypes")
    public static String getCountPrefix(Class c) {
        return "select count(*) from " + c.getSimpleName();
    }

    /**
     * 生成in查询字符串
     *
     * @return
     */
    public static String generateInStr(Collection<String> strs) {
        String result = "";
        if(strs != null) {
            for (String str : strs) {
                result += "'" + str + "',";
            }
        }
        if("".equals(result)) {
            return "''";
        }
        return result.substring(0, result.length() - 1);
    }

    /**
     * String转int，异常则转成def
     */
    public static int parseInt(String string, int def) {
//		System.out.println("=========>"+string);
        if (StringUtils.isBlank(string))
            return def;
        int num = def;
        try {
            num = Integer.parseInt(string);
        } catch (Exception e) {
            num = def;
        }
        return num;
    }

    /**
     * Id字符串集转化为 List<String>集合
     * @param JsonStr
     * @return  List<String>
     */
    public static List<String> parseJsonStrToList(String JsonStr){  // ["3c48d69477334b6fb6d70b337f168f2f","496b9c2bf1bd440a8b96ec48f45249f2"]
        List<String> strList = new ArrayList<String>();
        String[] arr = JsonStr.split(",");
        for(int i=0;i<arr.length;i++){
            if(i==0){
                String s1 = arr[i].substring(0);
                strList.add(s1);
                continue;
            }
            if(i==arr.length-1){
                String s2 = arr[i].substring(0, arr[i].length());
                strList.add(s2);
                continue;
            }
            strList.add(arr[i]);
        }

        return strList;
    }
}
