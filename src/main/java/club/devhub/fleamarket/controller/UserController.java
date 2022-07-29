package club.devhub.fleamarket.controller;

import club.devhub.fleamarket.constant.ResultCodeEnum;
import club.devhub.fleamarket.constant.UserRoleEnum;
import club.devhub.fleamarket.entity.User;
import club.devhub.fleamarket.exception.BusinessException;
import club.devhub.fleamarket.param.LoginParam;
import club.devhub.fleamarket.param.ModifyUserParam;
import club.devhub.fleamarket.param.UserParam;
import club.devhub.fleamarket.service.UserService;
import club.devhub.fleamarket.utils.JwtUtil;
import club.devhub.fleamarket.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping("/api/fleamarket/v1/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public void register(@RequestBody @Valid UserParam userParam) {
        userService.register(userParam.getUsername(), userParam.getPassword());
    }

    @PostMapping("/login")
    public Object login(@RequestBody @Valid LoginParam loginParam, HttpServletResponse response) {
        User user = userService.login(loginParam.getUsername(), loginParam.getPassword());
        if (UserRoleEnum.ORDINARY_USER.getRole() != user.getRole()) {
            throw new BusinessException(ResultCodeEnum.WRONG_USERNAME_OR_PASSWORD, "非普通用户账户无法在此登录");
        }
        String token = jwtUtil.getTokenFromUser(user);
        response.setHeader("token", token);
        return new Object();
    }

    /**
     * @PreAuthorize("hasAnyRole('USER')") 检验是否具备USER权限
     * @AuthenticationPrincipal User user获取当前用户*/
    @GetMapping("/info")
    @PreAuthorize("hasAnyRole('USER')")
    public UserVO getOwnInfo(@AuthenticationPrincipal User user){
        return userService.getUserInfo(user.getUserId());
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('USER')")
    public UserVO getUserInfo(@PathVariable Long userId){
        return userService.getUserInfo(userId);
    }

    @PutMapping("info")
    @PreAuthorize("hasAnyRole('USER')")
    public void modifyInfo(@AuthenticationPrincipal User user, @RequestBody @Valid ModifyUserParam modifyUserParam){
        userService.modifyInfo(user.getUserId(),modifyUserParam.getSex(),modifyUserParam.getAddress());
    }
}
