package edu.twt.rehuixiangshudong.controller;

import edu.twt.rehuixiangshudong.service.UserService;
import edu.twt.rehuixiangshudong.zoo.constant.JwtClaimsConstant;
import edu.twt.rehuixiangshudong.zoo.constant.MessageConstant;
import edu.twt.rehuixiangshudong.zoo.dto.ChangePasswordDTO;
import edu.twt.rehuixiangshudong.zoo.vo.UserInfoVO;
import edu.twt.rehuixiangshudong.zoo.dto.UserInfoDTO;
import edu.twt.rehuixiangshudong.zoo.dto.UserRegisterAndLoginDTO;
import edu.twt.rehuixiangshudong.zoo.entity.User;
import edu.twt.rehuixiangshudong.zoo.properties.JwtProperties;
import edu.twt.rehuixiangshudong.zoo.result.Result;
import edu.twt.rehuixiangshudong.zoo.util.JwtUtil;
import edu.twt.rehuixiangshudong.zoo.util.ThreadLocalUtil;
import edu.twt.rehuixiangshudong.zoo.vo.UserRegisterAndLoginVO;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<UserRegisterAndLoginVO> register(@RequestBody UserRegisterAndLoginDTO userRegisterAndLoginDTO) throws InterruptedException {
        log.info("用户注册：{}", userRegisterAndLoginDTO);

        User user = userService.register(userRegisterAndLoginDTO);

        Thread.sleep(300);

        //生成token
        Map<String, Object> claims = new HashMap<>();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        claims.put(JwtClaimsConstant.USER_ID, user.getUid());
        claims.put(JwtClaimsConstant.USERNAME, user.getUsername());
        claims.put(JwtClaimsConstant.CREATE_AT, timestamp);
        String token = JwtUtil.createJWT(
                jwtProperties.getSecretKey(),
                jwtProperties.getTtl(),
                claims
        );

        //包装返回结果
        UserRegisterAndLoginVO userRegisterAndLoginVO = UserRegisterAndLoginVO.builder()
                .uid(user.getUid())
                .username(user.getUsername())
                .token(token)
                .nickname(user.getNickname())
                .build();

        return Result.success(userRegisterAndLoginVO);

    }

    /**
     * 用户登录
     */
    //TODO 注意移动传密码时直接md5加密
    //注册时 先确保用户的最后登录时间字段被更新 再签发jwt令牌
    @PostMapping("/login")
    public Result<UserRegisterAndLoginVO> login(@RequestBody UserRegisterAndLoginDTO userRegisterAndLoginDTO) throws InterruptedException {
        log.info("用户登录：{}", userRegisterAndLoginDTO);

        User user = userService.login(userRegisterAndLoginDTO);

        Thread.sleep(500);

        Map<String, Object> claims = new HashMap<>();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        claims.put(JwtClaimsConstant.USER_ID, user.getUid());
        claims.put(JwtClaimsConstant.USERNAME, user.getUsername());
        claims.put(JwtClaimsConstant.CREATE_AT, timestamp);
        String token = JwtUtil.createJWT(
                jwtProperties.getSecretKey(),
                jwtProperties.getTtl(),
                claims
        );
        UserRegisterAndLoginVO userRegisterAndLoginVO = UserRegisterAndLoginVO.builder()
                .uid(user.getUid())
                .username(user.getUsername())
                .token(token)
                .nickname(user.getNickname())
                .build();

        return Result.success(userRegisterAndLoginVO);
    }

    /**
     * 获取用户协议
     *
     * @return 返回JSON格式的数据 在Result的data中
     */
    @GetMapping("/userAgreement")
    public Result<Object> getUserAgreement() {
        //返回JSON格式的数据
        log.info("用户协议被获取");
        return Result.success(MessageConstant.USER_AGREEMENT);
    }

    /**
     * 更改密码
     *
     * @param changePasswordDTO 传输旧密码和新密码
     * @return 返回JSON格式的数据
     */
    @PutMapping("/changePassword")
    public Result<Object> changePassWord(@RequestBody ChangePasswordDTO changePasswordDTO) {
        Integer uid = ThreadLocalUtil.getCurrentUid();
        log.info("uid {} 修改密码 {}", uid, changePasswordDTO);

        userService.changePassWord(changePasswordDTO, uid);

        return Result.success(MessageConstant.CHANGE_PASSWORD_SUCCESS);
    }

    /**
     * 登出
     *
     * @return 返回成功信息 保存在data中
     */
    @GetMapping("/logout")
    public Result logout() {
        //获取token中的uid
        Integer uid = ThreadLocalUtil.getCurrentUid();
        log.info("uid为 {} 的用户登出...", uid);
        userService.logout(uid);

        return new Result(MessageConstant.LOGOUT_SUCCEED);
    }

    /**
     * 设置和更改用户信息
     *
     * @param changeUserInfoDTO 传输要更改的信息
     * @return 返回成功信息
     */
    @PutMapping("/changeUserInfo")
    public Result<Object> changeUserInfo(@RequestBody UserInfoDTO changeUserInfoDTO) {
        //获取token中的uid
        Integer uid = ThreadLocalUtil.getCurrentUid();
        log.info("uid为 {} 的用户修改用户信息 {}", uid, changeUserInfoDTO);

        userService.changeUserInfo(changeUserInfoDTO, uid);

        return Result.success(MessageConstant.CHANGE_USERINFO_SUCCESS);
    }

    /**
     * 获取用户信息
     *
     * @return 返回UserInfoVO结果
     */
    @GetMapping("/getUserInfo")
    public Result<UserInfoVO> getUserInfo() {
        //获取token中的uid
        Integer uid = ThreadLocalUtil.getCurrentUid();
        log.info("uid为 {} 的用户获取用户数据", uid);

        //使用uid获取数据

        UserInfoVO userInfoVO = userService.getUserInfo(uid);


        return Result.success(userInfoVO);

    }
}
