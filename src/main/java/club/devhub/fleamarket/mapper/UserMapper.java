package club.devhub.fleamarket.mapper;

import club.devhub.fleamarket.entity.User;
import club.devhub.fleamarket.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* @author DELL
* @description 针对表【t_user】的数据库操作Mapper
* @createDate 2022-07-24 11:05:19
* @Entity club.devhub.fleamarket.entity.User
*/
@Mapper
public interface UserMapper{

    User getByUsername(@Param("username") String username);

    void insert(@Param("username") String username, @Param("password") String password);

    UserVO getVOById(@Param("userId") Long userId);

    /**
     * 将返回更新的行数
     * */
    int updateById(@Param("userId")Long userId, @Param("sex")Integer sex, @Param("address") String address);

    int CountById(@Param("userId") Long userId);

    /**
     * commoditi_num字段减一
     * */
    int increaseCommoditiesNum(@Param("userId")Long userId);
    /**
     * commoditi_num字段加一
     * */
    int decreaseCommoditiesNum(@Param("userId")Long userId);

    int deleteUser(@Param("userId") Long userId);

    Integer getUserById(@Param("userID") Long userId);
}




