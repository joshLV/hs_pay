package com.hszsd.webpay.util;

import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 对象处理工具类
 * Created by gzhengDu on 2016/7/29.
 */
public class ObjectUtils {
    /**
     * 对象转为Map
     * @param o 对象
     * @return map
     */
    public static Map objectToMapValue(Object o)
    {
        Map<String, String> map = new HashMap();
        //如果对象为空则返回true
        if(o == null){
            return map;
        }

        PropertyDescriptor[] propertys = BeanUtils.getPropertyDescriptors(o.getClass());
        Method methodGet;
        Object value = null;
        //循环判断对象属性值是否为空
        for(PropertyDescriptor property: propertys){
            methodGet = property.getReadMethod();
            try {
                value = methodGet.invoke(o);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if(value != null){
                map.put(property.getName(), String.valueOf(value));
            }

        }
        return map;
    }
}
