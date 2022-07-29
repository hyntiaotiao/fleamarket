package club.devhub.fleamarket.config;

import club.devhub.fleamarket.interceptor.IdempotentInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMvcConfigurer提供了多种接口让我们配置SpringMVC,包括但不限于addFormatters，addInterceptors，addViewControllers，addArgumentResolvers
 * 我们只需要实现它，然后重写里面的方法，就能按照自己的想法配置SpringMVC了
 * 以后如果看到implements WebMvcConfigurer，那一定要想到这是一个用来配置SpringMVC的类
 */

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Autowired
    private IdempotentInterceptor idempotentInterceptor;


/**
     * 添加拦截器
     */

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //addInterceptor用来注册拦截器
        //addPathPatterns用来设置所有路径都被拦截
        registry.addInterceptor(idempotentInterceptor).addPathPatterns("/**");

    }


/**
     * 前后端分离的项目，一般不是同源的，存在跨域问题
     * 同源要求浏览器要求协议、域名、端口号完全一致
     * 下面这个配置类可以解决跨域问题
     */

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .maxAge(3600);
    }
}
