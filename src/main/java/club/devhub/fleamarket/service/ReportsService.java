package club.devhub.fleamarket.service;

import club.devhub.fleamarket.vo.PageResult;
import club.devhub.fleamarket.vo.ReportVO;


/**
* @author DELL
* @description 针对表【t_reports(举报表)】的数据库操作Service
* @createDate 2022-07-24 11:05:16
*/
public interface ReportsService  {

    PageResult<ReportVO> getList(Integer current, Integer pageSize);

    /**
     * 评审某次举报
     */
    void review(Long reportId,int isIlleagl);

    /**
     * 对违规用户进行封号
     */
    void banOff(Long userId);
}
