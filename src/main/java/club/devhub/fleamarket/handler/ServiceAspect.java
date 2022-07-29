package club.devhub.fleamarket.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 这是日志切面，展示了Spring的AOP(面向切面)，
 * 我们可以把打印日志的部分抽出来，形成切面
 */
@Component
@Aspect
@Slf4j
public class ServiceAspect {

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 定义切面
     * 【execution(* club.devhub.messageboard.service.impl.*. *(..))】
     * execution表示在方法执行时触发
     * 开头的*表示可以有任意返回类型
     * club.devhub.messageboard.service.impl指定一个包
     * 第一个.*表示这个包下所有的类
     * 第二个.*表示匹配到的类里面所有的方法
     * (..)表示匹配到的方法可以有任意的参数
     * 换句话说,就是club.devhub.messageboard.service.impl下面的所有方法
     */
    @Pointcut("execution(* club.devhub.fleamarket.service.impl.*.*(..))")
    public void pointcut() {
    }

    /**
     * 定义通知,这里采用@Around类型
     * 注解里面指定的是执行哪个方法
     */
    @Around("pointcut()")
    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("[AOP前置处理]\n<执行类>  {}\n<执行方法>  {}\n<方法入参>  {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                objectMapper.writeValueAsString(joinPoint.getArgs()));
        Object result = joinPoint.proceed();
        log.info("[AOP后置处理]\n<方法出参>  {}", objectMapper.writeValueAsString(result));
        return result;
    }


}
