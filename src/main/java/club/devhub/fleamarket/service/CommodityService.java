package club.devhub.fleamarket.service;

import club.devhub.fleamarket.entity.Commodity;
import club.devhub.fleamarket.vo.CommodityVo;
import club.devhub.fleamarket.vo.PageResult;

/**
* @author DELL
* @description 针对表【t_commodity】的数据库操作Service
* @createDate 2022-07-24 10:56:48
*/
public interface CommodityService  {

    void publish(String commodityName, Integer category, Integer price, String message, Long userId);

    void edit(Long userId, Long commodityId, String commodityName, Integer category, Integer price, String message);

    void delete(Long commodityId, Long userId);

    CommodityVo getCommodityDetails(Long commodityId);

    PageResult<CommodityVo> search(Integer category, String userId, Integer sold, Integer current, Integer pageSize);

    void report(Long userId, Long commodityId, String reason);
}
