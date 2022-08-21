package club.devhub.fleamarket.service;

import club.devhub.fleamarket.entity.Evaluation;

/**
* @author DELL
* @description 针对表【t_evaluation(评价表)】的数据库操作Service
* @createDate 2022-07-24 11:05:09
*/
public interface EvaluationService {

    /**
     * 评价某次交易（订单）
     */
    void evaluate(Long userId, Long orderId, String evaluation);

    /**
     * 编辑评价
     */
    void edit(Long userId, Long orderId, String evaluation);

    /**
     * 删除自己（评价者）的某个评价
     */
    void delete(Long userId, Long evaluateId);
}
