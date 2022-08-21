package club.devhub.fleamarket.service.impl;

import club.devhub.fleamarket.constant.ResultCodeEnum;
import club.devhub.fleamarket.entity.Commodity;
import club.devhub.fleamarket.exception.BusinessException;
import club.devhub.fleamarket.exception.IllegalOperationException;
import club.devhub.fleamarket.exception.NotFoundException;
import club.devhub.fleamarket.mapper.ReportsMapper;
import club.devhub.fleamarket.mapper.UserMapper;
import club.devhub.fleamarket.service.CommodityService;
import club.devhub.fleamarket.mapper.CommodityMapper;
import club.devhub.fleamarket.utils.RedisLock;
import club.devhub.fleamarket.vo.CommodityVO;
import club.devhub.fleamarket.vo.PageResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author DELL
* @description 针对表【t_commodity】的数据库操作Service实现
* @createDate 2022-07-24 10:56:48
*/
@Service
@Slf4j
public class CommodityServiceImpl implements CommodityService{

    @Value("${commodity.maxCount}")
    private int maxCount;

    @Autowired
    private CommodityMapper commodityMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ReportsMapper reportMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 这里添加了基于redis的分布式锁：
     *  对用户的物品数量进行上锁（锁名为"publish:"+userId）
     * 1、判断用户发布的物品数量是否小于最大值
     * 2、插入物品信息；用户发布的物品数量加一
     */
    @Transactional
    @Override
    public void publish(String commodityName, Integer category, Integer price, String message, Long userId) {
        RedisLock redisLock=new RedisLock("publish_"+userId,stringRedisTemplate);
        boolean isLock=redisLock.tryLock(10);
        try{
            if(isLock){
                int count=userMapper.CountCommodityById(userId);
                log.info("count:"+count);
                if(count<maxCount){
                    commodityMapper.insert(commodityName,category,price,message,userId);
                    userMapper.increaseCommoditiesNum(userId);
                }else{
                    throw new BusinessException(ResultCodeEnum.MAXIMUM_NUMBER_COMMODITIES,"当前用户发布的物品数量已经上限");
                }
            }else{
                log.info("互斥锁 \"publish_\"+userId 已存在");
                throw new BusinessException(ResultCodeEnum.THE_SERVER_IS_BUSY,"服务器繁忙，请稍后重试");
            }
        }finally {
            //释放锁
            redisLock.unLock();
        }
    }

    /**
     * 1、根据commodityId返回物品，以判断其是否存在
     * 2、检查commodity与user是否对应（用户只能修改自己的物品）
     * 3、更新物品信息
     */
    @Override
    public void edit(Long userId, Long commodityId, String commodityName, Integer category, Integer price, String message) {
         Commodity commodity = commodityMapper.getCommodityById(commodityId);
         if(commodity.equals(null)){
             throw new NotFoundException("物品id错误或不存在");
         }
         Long id=commodity.getUserId();
         if(!id.equals(userId)){
             throw new IllegalOperationException("用户尝试更新不属于它的物品的信息");
         }
         commodityMapper.update(commodityId,commodityName,category,price,message);
    }
    /**
     * 1、根据commodityId返回物品，以判断其是否存在
     * 2、检查commodity与user是否对应（用户只能删除自己的物品）
     * 3、删除物品
     * 4、user中的commodi_num字段减一
     */
    @Override
    @Transactional
    public void delete(Long commodityId, Long userId) {
        RedisLock redisLock=new RedisLock("delete_"+userId,stringRedisTemplate);
        boolean isLock=redisLock.tryLock(10);
        try{
            if(isLock){
                Commodity commodity = commodityMapper.getCommodityById(commodityId);
                if(commodity.equals(null)){
                    throw new NotFoundException("物品id错误或不存在");
                }
                Long id=commodity.getUserId();
                if(!id.equals(userId)){
                    throw new IllegalOperationException("用户尝试删除不属于它的物品的信息");
                }
                commodityMapper.deleteById(commodityId);
                userMapper.decreaseCommoditiesNum(userId);
            }else{
                log.info("互斥锁 \"delete_\"+userId 已存在");
                throw new BusinessException(ResultCodeEnum.THE_SERVER_IS_BUSY,"服务器繁忙，请稍后重试");
            }
        }finally {
            //释放锁
            redisLock.unLock();
        }
    }

    /**
     * 1、根据commodityId返回物品，以判断其是否存在
     * 2、返回commodityVo
     */
    @Override
    public CommodityVO getCommodityDetails(Long commodityId) {
        CommodityVO commodityVo = commodityMapper.getCommodityVoById(commodityId);
        if(commodityVo.equals(null)){
            throw new NotFoundException("物品id错误或不存在");
        }
        return commodityVo;
    }

    /**
     * pagehelper是mybatis 提供的分页插件,可以很方便的获取很多分页参数
     * */
    @Override
    public PageResult<CommodityVO> search(Integer category, Long userId, Integer sold, Integer current, Integer pageSize) {
        //设定起始页
        PageHelper.startPage(current, pageSize);
        //查询数据
        List<CommodityVO> list = commodityMapper.getList(category, userId,sold);
        PageInfo<CommodityVO> pageInfo = new PageInfo<>(list);

        //根据pageInfo获取PageResult所需数据
        PageResult<CommodityVO> result = new PageResult<>();
        result.setTotal(pageInfo.getTotal());
        result.setPrev(pageInfo.isHasPreviousPage() ? pageInfo.getPrePage() : -1);
        result.setNext(pageInfo.isHasNextPage() ? pageInfo.getNextPage() : -1);
        result.setList(list);
        return result;
    }

    /**
     * 举报物品：
     * 1、检查commodity是否存在
     * 2、检查commodityId和userId是否对应（不可以举报自己的物品）
     * 3、插入一条举报信息
     * 注：要注意并行问题
     * */
    @Override
    public void report(Long userId, Long commodityId, String reason) {
        Commodity commodity = commodityMapper.getCommodityById(commodityId);
        if(commodity.equals(null)){
            throw new NotFoundException("物品id错误或不存在");
        }
        if(userId.equals(commodity.getUserId())){
            throw new IllegalOperationException("用户尝试举报自己上传的物品");
        }
        int count = reportMapper.count(commodityId, userId);
        if (count > 0) {
            log.info("userId为{}的用户重复举报messageId为{}的留言", userId, commodity);
            throw new BusinessException(ResultCodeEnum.REPEAT_OPERATION);
        }
        try {
            reportMapper.insert(commodityId, userId,reason);
        } catch (DuplicateKeyException e) {
            log.info("userId为{}的用户重复举报commodityId为{}的留言", userId, commodity);
            throw new BusinessException(ResultCodeEnum.REPEAT_OPERATION);
        }
    }
}




