package com.example.servicehi.TestRedis;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 *
 * @author huojg
 */
@Component
public class RedisUtils {
    @Autowired
    private RedisTemplate redisTemplate;

    @Resource(name="redisTemplate")
    private ValueOperations<String, String> valueOperations;
    @Resource(name="redisTemplate")
    private HashOperations<String, String, Object> hashOperations;
    @Resource(name="redisTemplate")
    private ListOperations<String, Object> listOperations;
    @Resource(name="redisTemplate")
    private SetOperations<String, Object> setOperations;
    @Resource(name="redisTemplate")
    private ZSetOperations<String, Object> zSetOperations;
    /**  默认过期时长，单位：秒 */
    public final static long DEFAULT_EXPIRE = 60 * 60 * 24;
    /**  不设置过期时长 */
    public final static long NOT_EXPIRE = -1;

    public void set(String key, Object value, long expire){
        valueOperations.set(key, toJson(value));
        if(expire != NOT_EXPIRE){
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }

    public void set(String key, Object value){
        set(key, value, DEFAULT_EXPIRE);
    }

    public <T> T get(String key, Class<T> clazz, long expire) {
        String value = valueOperations.get(key);
        if(expire != NOT_EXPIRE){
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
        return value == null ? null : fromJson(value, clazz);
    }

    public <T> T get(String key, Class<T> clazz) {
        return get(key, clazz, NOT_EXPIRE);
    }

    public String get(String key, long expire) {
        String value = valueOperations.get(key);
        if(expire != NOT_EXPIRE){
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
        return value;
    }

    public String get(String key) {
        return get(key, NOT_EXPIRE);
    }


    /**
     * 修改value不更改过期时间
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public void updateKey(String key,Object value) {
        long expire = redisTemplate.getExpire(key, TimeUnit.SECONDS);
        set(key, value, expire);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * by lvws
     * 清空所有缓存  todo
     */
    public void removeAll(){
        Set<String> keys = redisTemplate.keys("*");
        redisTemplate.delete(keys);
    }

    /** by lvws
     * 清空指定缓存，key可以为多个参数
     * @param keys
     */
    public void delete(String... keys){
        if(keys!=null&&keys.length>0){
            if(keys.length==1){
                redisTemplate.delete(keys[0]);
            }else{
                redisTemplate.delete(CollectionUtils.arrayToList(keys));
            }
        }
    }

    /**
     * 根据关键词模糊删除某一类缓存
     * <p>
     *     比如需要删除 key 中带有PC关键词的缓存，用于删除指定类别的缓存
     * </p>
     * @param keyWords
     */
    public void deleteLike(String keyWords){
        Set keys = redisTemplate.keys("**" + keyWords + "**");
        redisTemplate.delete(keys);
    }

    /**
     * 根据关键词模糊匹配删除多个key
     * @param keywords
     */
    public void deleteLikes(String... keywords){
        int length = keywords.length;
        if (length == 1){
            Set keys = redisTemplate.keys("**" + keywords + "**");
            redisTemplate.delete(keys);
        }else {
            for (String keyword : keywords) {
                Set keys = redisTemplate.keys("**" + keyword + "**");
                redisTemplate.delete(keys);
            }
        }

    }


    /** by lws
     * 根据关键词过得指定的key集合
     * @param keyWords
     * @return
     */
    public Set<String> keys(String keyWords){
        return redisTemplate.keys("**"+keyWords+"**");
    }


    /**
     * Object转成JSON数据
     */
    private String toJson(Object object){
        if(object instanceof Integer || object instanceof Long || object instanceof Float ||
                object instanceof Double || object instanceof Boolean || object instanceof String){
            return String.valueOf(object);
        }
        return JSON.toJSONString(object);
    }

    /**
     * JSON数据，转成Object
     */
    private <T> T fromJson(String json, Class<T> clazz){
        return JSON.parseObject(json, clazz);
    }

//    /**
//     * reids 自增长
//     */
//    public   long  incr (String key ,int cacheSeconds ){
//        long num = redisTemplate.opsForValue().increment(key);
//        if(cacheSeconds != NOT_EXPIRE){
//            redisTemplate.expire(key, cacheSeconds, TimeUnit.SECONDS);
//        }
//        return  num;
//    }
    /**
     * reids 自增长 1
     */
    public    long incr (String str ){
        return   redisTemplate.opsForValue().increment(str, 1);

    }
}
