package club.devhub.fleamarket.mapper;

import club.devhub.fleamarket.entity.Commodity;
import club.devhub.fleamarket.entity.Reports;
import club.devhub.fleamarket.vo.ReportVo;
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

    int count(@Param("commodity") Commodity commodity, @Param("userId") Long userId);

    void insert(@Param("commodityId") Long commodityId, @Param("userId") Long userId, @Param("reason") String reason);

    List<ReportVo> getList();

    int changeState(@Param("reportId") Long reportId);
}




