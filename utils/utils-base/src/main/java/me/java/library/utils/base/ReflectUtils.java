package me.java.library.utils.base;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author :  sylar
 * @FileName :
 * @CreateDate :  2017/11/08
 * @Description :
 * @ReviewedBy :
 * @ReviewedOn :
 * @VersionHistory :
 * @ModifiedBy :
 * @ModifiedDate :
 * @Comments :
 * @CopyRight : COPYRIGHT(c) xxx.com All Rights Reserved
 * *******************************************************************************************
 */
public class ReflectUtils {

    static public <T> List<Field> getAllField(Class<T> clazz) {
        List<Field> fieldList = Lists.newArrayList();
        Class tempClass = clazz;
        while (tempClass != null) {
            //当父类为null的时候说明到达了最上层的父类(Object类)
            fieldList.addAll(Arrays.asList(tempClass.getDeclaredFields()));
            tempClass = tempClass.getSuperclass(); //得到父类,然后赋给自己
        }
        return fieldList;
    }

    static public <T> List<Field> getAllFieldByOrder(Class<T> clazz) {
        List<Field> list = Lists.newArrayList();
        Class tempClass = clazz;
        while (tempClass != null) {
            list.addAll(0, Lists.newArrayList(tempClass.getDeclaredFields()));
            //得到父类,然后赋给自己
            tempClass = tempClass.getSuperclass();
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    static public <T> T getReflectObj(String classFullName, Class<T> clazz) {
        T t = null;

        try {
            if (!Strings.isNullOrEmpty(classFullName)) {
                Class<?> c = Class.forName(classFullName);
                Object obj = c.newInstance();
                if (obj != null && clazz.isAssignableFrom(c)) {
                    t = (T) obj;
                }
            }
        } catch (Exception e) {
        }

        return t;
    }

    static public <T> Class<?> getGenericType(List<T> list) {
        Object[] array = list.toArray();
        return array.getClass().getComponentType();
    }

    static public <T> Class<?> getGenericType(Collection<T> collection) {
        Object[] array = Lists.newArrayList(collection).toArray();
        return array.getClass().getComponentType();
    }

    static public Class<?> getGenericType(Object obj) {
        return getGenericType(obj, 0);
    }

    static public Class<?> getGenericType(Object obj, int index) {
        if (obj == null) {
            return null;
        }

        // 获取泛型父类
        Type type = obj.getClass().getGenericSuperclass();

        // 不是泛型类
        if (!(type instanceof ParameterizedType)) {
            return null;
        }

        ParameterizedType genType = (ParameterizedType) type;
        Type[] types = genType.getActualTypeArguments();

        Preconditions.checkState(index >= 0 && index < types.length);
        Type objType = types[index];

        return (Class<?>) objType;
    }
}
