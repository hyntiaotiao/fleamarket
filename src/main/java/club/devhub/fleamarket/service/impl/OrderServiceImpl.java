package club.devhub.fleamarket.service.impl;

import club.devhub.fleamarket.constant.ResultCodeEnum;
import club.devhub.fleamarket.entity.Commodity;
import club.devhub.fleamarket.entity.Order;
import club.devhub.fleamarket.exception.BusinessException;
import club.devhub.fleamarket.exception.NotFoundException;
import club.devhub.fleamarket.mapper.CommodityMapper;
import club.devhub.fleamarket.mapper.OrderMapper;
import club.devhub.fleamarket.service.OrderService;
import club.devhub.fleamarket.vo.CommodityVo;
import club.devhub.fleamarket.vo.OrderVo;
import club.devhub.fleamarket.vo.PageResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author DELL
* @description 针对表【t_order(订单表)】的数据库操作Service实现
* @createDate 2022-07-24 11:05:13
*/
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private CommodityMapper commodityMapper;

    /**
     * 1、检查物品是否存在
     * 2、检查是否重复购买
     * 3、插入一条订单*/
    @Override
    public void buy(Long commodityId, Long userId) {
        Commodity commodity = commodityMapper.getCommodityById(commodityId);
        if(commodity==null){
            throw new NotFoundException("物品id错误或不存在");
        }
        try {
            orderMapper.insert(commodityId,userId);
        } catch (DuplicateKeyException e) {
            log.info("userId为{}的用户重复购买commodityId为{}的物品", userId, commodity);
            throw new BusinessException(ResultCodeEnum.REPEAT_OPERATION);
        }

    }

    /**
     * 1、检查订单是否存在
     * 2、修改订单状态*/
    @Override
    public void sell(Long orderId, Long userId) {
        Order order = orderMapper.getOrderById(orderId);
        if(order==null){
            throw new NotFoundException("订单id错误或不存在");
        }
        orderMapper.changeState(orderId);
        commodityMapper.changeSold(order.getCommodityId());
    }

    @Override
    public PageResult<OrderVo> search(Integer category, Long userId, Integer state, Integer current, Integer pageSize) {
        //设定起始页
        PageHelper.startPage(current, pageSize);
        //查询数据
        List<OrderVo> list = orderMapper.getList(category, userId,state);
        PageInfo<OrderVo> pageInfo = new PageInfo<>(list);

        //根据pageInfo获取PageResult所需数据
        PageResult<OrderVo> result = new PageResult<>();
        result.setTotal(pageInfo.getTotal());
        result.setPrev(pageInfo.isHasPreviousPage() ? pageInfo.getPrePage() : -1);
        result.setNext(pageInfo.isHasNextPage() ? pageInfo.getNextPage() : -1);
        result.setList(list);
        return result;
    }
}




