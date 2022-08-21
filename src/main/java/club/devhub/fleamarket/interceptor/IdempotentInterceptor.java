package club.devhub.fleamarket.interceptor;

import club.devhub.fleamarket.annotation.Idempotent;
import club.devhub.fleamarket.constant.ResultCodeEnum;
import club.devhub.fleamarket.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * 这是一个Interceptor（拦截器），我们可以用拦截器保证请求的幂等性
 * 原理：对于需要保证幂等的请求，让它们都带上requestId，每个请求都带有不同的requestId，请求被拦截器拦截，
 * 拦截器先判断缓存中是否有requestId，
 * 如果还没有，那么这个请求是第一次来，把requestId放入缓存，并准许请求通行，
 * 如果已经有了，那么这个就是重复请求，拦截请求，不让它进入Controller，
 * 提示：先经过Filter（过滤器），再经过Interceptor（拦截器），最后才进入Controller
 * 拦截器仅仅用@Component注入IOC容器还不够，还需要在WebMvcConfigurer配置，详情请见MvcConfig类
 */
@Component
@Slf4j
public class IdempotentInterceptor implements HandlerInterceptor {


    /**
     * 其实这里用ConcurrentHashMap很不合适，因为不断往里面放东西且不删除，运行时间长了就导致OOM，
     * 这里适合用Redis存储并设置过期时间，但是后端开发初级篇还不能讲Redis，等到后端开发中级篇才能讲Redis，
     * 现在先凑活用吧，重点是理解这种思想，今后改用Redis实现缓存
     */
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Idempotent idempotent = (((HandlerMethod) handler).getMethod()).getAnnotation(Idempotent.class);
        if (idempotent != null) {
            log.info("[拦截器前置处理]\n需要保证幂等性，即将校验。");
            String key = request.getHeader("requestId");
            //putIfAbsent方法：如果缓存中没有这个key返回的是null，如果缓存中已经有这个key，返回的是旧值
            //如果putIfAbsent返回的不是null，说明这是重复请求，进行拦截
            if (key == null || redisTemplate.opsForValue().get(key)!=null) {
                log.info("[拦截器前置处理]\n无法保证幂等，携带的requestId为" + key);
                redisTemplate.opsForValue().set(key,"1",3, TimeUnit.SECONDS);
                throw new BusinessException(ResultCodeEnum.REPEAT_OPERATION);
            }else if(key!=null&&redisTemplate.opsForValue().get(key)==null){
                redisTemplate.opsForValue().set(key,"1",10, TimeUnit.SECONDS);
            }
            log.info("[拦截器前置处理]\n幂等校验通过，进行放行。");
        } else {
            log.info("[拦截器前置处理]\n无需保证幂等性，直接放行。");
        }
        return true;

    }


}