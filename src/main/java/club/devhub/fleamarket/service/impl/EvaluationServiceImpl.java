package club.devhub.fleamarket.service.impl;

import club.devhub.fleamarket.constant.ResultCodeEnum;
import club.devhub.fleamarket.entity.Evaluation;
import club.devhub.fleamarket.entity.Order;
import club.devhub.fleamarket.exception.BusinessException;
import club.devhub.fleamarket.exception.IllegalOperationException;
import club.devhub.fleamarket.exception.NotFoundException;
import club.devhub.fleamarket.mapper.OrderMapper;
import club.devhub.fleamarket.service.EvaluationService;
import club.devhub.fleamarket.mapper.EvaluationMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

/**
* @author DELL
* @description 针对表【t_evaluation(评价表)】的数据库操作Service实现
* @createDate 2022-07-24 11:05:09
*/
@Service
@Slf4j
public class EvaluationServiceImpl implements EvaluationService{

    @Autowired
    private EvaluationMapper evaluationMapper;

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 1、检查order是否存在
     * 2、检查commodity是不是userId购买的
     * 3、插入评论*/
    @Override
    public void evaluate(Long userId, Long orderId, String evaluation) {
        Order order = orderMapper.getOrderById(orderId);
        if(order.equals(null)){
            throw new NotFoundException("物品id错误或不存在");
        }
        if(!order.getBuyerId().equals(userId)){
            throw new IllegalOperationException("用户尝试评价不属于自己购买的物品");
        }
        Long commodityId=order.getCommodityId();
        try {
            evaluationMapper.insert(commodityId,evaluation);
        } catch (DuplicateKeyException e) {
            log.info("userId为{}的用户重复评价orderId为{}的订单（物品）", userId, orderId);
            throw new BusinessException(ResultCodeEnum.REPEAT_OPERATION);
        }
    }

    /**
     * 1、检查order是否存在
     * 2、检查commodity是不是userId购买的
     * 3、更新评论*/
    @Override
    public void edit(Long userId, Long orderId, String evaluation) {
        Order order = orderMapper.getOrderById(orderId);
        if(order.equals(null)){
            throw new NotFoundException("物品id错误或不存在");
        }
        if(!order.getBuyerId().equals(userId)){
            throw new IllegalOperationException("用户尝试评价不属于自己购买的物品");
        }
        Long commodityId=order.getCommodityId();
        try {
            evaluationMapper.update(commodityId, userId,evaluation);
        } catch (BusinessException e) {
            log.info("userId为{}的用户重复评价orderId为{}的订单（物品）", userId, orderId);
            throw new BusinessException(ResultCodeEnum.REPEAT_OPERATION);
        }
    }

    /**
     * 1、检查order是否存在
     * 2、检查commodity是不是userId购买的
     * 3、删除评论*/
    @Override
    public void delete(Long userId, Long evaluationId) {
        Evaluation evaluation=evaluationMapper.getEvaluationById(evaluationId);
        if(evaluation.equals(null)){
            throw new NotFoundException("评价id错误或不存在");
        }
        if(!evaluationMapper.getUserIdById(evaluationId).equals(userId)){
            throw new IllegalOperationException("用户尝试删除不属于自己发出的评价");
        }
        evaluationMapper.delete(evaluationId);

    }
}




