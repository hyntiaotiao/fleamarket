package club.devhub.fleamarket.config;


import club.devhub.fleamarket.advice.GlobalExceptionHandler;
import club.devhub.fleamarket.entity.User;
import club.devhub.fleamarket.filter.TokenFilter;
import club.devhub.fleamarket.mapper.UserMapper;
import club.devhub.fleamarket.vo.CommonResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 这是SpringSecurity的配置类，一旦看到extends WebSecurityConfigurerAdapter就应该想到这是安全相关的配置
 * 这里继承的是WebSecurityConfigurerAdapter，
 */
@Configuration
//往项目中注入BCryptPasswordEncoder，这是SpringSecurity写好的工具类，它实现了PasswordEncoder接口，可以用它来把密码加密，这样就不用我们自己写加密的逻辑了
@Import(BCryptPasswordEncoder.class)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private TokenFilter tokenFilter;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 下面这个方法用于配置认证方式(如何根据用户名获取用户信息,以及密码加密方式)
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //userDetailsService(userDetailsService)指定如何根据username获取password，方法的参数是我们注入到容器中的bean，
        // 里面写了loadUserByUsername方法，在本项目中是用@Service注入的UserDetailsServiceImpl，你往下翻就能看到了
        //passwordEncoder(passwordEncoder)是指定用那个密码编码器来进行加密，方法的参数是我们注入到容器中的bean，
        // 里面写了如何encode和matches，在本项目中是用@Import注入的BCryptPasswordEncoder
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    /**
     * 用@Bean往容器中注入AuthenticationManager，这样UserServiceImpl才能Autowired它，不然会报错找不到这个类型的bean
     * 我们要用它来帮我们实现认证，如果认证失败会抛出异常
     */
    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    /**
     * 下面这个方法用于配置"保护策略"
     * 在不同的项目中，不拦截URL列表改通常是不同的
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //.csrf().disable()以及.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);是在配置两个不一样的东西
        //csrf是默认开启的，我们需要关闭csrf才行。
        // csrf要求请求携带“_csrf”字段，否则会被拦截，我们前端发的请求都没有“_csrf”，不应该启动这个功能
        //不要用session获取context，因为JWT的优点就是不需要session
        //SessionCreationPolicy.STATELESS就是指禁用session
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        //配置哪些路径需要认证，哪些路径不需要认证
        //配置的顺序很重要，越靠前的规则优先级越高
        //在这个项目中，我们设置了三个URL无需认证，剩下的所有URL都要认证
        //通俗地讲：访问下面这三个URL，无需token
        //但是，你访问地路径如果不是这三个之一，那么就必须带上合法的token，以便我能知道“你是谁”
        //不要搞混认证和授权，先认证再授权，认证在前，授权在后。
        //认证是指要知道“你是谁，你是不是我们系统的用户”，不论你是普通用户还是管理员，都算认证成功，
        //授权是指要知道“你有哪些权限，你是否拥有要访问的接口要求的权限“，必须有要求的权限，才算授权成功
        http
                .authorizeRequests()
                .antMatchers("/api/fleamarket/v1/users/register").permitAll()
                .antMatchers("/api/fleamarket/v1/users/login").permitAll()
                .antMatchers("/api/fleamarket/v1/admins/login").permitAll()
                .anyRequest().authenticated();

        //verifyTokenFilter是我们自定义的一个Filter，我想把它加入过滤器链里面，所以调用addFilterBefore
        //如果你不调用addFilterBefore方法，那么我们写的Filter是不会生效的
        //verifyTokenFilter的详细功能请见那个类里面注释
        http.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);

        //这里是配置认证失败处理器
        //认证失败而抛出的异常是在Filter中抛出的，全局异常处理器是[捕捉不到在Filter中抛出的异常的]，因为全局异常处理器只能捕捉到经过Filter之后才出现的异常
        //我们用authenticationEntryPoint方法指定在Filter中抛出的异常由谁去处理，可以认为在这里指定“认证失败”抛出的异常由谁去处理
        http.exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint);
        //允许跨域
        http.cors();

        //SpringSecurity还提供了认证成功处理器，认证失败处理器，注销成功处理器
        //例如http.formLogin().successHandler(你的认证成功处理器).failureHandler(你的认证失败处理器);
        //例如http.logout().logoutSuccessHandler(你的注销成功处理器);
        //我们完全用不到这些东西，你只用知道有这玩意儿就行

    }

    /**
     * 这个类就是我们自定义如何去从数据库，根据用户名拿到用户信息的
     */
    @Service
    public static class UserDetailsServiceImpl implements UserDetailsService {

        @Autowired
        private UserMapper userMapper;

        /**
         * 这步就是根据输入Controller层输入地username去数据库拿出加密之后的password，准确地说，它是把整个用户都拿出来了，封装成UserDetails
         * 至于Controller层输入的密码是否和数据库里保存地密码一样，这个判断是由SpringSecurity帮我们做的
         * 它会把Controller层输入的密码按照PasswordEncoder进行加密，然后去和数据库里的加密后的密码判断是否匹配
         * 如果不匹配就会抛异常。（抛异常也没事，我已经在UserServiceImpl的login方法里面用try-catch捕获了）
         */
        @Override
        public UserDetails loadUserByUsername(String username) {
            User user = userMapper.getByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException("用户名为" + username + "的账户不存在");
            }
            return user;
        }
    }

    /**
     * 认证失败处理器，专门负责处理在Filter中抛出的异常
     */
    @Component
    @Slf4j
    public static class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

        @Autowired
        private ObjectMapper objectMapper;

        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
            //设置HTTP状态码为401，表示认证失败
            log.warn(GlobalExceptionHandler.formatException(authException, request, null, false));
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().print(objectMapper.writeValueAsString(CommonResult.failure("[SpringSecurity] 认证失败，因为访问的接口需要token，但是token缺失/token无效/token过期")));
        }
    }

}