package club.devhub.fleamarket.service.impl;

import club.devhub.fleamarket.constant.UserRoleEnum;
import club.devhub.fleamarket.entity.Commodity;
import club.devhub.fleamarket.entity.Report;
import club.devhub.fleamarket.exception.IllegalOperationException;
import club.devhub.fleamarket.exception.NotFoundException;
import club.devhub.fleamarket.mapper.CommodityMapper;
import club.devhub.fleamarket.mapper.UserMapper;
import club.devhub.fleamarket.service.ReportsService;
import club.devhub.fleamarket.mapper.ReportsMapper;
import club.devhub.fleamarket.vo.PageResult;
import club.devhub.fleamarket.vo.ReportVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author DELL
* @description 针对表【t_reports(举报表)】的数据库操作Service实现
* @createDate 2022-07-24 11:05:16
*/
@Service
public class ReportServiceImpl implements ReportsService{

    @Autowired
    private ReportsMapper reportMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommodityMapper commodityMapper;

    @Override
    public PageResult<ReportVO> getList(Integer current, Integer pageSize) {
        //设定起始页
        PageHelper.startPage(current, pageSize);
        //查询数据
        List<ReportVO> list = reportMapper.getList();
        PageInfo<ReportVO> pageInfo = new PageInfo<ReportVO>(list);

        //根据pageInfo获取PageResult所需数据
        PageResult<ReportVO> result = new PageResult<ReportVO>();
        result.setTotal(pageInfo.getTotal());
        result.setPrev(pageInfo.isHasPreviousPage() ? pageInfo.getPrePage() : -1);
        result.setNext(pageInfo.isHasNextPage() ? pageInfo.getNextPage() : -1);
        result.setList(list);
        return result;
    }

    /**
     *若确实违规，将封禁违规账户*/
    @Override
    @Transactional
    public void review(Long reportId,int isIllegal) {
        Report report = reportMapper.getReportById(reportId);
        if(report.equals(null)){
           throw new NotFoundException("reportId错误或不存在");
        }
        if(isIllegal==1){
           Commodity commodity = commodityMapper.getCommodityById(report.getCommodityId());
            if(commodity.equals(null)){
                throw new NotFoundException("commodityId错误或不存在");
            }
            banOff(commodity.getUserId());

        }
        reportMapper.setStateToAudited(reportId);
    }

    /**
     * 先检查要删除的用户是不是管理员（管理员不可以被封禁）
     * 封禁user，同时函数会返回数据表中删除行数
     * 若更新行数count为0，说明userID错误*/
    @Override
    public void banOff(Long userId) {
        Integer role=userMapper.getRoleById(userId);
        if(role.equals(UserRoleEnum.ADMINISTRATOR.getRole())){
            throw new IllegalOperationException("不允许封禁管理员账户");
        }
        int count = userMapper.banOffById(userId);
        if(count==0){
            throw new NotFoundException("userId错误或不存在");
        }
    }
}



