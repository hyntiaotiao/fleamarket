package club.devhub.fleamarket.service;

import club.devhub.fleamarket.entity.Reports;
import club.devhub.fleamarket.vo.PageResult;
import club.devhub.fleamarket.vo.ReportVo;


/**
* @author DELL
* @description 针对表【t_reports(举报表)】的数据库操作Service
* @createDate 2022-07-24 11:05:16
*/
public interface ReportsService  {

    PageResult<ReportVo> getList(Integer current, Integer pageSize);

    void review(Long reportId);

    void logOff(Long userId);
}
