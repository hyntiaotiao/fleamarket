package club.devhub.fleamarket.service;

import club.devhub.fleamarket.entity.User;
import club.devhub.fleamarket.vo.UserVO;

/**
* @author DELL
* @description 针对表【t_user】的数据库操作Service
* @createDate 2022-07-24 11:05:19
*/
public interface UserService  {

    /**
     * 用户注册
     */
    void register(String username, String password);

    /**
     * 用户登录
     */
    User login(String username, String password);

    /**
     *更具ID获取UserVO
     */
    UserVO getUserInfo(Long userId);

    /**
     * 修改个人信息
     */
    void modifyInfo(Long userId, Integer sex, String address);
}
