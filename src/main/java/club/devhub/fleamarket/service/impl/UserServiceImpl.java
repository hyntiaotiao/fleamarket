package club.devhub.fleamarket.service.impl;

import club.devhub.fleamarket.constant.ResultCodeEnum;
import club.devhub.fleamarket.constant.UserStatusEnum;
import club.devhub.fleamarket.entity.User;
import club.devhub.fleamarket.exception.BusinessException;
import club.devhub.fleamarket.exception.NotFoundException;
import club.devhub.fleamarket.service.UserService;
import club.devhub.fleamarket.mapper.UserMapper;
import club.devhub.fleamarket.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author DELL
 * @description 针对表【t_user】的数据库操作Service实现
 * @createDate 2022-07-24 11:05:19
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;


    /**
     * PasswordEncoder用于用户注册，把用户密码加密后，也即是passwordEncoder.encode()之后，
     * 再存入数据库，不应该存储密码明文到数据库
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 自动进行认证
     * */
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
    * 在数据库中给username添加唯一索引，防止出错
    * */
    @Override
    public void register(String username, String password) {
        /**
         * 有可能报错，用户名重复，或者俩个人同时注册同一个用户名
         * 所以要捕获DuplicateKeyException 重复键异常*/
        try {
            userMapper.insert(username, passwordEncoder.encode(password));
        } catch (DuplicateKeyException e) {
            throw new BusinessException(ResultCodeEnum.USERNAME_ALREADY_EXIST, "用户名" + username + "已存在");
        }
    }

    @Override
    public User login(String username, String password) {
        User user=userMapper.getByUsername(username);
        if(user.getUserStatus().equals(UserStatusEnum.BANNED.getCode())){
            log.warn("[登录失败]  尝试登录失败，失败原因：账号已经被封禁");
            throw new BusinessException(ResultCodeEnum.ACCOUNT_IS_BLOCKED);
        }
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (AuthenticationException e) {
            log.warn("[登录失败]  尝试登录失败，失败原因：{}", e.getMessage());
            throw new BusinessException(ResultCodeEnum.WRONG_USERNAME_OR_PASSWORD);
        }
        return (User) authentication.getPrincipal();
    }

    @Override
    public UserVO getUserInfo(Long userId) {
        UserVO userVo=userMapper.getVOById(userId);
        if(userVo.equals(null)){
            throw new NotFoundException("用户不存在");
        }
        return userVo;
    }

    /**
     * updateById方法返回更新行数，
     * 如果更新行数为0，说明用户名不存在
     * */
    @Override
    public void modifyInfo(Long userId, Integer sex, String address) {
        int matched = userMapper.updateById(userId, sex,address);
        if (matched == 0) {
            throw new NotFoundException("userId为" + userId + "的用户不存在");
        }
    }
}




