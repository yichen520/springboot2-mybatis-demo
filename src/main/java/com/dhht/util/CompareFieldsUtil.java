package com.dhht.util;


import com.dhht.model.CompareResult;
import com.dhht.model.Employee;
import com.dhht.model.EntityCommentModel;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CompareFieldsUtil {
    /**
     * 比较
     * @param newObject
     * @param oldObject
     * @param ignoreArr
     * @return
     */
    public static List<CompareResult> compareField(Object newObject, Object oldObject, String[] ignoreArr){
        try{
            List<CompareResult> compareResults = new ArrayList<>();
            List<String> ignoreList = null;
            if(ignoreArr != null && ignoreArr.length > 0){
                // array转化为list
                ignoreList = Arrays.asList(ignoreArr);
            }
            if (newObject.getClass() == oldObject.getClass()) {// 只有两个对象都是同一类型的才有可比性
                Class clazz = newObject.getClass();
                // 获取object的属性描述
                PropertyDescriptor[] pds = Introspector.getBeanInfo(clazz,
                        Object.class).getPropertyDescriptors();
                for (PropertyDescriptor pd : pds) {// 这里就是所有的属性了
                    String name = pd.getName();// 属性名
                    if(ignoreList != null && ignoreList.contains(name)){// 如果当前属性选择忽略比较，跳到下一次循环
                        continue;
                    }
                    Method readMethod = pd.getReadMethod();// get方法
                    // 在obj1上调用get方法等同于获得obj1的属性值
                    Object o1 = readMethod.invoke(newObject);
                    // 在obj2上调用get方法等同于获得obj2的属性值
                    Object o2 = readMethod.invoke(oldObject);
                    if(o1 instanceof Timestamp){
                        o1 = new Date(((Timestamp) o1).getTime());
                    }
                    if(o2 instanceof Timestamp){
                        o2 = new Date(((Timestamp) o2).getTime());
                    }
                    if(o1 == null && o2 == null){
                        continue;
                    }else if(o1 == null && o2 != null){
                        CompareResult compareResult = new CompareResult();
                        compareResult.setNewValue(o2);
                        compareResult.setOldValue(o1);
                        EntityCommentModel entityCommentModel = EntityAnnotationUtil.getAnnotationValue(newObject.getClass(),name);
                        compareResult.setPropertyName(entityCommentModel.getValue());
                        compareResult.setPropertyType(entityCommentModel.getType());
                        compareResults.add(compareResult);
                        continue;
                    }
                    if (!o1.equals(o2)) {// 比较这两个值是否相等,不等就可以放入map了
                        CompareResult compareResult = new CompareResult();
                        compareResult.setNewValue(o2);
                        compareResult.setOldValue(o1);
                        EntityCommentModel entityCommentModel = EntityAnnotationUtil.getAnnotationValue(newObject.getClass(),name);
                        compareResult.setPropertyName(entityCommentModel.getValue());
                        compareResult.setPropertyType(entityCommentModel.getType());
                        compareResults.add(compareResult);
                    }
                }
            }
            return compareResults;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }


}
