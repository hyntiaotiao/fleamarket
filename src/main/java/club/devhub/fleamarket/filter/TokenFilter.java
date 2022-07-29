package club.devhub.fleamarket.filter;

import club.devhub.fleamarket.entity.User;
import club.devhub.fleamarket.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 这是一个Filter（过滤器），其职能为：
 * （1）如果请求没带token，则[不设置]Authentication标识为true，并且放行
 * （2）如果请求带了token，但是token是无效的，抛出异常，不会进入下一个Filter，异常会被认证失败处理器捕获，返回HTTP401
 * （3）如果请求带了token，而且token是有效的，[设置]Authentication标识为true，并且放行
 * 仅仅用@Component注入IOC容器还不够，还需要在SpringConfig里面配置
 * SpringSecurity是由一串过滤器链实现的，而这里就是我们自定义的一个过滤器
 */
@Component
@Slf4j
public class TokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("=================请求进来了=================");
        log.info("[新请求]\n<方法>  {}\n<URI>  {}\n<Query>  {}\n<主机>  {}",
                request.getMethod(), request.getRequestURI(), request.getQueryString(), request.getRemoteHost());
        String authorization = request.getHeader("authorization");

        //如果有token，就执行token解析，若解析成功，就设置Authenticated为true，表示认证成功
        if (StringUtils.hasText(authorization)) {
            User user;
            try {
                user = jwtUtil.getUserFromToken(authorization);
            } catch (Exception e) {
                log.warn("在解析token时出错，错误原因:{}", e.getMessage());
                throw new BadCredentialsException(e.getMessage());
            }

            //new UsernamePasswordAuthenticationToken这个三参数构造器，你点进去源码
            // 有一句就是super.setAuthenticated(true);，隐式地把布尔值Authenticated为true，这样过滤链结尾就不会抛异常被拦了，能够进入controller里面
            //如果你不把Authenticated这个布尔值设置为true，在过滤链的结尾处，会认证失败，无法进入controller
            //所以请调用UsernamePasswordAuthenticationToken含有三个参数的构造器，确保这个布尔值为true
            //这个是构造器的方法签名： public UsernamePasswordAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities)
            //你在controller的方法参数列表中，就可以用@AuthenticationPrincipal提取把这里传进去的参数拿出来，
            // 拿出的是什么呢？就是下面UsernamePasswordAuthenticationToken构造器的第一个参数：user，
            // 所以你今后再看到@AuthenticationPrincipal User user就不会感到惊讶了，它就是在这里SecurityContextHolder.getContext().setAuthentication设置的值！
            //虽然这个过滤器只负责认证，不负责授权，但顺便讲一下授权原理吧，Controller方法上@PreAuthorize("hasAnyRole('ADMIN')")注解就是用来授权的，
            // 注解里的内容都是我们写好的，例如hasAnyRole('ADMIN')要求管理员才能进入这个接口，但是用户有哪些权限，我们怎么知道呢？
            // SpringSecurity会从SecurityContextHolder中getContext()，然后获取其中的Authentication，从Authentication中提出getAuthorities，然后去和注解里的内容进行对比
            // 拿出的是什么呢？就是下面UsernamePasswordAuthenticationToken构造器的第三个参数：user.getAuthorities()，
            // 我们把用户具有的权限用SecurityContextHolder.getContext().setAuthentication也告诉SpringSecurity了
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities())
            );

        }
        //如果token是空的，也必须放行，因为那些注册和登录接口就无需带token，如果按照没带token一律抛异常的逻辑，那么注册和登录接口就没法用了
        //那不带token会不会也进入需要认证的接口呢？其实不用担心。不带token的请求在这个filter中，不会设置布尔值“Authenticated”为true，
        //所以这个请求只有在访问无需认证接口（注册/登录）时不会被拦，在访问需要认证接口的时候会被拦
        //至于哪些接口属于无需认证，哪些接口需要认证，请见SecurityConfig类
        filterChain.doFilter(request, response);
        log.info("=================请求离开了=================");

    }
}