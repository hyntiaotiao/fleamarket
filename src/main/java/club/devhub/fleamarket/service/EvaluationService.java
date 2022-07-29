package club.devhub.fleamarket.service;

import club.devhub.fleamarket.entity.Evaluation;

/**
* @author DELL
* @description 针对表【t_evaluation(评价表)】的数据库操作Service
* @createDate 2022-07-24 11:05:09
*/
public interface EvaluationService {

    void evaluate(Long userId, Long orderId, String evaluation);

    void edit(Long userId, Long orderId, String evaluation);

    void delete(Long userId, Long evaluateId);
}
