package com.sunsulei.spider.jd_spider.common;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/3/1 14:57.
 */
@Component
public class RedisUtil {

    private static final String _And = "_";

    private static StringRedisTemplate redisTemplate;

    private static ValueOperations<String, String> operations;
    private static HashOperations<String, Byte[], Byte[]> hashOperations;


    @Autowired
    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        RedisUtil.redisTemplate = redisTemplate;
    }

    @Autowired
    public void setOperations(ValueOperations<String, String> operations) {
        RedisUtil.operations = operations;
    }


//    public static Object get() {
//        return null;
//    }


    /**
     * 根据类型获取类名称.
     *
     * @param e:类型
     * @return 返回类的名称
     */
    private static <E> String getRedisKey(Class<E> e) {
        return StrUtil.subAfter(e.getName(), ".", true).toLowerCase();
    }

    /**
     * 生成最终的key
     *
     * @param key:key
     * @param e:类型
     * @return 返回最终的key
     */
    private static <E> String generateKey(String key, Class<E> e) {
        String redisKey = getRedisKey(e);
        return redisKey + _And + key;
    }


    /**
     * 添加
     *
     * @param key    key
     * @param e      对象
     * @param expire 过期时间(单位:秒),传入 -1 时表示不设置过期时间
     */
    public static void set(String key, Object e, Long expire) {
        HashOperations<String, Object, Object> ops = redisTemplate.opsForHash();
        key = generateKey(key, e.getClass());
        String jsonValue = JSONUtil.toJsonStr(e);
        operations.set(key, jsonValue);
        if (expire != -1) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }

    /**
     * 查询
     *
     * @param key 查询的key
     * @param e   查询数据的类型
     */
    public static <E> E get(String key, Class<E> e) {
        String jsonObj = operations.get(generateKey(key, e));
        if (JSONUtil.isJson(jsonObj)) {
            return JSONUtil.toBean(jsonObj, e);
        }
        return (E) jsonObj;
    }

    /**
     * 查询集合
     *
     * @param key:查询的key
     * @param e:返回的类型
     * @return 返回指定类型的集合
     */
    public static <E> List<E> getList(String key, Class<E> e) {
        String jsonObj = operations.get(generateKey(key, e));
        if (JSONUtil.isJsonArray(jsonObj)) {
            return JSONUtil.parseArray(jsonObj).toList(e);
        }
        if (JSONUtil.isJson(jsonObj)) {
            return new ArrayList<E>() {{
                add(JSONUtil.toBean(jsonObj, e));
            }};
        }
        return null;
//        throw new ParserException("当前json数据不是集合,请检查数据合法性 , jsonData : " + jsonObj);
    }


    /**
     * 给key设置过期时间
     *
     * @param key:key
     * @param expire:过期时间(单位:秒)
     */
    public static void expire(String key, Class<?> t, Long expire) {
        redisTemplate.expire(generateKey(key, t), expire, TimeUnit.SECONDS);
    }

    /**
     * 删除
     *
     * @param key 传入key的名称
     */
    public static void del(String key, Class<?> t) {
        if (StrUtil.isAllBlank(key)) {
            throw new RuntimeException("请指定要删除的具体的key");
        }
        redisTemplate.delete(generateKey(key, t));
    }

    /**
     * 查询查询当前redis库下所有key
     */
    public static Set<String> keys(Class<?> t) {
        return redisTemplate.keys(generateKey("", t) + "*");
    }

    /**
     * 判断key是否存在redis中
     *
     * @param key 传入key的名称
     */
    public static boolean exists(String key, Class<?> t) {
        return redisTemplate.hasKey(generateKey(key, t));
    }

    /**
     * 查询当前key下缓存数量
     *
     * @return
     */
    public static long count(Class<?> t) {
        return keys(t).size();
    }

    /**
     * 清空redis
     */
    public void empty(Class<?> t) {
        Set<String> set = keys(t);
        set.forEach(key -> redisTemplate.delete(generateKey(key, t)));
    }
}