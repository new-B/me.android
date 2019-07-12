package me.util.flowable.filter;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: luhao
 * @create: 2019-7-2 18:22
 */
public abstract class AbstractParamsFilter {

    public Map<String, Object> toMap() {
        Map<String, Object> uriVariables = new HashMap<>();
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            Object value = this.invokeGetter(field.getName());
            if (value != null) {
                uriVariables.put(field.getName(), value);
            }
        }
        return uriVariables;
    }

    private Object invokeGetter(String propertyName) {
        try {
            PropertyDescriptor desc = new PropertyDescriptor(propertyName, this.getClass());
            Method storedReadMethod = desc.getReadMethod();
            return storedReadMethod.invoke(this);
        } catch (IllegalAccessException | IntrospectionException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
