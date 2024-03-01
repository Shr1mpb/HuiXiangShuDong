package edu.twt.rehuixiangshudong.controller;

import edu.twt.rehuixiangshudong.service.UserService;
import edu.twt.rehuixiangshudong.zoo.constant.JwtClaimsConstant;
import edu.twt.rehuixiangshudong.zoo.dto.UserRegisterAndLoginDTO;
import edu.twt.rehuixiangshudong.zoo.entity.User;
import edu.twt.rehuixiangshudong.zoo.properties.JwtProperties;
import edu.twt.rehuixiangshudong.zoo.result.Result;
import edu.twt.rehuixiangshudong.zoo.util.JwtUtil;
import edu.twt.rehuixiangshudong.zoo.vo.UserRegisterAndLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        log.info("用户注册：{}",userRegisterAndLoginDTO);

        User user = userService.register(userRegisterAndLoginDTO);

        Thread.sleep(300);

        //生成token
        Map<String, Object> claims = new HashMap<>();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        claims.put(JwtClaimsConstant.USER_ID, user.getUid());
        claims.put(JwtClaimsConstant.USERNAME,user.getUsername());
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
        log.info("用户登录：{}",userRegisterAndLoginDTO);

        User user = userService.login(userRegisterAndLoginDTO);

        Thread.sleep(500);

        Map<String, Object> claims = new HashMap<>();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        claims.put(JwtClaimsConstant.USER_ID, user.getUid());
        claims.put(JwtClaimsConstant.USERNAME,user.getUsername());
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
}
