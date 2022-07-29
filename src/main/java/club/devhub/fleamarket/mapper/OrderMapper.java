package club.devhub.fleamarket.mapper;


import club.devhub.fleamarket.entity.Order;
import club.devhub.fleamarket.vo.OrderVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
* @author DELL
* @description 针对表【t_order(订单表)】的数据库操作Mapper
* @createDate 2022-07-24 11:05:13
* @Entity club.devhub.fleamarket.entity.Order
*/
@Mapper
public interface OrderMapper  {

    void insert(@Param("commodityId") Long commodityId, @Param("userId")Long userId);

    Order getOrderById(@Param("orderId") Long orderId);

    void changeState(@Param("orderId")Long orderId);

    List<OrderVo> getList(@Param("category") Integer category, @Param("userId") Long userId, @Param("state") Integer state);
}




