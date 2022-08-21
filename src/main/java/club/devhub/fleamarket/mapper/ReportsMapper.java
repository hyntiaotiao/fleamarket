package club.devhub.fleamarket.mapper;

import club.devhub.fleamarket.entity.Commodity;
import club.devhub.fleamarket.entity.Report;
import club.devhub.fleamarket.vo.ReportVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
* @author DELL
* @description 针对表【t_reports(举报表)】的数据库操作Mapper
* @createDate 2022-07-24 11:05:16
* @Entity club.devhub.fleamarket.entity.Reports
*/
@Mapper
public interface ReportsMapper  {

    void insert(@Param("commodityId") Long commodityId, @Param("userId") Long userId, @Param("reason") String reason);

    List<ReportVO> getList();

    int setStateToAudited(@Param("reportId") Long reportId);

    Report getReportById(@Param("reportId") Long reportId);

    int count(@Param("commodityId") Long commodityId, @Param("userId") Long userId);
}




