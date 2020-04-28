package com.nowcoder.community.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

//一些工具的封装
public class CommunityUtil {

    //生成随机字符串
    public static String generateUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    //MD5加密（只能加密，不能解密）
    //hello -> abc123def456
    //hello + 3e4a8 -> fgheli34yf9jksbvkjy8fyj5683492(密码后面加一个随机字符串再去加密)
    public static String md5(String key){
        if(StringUtils.isBlank(key)){
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

    public static String getJSONString(int code, String msg, Map<String,Object> map){
        JSONObject json = new JSONObject();
        json.put("code",code);
        json.put("msg",msg);
        if(map != null){
            for (String key : map.keySet()) {
                json.put(key,map.get(key));
            }
        }
        return json.toJSONString();
    }

    public static String getJSONString(int code, String msg){
        return getJSONString(code, msg,null);
    }

    public static String getJSONString(int code){
        return getJSONString(code, null,null);
    }

    public static void main(String[] args) {
        Map<String,Object> map =new HashMap<>();
        map.put("name","zs");
        map.put("age",25);
        System.out.println(getJSONString(0,"ok",map));
    }
}
