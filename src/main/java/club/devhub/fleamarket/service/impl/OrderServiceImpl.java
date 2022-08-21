package club.devhub.fleamarket.service.impl;

import club.devhub.fleamarket.constant.ResultCodeEnum;
import club.devhub.fleamarket.entity.Commodity;
import club.devhub.fleamarket.entity.Order;
import club.devhub.fleamarket.exception.BusinessException;
import club.devhub.fleamarket.exception.IllegalOperationException;
import club.devhub.fleamarket.exception.NotFoundException;
import club.devhub.fleamarket.mapper.CommodityMapper;
import club.devhub.fleamarket.mapper.OrderMapper;
import club.devhub.fleamarket.service.OrderService;
import club.devhub.fleamarket.vo.OrderVO;
import club.devhub.fleamarket.vo.PageResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * 3、插入一条订单
     * 4、修改物品状态为已卖出*/
    @Override
    @Transactional
    public void buy(Long commodityId, Long userId,String address) {
        Commodity commodity = commodityMapper.getCommodityById(commodityId);
        if(commodity.equals(null)){
            throw new NotFoundException("物品id错误或不存在");
        }
        try {
            orderMapper.insert(commodityId,userId,address);
        } catch (DuplicateKeyException e) {
            log.info("userId为{}的用户重复购买commodityId为{}的物品", userId, commodity);
            throw new BusinessException(ResultCodeEnum.REPEAT_OPERATION);
        }
        commodityMapper.changeSold(commodityId);
    }

    /**
     * 1、检查订单是否存在
     * 2、卖家才能够执行“确认卖出操作”
     * 3、修改订单状态*/
    @Override
    public void sell(Long orderId, Long userId) {
        Order order = orderMapper.getOrderById(orderId);
        if(order.equals(null)){
            throw new NotFoundException("订单id错误或不存在");
        }
        Commodity commodity = commodityMapper.getCommodityById(order.getCommodityId());
        if(!commodity.getUserId().equals(userId)){
            throw new IllegalOperationException("卖家才能执行“确认卖出操作”");
        }
        orderMapper.setStateToCompleted(orderId);
    }

    @Override
    public PageResult<OrderVO> search(Integer category, Long userId, Integer state, Integer current, Integer pageSize) {
        //设定起始页
        PageHelper.startPage(current, pageSize);
        //查询数据
        List<OrderVO> list = orderMapper.getList(category, userId,state);
        PageInfo<OrderVO> pageInfo = new PageInfo<>(list);

        //根据pageInfo获取PageResult所需数据
        PageResult<OrderVO> result = new PageResult<>();
        result.setTotal(pageInfo.getTotal());
        result.setPrev(pageInfo.isHasPreviousPage() ? pageInfo.getPrePage() : -1);
        result.setNext(pageInfo.isHasNextPage() ? pageInfo.getNextPage() : -1);
        result.setList(list);
        return result;
    }
}




