package club.devhub.fleamarket.controller;

import club.devhub.fleamarket.annotation.Idempotent;
import club.devhub.fleamarket.entity.Commodity;
import club.devhub.fleamarket.entity.User;
import club.devhub.fleamarket.mapper.CommodityMapper;
import club.devhub.fleamarket.param.CommodityParam;
import club.devhub.fleamarket.param.PagingParam;
import club.devhub.fleamarket.param.ReportCommodityParam;
import club.devhub.fleamarket.param.SearchCommodityParam;
import club.devhub.fleamarket.service.CommodityService;
import club.devhub.fleamarket.service.UserService;
import club.devhub.fleamarket.vo.CommodityVo;
import club.devhub.fleamarket.vo.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping("/api/fleamarket/v1/commodity")
public class CommodityController {
    @Autowired
    CommodityService commodityService;

    /**
    *用户上传物品信息：
    *规定上传的物品数量有限，最大值在配置文件中读取*/
    @RequestMapping()
    @PreAuthorize("hasAnyRole('USER')")
    public void publish(@AuthenticationPrincipal User user, @RequestBody @Valid CommodityParam commodityParam){
        commodityService.publish(commodityParam.getCommodityName(),
                commodityParam.getCategory(),
                commodityParam.getPrice(),
                commodityParam.getMessage(),
                user.getUserId());
    }
    /**
     *更新物品信息（用户只能更新自己的物品的信息）*/
    @PutMapping("{commodityId}")
    @PreAuthorize("hasAnyRole('USER')")
    public void edit(@AuthenticationPrincipal User user,@PathVariable Long commodityId, @RequestBody @Valid CommodityParam commodityParam){
        commodityService.edit(
                user.getUserId(),
                commodityId,
                commodityParam.getCommodityName(),
                commodityParam.getCategory(),
                commodityParam.getPrice(),
                commodityParam.getMessage()
        );
    }

    /**
     *删除物品（用户只能删除自己的物品）*/
    @DeleteMapping("{commodityId}")
    @PreAuthorize("hasAnyRole('USER')")
    public void delete(@AuthenticationPrincipal User user,@PathVariable Long commodityId){
        commodityService.delete(commodityId,user.getUserId());
    }

    /**
    *获取物品详细信息*/
    @GetMapping("{commodityId}")
    public CommodityVo search(@PathVariable Long commodityId){
        return commodityService.getCommodityDetails(commodityId);
    }

    /**
     *获取物品分页列表*/
    @GetMapping
    public PageResult<CommodityVo> getPageResult(@Valid SearchCommodityParam searchCommodityParam, @Valid PagingParam pagingParam){
        PageResult<CommodityVo> pageResult=commodityService.search(
                searchCommodityParam.getCategory(),
                searchCommodityParam.getUserId(),
                searchCommodityParam.getSold(),
                pagingParam.getCurrent(),
                pagingParam.getPageSize()
                );
        return pageResult;
    }

    /**
     *举报违规物品*/
    @PostMapping("/{commodityId}/report")
    @PreAuthorize("hasAnyRole('USER')")
    @Idempotent
    public void report(@AuthenticationPrincipal User user, @PathVariable Long commodityId, @RequestBody @Valid ReportCommodityParam reportCommodityParam){
        commodityService.report(user.getUserId(),commodityId,reportCommodityParam.getReason());
    }
}
