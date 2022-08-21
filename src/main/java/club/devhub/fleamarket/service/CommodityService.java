package club.devhub.fleamarket.service;

import club.devhub.fleamarket.vo.CommodityVO;
import club.devhub.fleamarket.vo.PageResult;

/**
* @author DELL
* @description 针对表【t_commodity】的数据库操作Service
* @createDate 2022-07-24 10:56:48
*/
public interface CommodityService  {

    /**
     * 发布物品
     */
    void publish(String commodityName, Integer category, Integer price, String message, Long userId);

    /**
     * 编辑物品
     */
    void edit(Long userId, Long commodityId, String commodityName, Integer category, Integer price, String message);

    /**
     * 用户删除自己的某个物品
     */
    void delete(Long commodityId, Long userId);

    /**
     * 根据id获取CommodityVO
     */
    CommodityVO getCommodityDetails(Long commodityId);

    /**
     * 根据指定条件获取分页结果
     */
    PageResult<CommodityVO> search(Integer category, Long userId, Integer sold, Integer current, Integer pageSize);

    /**
     * 举报某物品
     */
    void report(Long userId, Long commodityId, String reason);
}
