/**
 * RestTemplate.java
 * net.shopin.jx.web.common.util
 * <p>
 * Function： TODO
 * <p>
 * ver     date      		author
 * ──────────────────────────────────
 * 1.0   2016年11月18日  	 wangxiaoming
 * <p>
 * Copyright (c) 2016, TNT All Rights Reserved.
 */

package net.shopin.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * <p>ClassName:RestTemplate</p>
 * <p>Description:	</p>
 * <p>Company: www.shopin.net</p>
 *
 * @author wangxiaoming
 * @version 1.0
 * @date 2016年11月18日下午6:20:24
 */
@Component
public class JxRedisTemplate<E> {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    SerializeUtil<E> serialize;

    public static ListOperations<String, String> list;

    /**
     * 根据key获取value;
     *
     * @param key
     * @return
     */
    public E get(String key) {
        ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
        return serialize.unserialize(opsForValue.get(key));
    }
    /**
     * 将数据从左部压入队列
     * @param key
     * @param value
     * @return
     */
    public Long lpush(String key, String value) {
        list = redisTemplate.opsForList();
        return list.leftPush(key, value);
    }

    /**
     * 队列从右部弹出对象;
     *
     * @param key
     * @return
     */
    public String rpop(String key) {

        return list.rightPop(key);
    }

    /**
     * 获取队列长度(list模拟)
     *
     * @param key
     * @return
     */
    public Long llen(String key) {
        list = redisTemplate.opsForList();
        return list.size(key);
    }

    /**
     * set 键值对;
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(String key, E value) {

        ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
        try {
            opsForValue.set(key, serialize.serialize(value));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * <p>Title:set</p>
     * <p>Description:	</p>
     *
     * @param key
     * @param value
     * @param expire 过期时间，单位：秒
     * @return boolean
     * @author wangxiaoming
     * @Date 2016年11月19日下午3:35:11
     */
    public boolean set(String key, E value, long expire) {
        ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
        try {
            opsForValue.set(key, serialize.serialize(value), expire, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}

