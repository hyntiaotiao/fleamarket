package club.devhub.fleamarket.mapper;

import club.devhub.fleamarket.entity.Evaluation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
* @author DELL
* @description 针对表【t_evaluation(评价表)】的数据库操作Mapper
* @createDate 2022-07-24 11:05:09
* @Entity club.devhub.fleamarket.entity.Evaluation
*/
@Mapper
public interface EvaluationMapper  {

    void insert(@Param("commodityId") Long commodityId, @Param("userId") Long userId, @Param("evaluation") String evaluation);

    void update(@Param("commodityId") Long commodityId, @Param("userId") Long userId, @Param("evaluation") String evaluation);

    Evaluation getEvaluationById(@Param("evaluateId") Long evaluateId);

    int delete(@Param("evaluateId") Long evaluateId);
}




