package club.devhub.fleamarket.service.impl;

import club.devhub.fleamarket.constant.UserRoleEnum;
import club.devhub.fleamarket.entity.Reports;
import club.devhub.fleamarket.entity.User;
import club.devhub.fleamarket.exception.IllegalOperationException;
import club.devhub.fleamarket.exception.NotFoundException;
import club.devhub.fleamarket.mapper.UserMapper;
import club.devhub.fleamarket.service.ReportsService;
import club.devhub.fleamarket.mapper.ReportsMapper;
import club.devhub.fleamarket.vo.OrderVo;
import club.devhub.fleamarket.vo.PageResult;
import club.devhub.fleamarket.vo.ReportVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author DELL
* @description 针对表【t_reports(举报表)】的数据库操作Service实现
* @createDate 2022-07-24 11:05:16
*/
@Service
public class ReportsServiceImpl implements ReportsService{

    @Autowired
    private ReportsMapper reportsMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public PageResult<ReportVo> getList(Integer current, Integer pageSize) {
        //设定起始页
        PageHelper.startPage(current, pageSize);
        //查询数据
        List<ReportVo> list = reportsMapper.getList();
        PageInfo<ReportVo> pageInfo = new PageInfo<ReportVo>(list);

        //根据pageInfo获取PageResult所需数据
        PageResult<ReportVo> result = new PageResult<ReportVo>();
        result.setTotal(pageInfo.getTotal());
        result.setPrev(pageInfo.isHasPreviousPage() ? pageInfo.getPrePage() : -1);
        result.setNext(pageInfo.isHasNextPage() ? pageInfo.getNextPage() : -1);
        result.setList(list);
        return result;
    }

    /**
     * 改变state，同时函数会返回数据表中更新行数
     * 若更新行数count为0，说明reportID错误*/
    @Override
    public void review(Long reportId) {
       int count = reportsMapper.changeState(reportId);
       if(count==0){
           throw new NotFoundException("reportId错误或不存在");
       }
    }

    /**
     * 先检查要删除的用户是不是管理员（管理员不可以被删除）
     * 删除user，同时函数会返回数据表中删除行数
     * 若更新行数count为0，说明userID错误*/
    @Override
    public void logOff(Long userId) {
        Integer role=userMapper.getUserById(userId);
        if(role == UserRoleEnum.ADMINISTRATOR.getRole()){
            throw new IllegalOperationException("不允许注销管理员");
        }
        int count = userMapper.deleteUser(userId);
        if(count==0){
            throw new NotFoundException("userId错误或不存在");
        }
    }
}



