package club.devhub.fleamarket.controller;


import club.devhub.fleamarket.constant.ResultCodeEnum;
import club.devhub.fleamarket.constant.UserRoleEnum;
import club.devhub.fleamarket.entity.User;
import club.devhub.fleamarket.exception.BusinessException;
import club.devhub.fleamarket.param.LoginParam;
import club.devhub.fleamarket.param.PagingParam;
import club.devhub.fleamarket.service.ReportsService;
import club.devhub.fleamarket.service.UserService;
import club.devhub.fleamarket.utils.JwtUtil;
import club.devhub.fleamarket.vo.PageResult;
import club.devhub.fleamarket.vo.ReportVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 管理员相关的Controller
 */
@RestController
@RequestMapping("/api/fleamarket/v1/admins")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private ReportsService reportsService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public Object login(@RequestBody @Valid LoginParam loginParam, HttpServletResponse response) {
        User user = userService.login(loginParam.getUsername(), loginParam.getPassword());
        if (UserRoleEnum.ADMINISTRATOR.getRole() != user.getRole()) {
            throw new BusinessException(ResultCodeEnum.WRONG_USERNAME_OR_PASSWORD, "非管理员账户无法在此登录");
        }
        String token = jwtUtil.getTokenFromUser(user);
        response.setHeader("token", token);
        return new Object();
    }

    @GetMapping("/reports")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public PageResult<ReportVo> getReportList(@Valid PagingParam pagingParam) {
        return reportsService.getList(pagingParam.getCurrent(), pagingParam.getPageSize());
    }


    @PutMapping("/{reportId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public void review(@PathVariable Long reportId) {
        reportsService.review(reportId);
    }
    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public void logOff(Long userId) {
        reportsService.logOff(userId);
    }
}
