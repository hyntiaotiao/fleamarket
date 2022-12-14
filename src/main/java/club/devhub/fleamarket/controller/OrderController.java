package club.devhub.fleamarket.controller;


import club.devhub.fleamarket.entity.User;
import club.devhub.fleamarket.param.AddressParam;
import club.devhub.fleamarket.param.PagingParam;
import club.devhub.fleamarket.param.SearchOrderParam;
import club.devhub.fleamarket.param.UserParam;
import club.devhub.fleamarket.service.OrderService;
import club.devhub.fleamarket.vo.OrderVO;
import club.devhub.fleamarket.vo.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping("/api/fleamarket/v1/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/buy/{commodityId}")
    @PreAuthorize("hasAnyRole('USER')")
    public void buy(@PathVariable Long commodityId , @AuthenticationPrincipal User user, @RequestBody AddressParam addressParam){
        orderService.buy(commodityId,user.getUserId(),addressParam.getAddress());
    }

    @PostMapping("sell/{orderId}")
    @PreAuthorize("hasAnyRole('USER')")
    public void sell(@PathVariable Long orderId , @AuthenticationPrincipal User user){
        orderService.sell(orderId,user.getUserId());
    }

    /**
     *获取订单分页列表
     * */
    @GetMapping
    @PreAuthorize("hasAnyRole('USER')")
    public PageResult<OrderVO> getPageResult(@Valid SearchOrderParam searchOrderParam,
                                             @Valid PagingParam pagingParam,
                                             @AuthenticationPrincipal User user){
        PageResult<OrderVO> pageResult=orderService.search(
                searchOrderParam.getCategory(),
                user.getUserId(),
                searchOrderParam.getState(),
                pagingParam.getCurrent(),
                pagingParam.getPageSize()
        );
        return pageResult;
    }
}
