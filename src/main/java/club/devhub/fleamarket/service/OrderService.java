package club.devhub.fleamarket.service;

import club.devhub.fleamarket.vo.OrderVO;
import club.devhub.fleamarket.vo.PageResult;

/**
* @author DELL
* @description 针对表【t_order(订单表)】的数据库操作Service
* @createDate 2022-07-24 11:05:13
*/
public interface OrderService  {

    /**
     * 买家提出购买（生成订单）
     */
    void buy(Long commodityId, Long userId,String address);

    /**
     * 卖家确认订单（完成交易）
     */
    void sell(Long orderId, Long userId);

    /**
     *根据指定条件返回订单的分页结果
     */
    PageResult<OrderVO> search(Integer category, Long userId, Integer state, Integer current, Integer pageSize);
}
