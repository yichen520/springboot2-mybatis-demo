package com.dhht.util;

import com.dhht.annotation.EntityComment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * create by fyc 2018/8/16
 * 实在累注解处理工具类
 */
public class EntityAnnotationUtil {

    /**
     *批量获取注解值
     * @param entityClass
     * @return
     */
    public static List<String> getAnnotationValue(Class<?> entityClass){
        List<String> result = new ArrayList<>();
        Field[] fields = entityClass.getDeclaredFields();
        for(Field field:fields){
            boolean fieldHasAnno = field.isAnnotationPresent(EntityComment.class);
            if(fieldHasAnno){
                EntityComment entityComment = field.getAnnotation(EntityComment.class);
                result.add(entityComment.value());
            }
        }
        return result;
    }

    /**
     * 处理单一字段
     * @param entityClass
     * @param fieldName
     * @return
     */
    public static String getAnnotationValue(Class<?> entityClass,String fieldName){
        String result = "";
        Field[] fields = entityClass.getDeclaredFields();
        for(Field field : fields){
            boolean fieldHasAnno = field.isAnnotationPresent(EntityComment.class);
            boolean isField = field.getName().equals(fieldName);
            if(fieldHasAnno&&isField){
                EntityComment entityComment = field.getAnnotation(EntityComment.class);
                result = entityComment.value();
            }
        }
        return result;
    }
}
