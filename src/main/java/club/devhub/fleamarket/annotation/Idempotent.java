package club.devhub.fleamarket.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * 这是一个自定义注解，
 * 把这个注解加到Controller的方法上面，可以确保进入这个方法的每个请求拥有不一样的requestId,
 * 如果有重复的requestId，则不会被处理，解决了重复提交的问题
 * 在这个项目中，此注解和拦截器配合使用
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Idempotent {
}
