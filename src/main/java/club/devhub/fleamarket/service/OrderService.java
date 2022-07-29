package club.devhub.fleamarket.service;

import club.devhub.fleamarket.entity.Order;
import club.devhub.fleamarket.entity.User;
import club.devhub.fleamarket.vo.OrderVo;
import club.devhub.fleamarket.vo.PageResult;

/**
* @author DELL
* @description 针对表【t_order(订单表)】的数据库操作Service
* @createDate 2022-07-24 11:05:13
*/
public interface OrderService  {

    void buy(Long commodityId, Long userId);

    void sell(Long orderId, Long userId);

    PageResult<OrderVo> search(Integer category, Long userId, Integer state, Integer current, Integer pageSize);
}
