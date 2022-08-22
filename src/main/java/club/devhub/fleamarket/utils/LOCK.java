package club.devhub.fleamarket.utils;

public interface LOCK {

    /**
     * 尝试获取锁
     * @param  timeoutSec 锁持有的超时时间，过期自动释放
     * @return true代表获取成功 false代表获取失败
     */
    boolean tryLock(long timeoutSec);

    /**
     * 释放锁
     */
    void unLock();
}
