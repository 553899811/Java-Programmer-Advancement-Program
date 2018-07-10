package net.shopin.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * <p>ClassName:ShopinRedisTemplate</p>
 * <p>Description:	ShopinRedisTemplate 使用模板(整合SpringBoot) </p>
 * <p>Company: www.shopin.net</p>
 * <p>Desc:  </p>
 *
 * @author zhangyong@shopin.cn
 * @version 1.0
 * @date 2018/5/15 10:47
 */

@Component
public class ShopinRedisTemplate<E> {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SerializeUtil<E> serialize;

    public static ListOperations<String, String> list;

    /**
     * 根据键值获取value
     *
     * @param key
     * @return
     */
    public E get(String key) {
        ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
        return key == null ? null : serialize.unserialize(opsForValue.get(key));
    }

    /**
     * 设置键值对(无超时时间)
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
     * 设置键值对 (设置有效时间,以秒为单位)
     *
     * @param key
     * @param value
     * @param expire
     * @return boolean
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

    /**
     * 根据key删除键值对
     *
     * @param key
     * @return
     */
    public boolean delete(String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /*
     * 模拟队列
     */

    /**
     * lpush 进队列
     *
     * @param key
     * @param value
     * @return
     */
    public Long lpush(String key, String value) {
        list = redisTemplate.opsForList();
        try {
            Long leftPush = list.leftPush(key, value);
            return leftPush;
        } catch (Exception e) {
            e.printStackTrace();
            return Long.valueOf(-1);
        }
    }

    /**
     * rpop 出队列
     *
     * @param key
     * @return
     */
    public String rpop(String key) {
        list = redisTemplate.opsForList();
        try {
            return list.rightPop(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 返回指定队列(list)名队列长度
     *
     * @param key
     * @return
     */
    public Long llen(String key) {
        list = redisTemplate.opsForList();
        return list.size(key);
    }
}
