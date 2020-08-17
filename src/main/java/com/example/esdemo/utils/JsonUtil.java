package com.example.esdemo.utils;

import com.alibaba.fastjson.JSON;

/**
 * @author :liyufei
 * @Decirption : todo
 * @date :2020/8/9 17:55
 */
public class JsonUtil {

    //序列化成Json格式
    public static String EntityToJson(Object object){
        return JSON.toJSONString(object);
    }
    //反序列化成Object
    public static <T> T JsonToEntity(String json,Class<T> clazz){
        return JSON.parseObject(json,clazz);
    }
}
