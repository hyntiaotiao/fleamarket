package club.devhub.fleamarket.mapper;

import club.devhub.fleamarket.entity.Commodity;
import club.devhub.fleamarket.vo.CommodityVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
* @author DELL
* @description 针对表【t_commodity】的数据库操作Mapper
* @createDate 2022-07-24 10:56:48
* @Entity club.devhub.fleamarket.entity.Commodity
*/
@Mapper
public interface CommodityMapper  {
    void insert(@Param("commodityName") String commodityName,
                @Param("category") Integer category,
                @Param("price") Integer price,
                @Param("message") String message,
                @Param("userId") Long userId
    );

    Commodity getCommodityById(@Param("commmodityId") Long commodityId);

    void update(@Param("commodityId") Long commodityId,
                @Param("commodityName") String commodityName,
                @Param("category") Integer category,
                @Param("price") Integer price,
                @Param("message") String message);

    int deleteById(@Param("commodityId") Long commodityId);

    CommodityVo getCommodityVoById(@Param("commodityId") Long commodityId);

    List<CommodityVo> getList(@Param("category") Integer category, @Param("userId") String userId, @Param("sold") Integer sold);

    void changeSold(@Param("commodiyuId") Long commodityId);
}




