package club.devhub.fleamarket.utils;

import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

public class RedisLock implements LOCK{

    private String name;
    private StringRedisTemplate stringRedisTemplate;

    private static final String LOCK_PREFIX="lock:";

    public RedisLock(String name,StringRedisTemplate stringRedisTemplate){
        this.name=name;
        this.stringRedisTemplate=stringRedisTemplate;
    }

    /**
     * 把获取锁和添加过期时间，用一个函数实现，以保证这俩个步骤合起来是原子操作
     * setIfAbsent表示没有锁的时候，才执行set操作，参数含义分别是：锁名，线程id，过期时间，时间单位
     * Boolean到boolean有一个自动拆箱的过程，为避免success为空指针导致异常，所以返回Boolean.TRUE.equals(success)，
     * 此时即使success为空，返回的结果也为FALSE
     */
    @Override
    public boolean tryLock(long timeoutSec) {
        //获取线程标识
        long threadId=Thread.currentThread().getId();
        //获取锁
        Boolean success=stringRedisTemplate.opsForValue()
                .setIfAbsent(LOCK_PREFIX+name,threadId+"",timeoutSec, TimeUnit.SECONDS);
        return Boolean.TRUE.equals(success);
    }

    @Override
    public void unLock() {
        stringRedisTemplate.delete(LOCK_PREFIX+name);
    }
}
