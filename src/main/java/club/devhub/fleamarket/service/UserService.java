package club.devhub.fleamarket.service;

import club.devhub.fleamarket.entity.User;
import club.devhub.fleamarket.vo.UserVO;

/**
* @author DELL
* @description 针对表【t_user】的数据库操作Service
* @createDate 2022-07-24 11:05:19
*/
public interface UserService  {

    void register(String username, String password);

    User login(String username, String password);

    UserVO getUserInfo(Long userId);

    void modifyInfo(Long userId, Integer sex, String address);
}
