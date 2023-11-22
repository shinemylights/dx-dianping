package com.dxdp.utils;

import cn.hutool.core.lang.UUID;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class SimpleRedisLock implements ILock {

    private String name;
    private StringRedisTemplate stringRedisTemplate;

    public SimpleRedisLock(String name, StringRedisTemplate stringRedisTemplate) {
        this.name = name;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    private static final String KEY_PREFIX = "lock:";

    //region 1.0
    // @Override
    // public boolean tryLock(long timeoutSec) {
    //     // 获取线程标示
    //     long threadId = Thread.currentThread().getId();
    //     // 获取锁
    //     Boolean success = stringRedisTemplate.opsForValue()
    //             .setIfAbsent(KEY_PREFIX + name, threadId + "", timeoutSec, TimeUnit.SECONDS);
    //     return Boolean.TRUE.equals(success);
    // }
    //
    // public void unlock() {
    //     //通过del删除锁
    //     stringRedisTemplate.delete(KEY_PREFIX + name);
    // }
    //endregion

    //region 2.0解决了误删别人锁的问题
    // private static final String ID_PREFIX = UUID.randomUUID().toString(true) + "-";
    // @Override
    // public boolean tryLock(long timeoutSec) {
    //     // 获取线程标示
    //     String threadId = ID_PREFIX + Thread.currentThread().getId();
    //     // 获取锁
    //     Boolean success = stringRedisTemplate.opsForValue()
    //             .setIfAbsent(KEY_PREFIX + name, threadId, timeoutSec, TimeUnit.SECONDS);
    //     return Boolean.TRUE.equals(success);
    // }
    //
    // public void unlock() {
    //     // 获取线程标示
    //     String threadId = ID_PREFIX + Thread.currentThread().getId();
    //     // 获取锁中的标示
    //     String id = stringRedisTemplate.opsForValue().get(KEY_PREFIX + name);
    //     // 判断标示是否一致
    //     if(threadId.equals(id)) {
    //         // 释放锁
    //         stringRedisTemplate.delete(KEY_PREFIX + name);
    //     }
    // }
    //endregion

    //region 使用lua脚本解决多条命令原子性问题
    private static final String ID_PREFIX = UUID.randomUUID().toString(true) + "-";
    private static final DefaultRedisScript<Long> UNLOCK_SCRIPT;
    static {
        UNLOCK_SCRIPT = new DefaultRedisScript<>();
        UNLOCK_SCRIPT.setLocation(new ClassPathResource("unlock.lua"));
        UNLOCK_SCRIPT.setResultType(Long.class);
    }

    @Override
    public boolean tryLock(long timeoutSec) {
        // 获取线程标示
        String threadId = ID_PREFIX + Thread.currentThread().getId();
        // 获取锁
        Boolean success = stringRedisTemplate.opsForValue()
                .setIfAbsent(KEY_PREFIX + name, threadId, timeoutSec, TimeUnit.SECONDS);
        return Boolean.TRUE.equals(success);
    }

    @Override
    public void unlock() {
        // 调用lua脚本
        stringRedisTemplate.execute(
                UNLOCK_SCRIPT,
                Collections.singletonList(KEY_PREFIX + name),
                ID_PREFIX + Thread.currentThread().getId());
    }
    //endregion


    /*@Override
    public void unlock() {
        // 获取线程标示
        String threadId = ID_PREFIX + Thread.currentThread().getId();
        // 获取锁中的标示
        String id = stringRedisTemplate.opsForValue().get(KEY_PREFIX + name);
        // 判断标示是否一致
        if(threadId.equals(id)) {
            // 释放锁
            stringRedisTemplate.delete(KEY_PREFIX + name);
        }
    }*/
}
