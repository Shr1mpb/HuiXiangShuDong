package edu.twt.rehuixiangshudong.controller;

import edu.twt.rehuixiangshudong.mapper.UserMapper;
import edu.twt.rehuixiangshudong.service.UserService;
import edu.twt.rehuixiangshudong.zoo.constant.JwtClaimsConstant;
import edu.twt.rehuixiangshudong.zoo.constant.MessageConstant;
import edu.twt.rehuixiangshudong.zoo.dto.ChangePasswordDTO;
import edu.twt.rehuixiangshudong.zoo.dto.SetDefaultPictureDTO;
import edu.twt.rehuixiangshudong.zoo.properties.StaticProperties;
import edu.twt.rehuixiangshudong.zoo.util.AliOssUtil;
import edu.twt.rehuixiangshudong.zoo.vo.UserInfoVO;
import edu.twt.rehuixiangshudong.zoo.dto.UserInfoDTO;
import edu.twt.rehuixiangshudong.zoo.dto.UserRegisterAndLoginDTO;
import edu.twt.rehuixiangshudong.zoo.entity.User;
import edu.twt.rehuixiangshudong.zoo.properties.JwtProperties;
import edu.twt.rehuixiangshudong.zoo.result.Result;
import edu.twt.rehuixiangshudong.zoo.util.JwtUtil;
import edu.twt.rehuixiangshudong.zoo.util.ThreadLocalUtil;
import edu.twt.rehuixiangshudong.zoo.vo.UserRegisterAndLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private AliOssUtil aliOssUtil;
    @Autowired
    private StaticProperties staticProperties;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 根目录控制器
     * 浏览器直接访问url默认是get请求
     */
    @GetMapping("/")
    public String redirect() {
        return "这里是回响树洞项目后端服务器,请确保你使用正确的接口进行访问";
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<UserRegisterAndLoginVO> register(@RequestBody UserRegisterAndLoginDTO userRegisterAndLoginDTO) throws InterruptedException {
        if (userRegisterAndLoginDTO == null) {
            return Result.fail(MessageConstant.REGISTER_ERROR);
        }
        log.info("用户注册：{}", userRegisterAndLoginDTO);

        User user = userService.register(userRegisterAndLoginDTO);

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

        //生成的token存入redis中
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.set(token,token,jwtProperties.getTtl(), TimeUnit.MILLISECONDS);
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
        //生成的token存入redis中
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.set(token,token,jwtProperties.getTtl(), TimeUnit.MILLISECONDS);
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
    public Result<Object> changePassWord(@RequestBody ChangePasswordDTO changePasswordDTO,@RequestHeader("token")String token) {
        if (changePasswordDTO == null) {
            return Result.fail(MessageConstant.COMMON_ERROR);
        }
        Integer uid = ThreadLocalUtil.getCurrentUid();
        log.info("uid {} 修改密码 {}", uid, changePasswordDTO);

        userService.changePassWord(changePasswordDTO, uid);

        //修改密码成功后 删除redis中存储的令牌
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.getOperations().delete(token);
        return Result.success(MessageConstant.CHANGE_PASSWORD_SUCCESS);
    }

    /**
     * 登出
     *
     * @return 返回成功信息 保存在data中
     */
    @GetMapping("/logout")
    public Result logout(@RequestHeader("token")String token) {
        //获取token中的uid
        Integer uid = ThreadLocalUtil.getCurrentUid();
        log.info("uid为 {} 的用户登出...", uid);
        userService.logout(uid);

        //登出成功后 删除redis中存储的令牌
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.getOperations().delete(token);

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
        if (changeUserInfoDTO == null) {
            return Result.fail(MessageConstant.COMMON_ERROR);
        }
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

    /**
     * 上传并修改用户头像
     *
     * @return 返回成功与否 信息结果
     */
    @PostMapping("/uploadUserProfilePicture")
    public Result<Object> uploadUserProfilePicture(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return Result.fail(MessageConstant.COMMON_ERROR);
        }
        Integer uid = ThreadLocalUtil.getCurrentUid();

        log.info("uid为 {} 的用户 上传并修改头像", uid);
        //获取原始文件名 并获取文件拓展名 并且用uuid拼接
        String originalFilename = file.getOriginalFilename();
        String extension = Objects.requireNonNull(originalFilename).substring(originalFilename.lastIndexOf("."));
        String objectName = UUID.randomUUID() + extension;
        //若扩展名不是 jpg 或 png
        if (!(extension.equals(".jpg") || extension.equals(".png"))) {
            return Result.fail(MessageConstant.UPLOAD_FILE_UNMATCHED);
        }
        //上传文件
        try {
            String filePath = aliOssUtil.upload(file.getBytes(), objectName);

            //上传成功
            userService.uploadUserProfilePicture(filePath, uid);
            return Result.success(MessageConstant.CHANGE_PROFILE_PICTURE_SUCCESS);
        } catch (Exception e) {
            //上传失败
            return Result.fail(MessageConstant.UPLOAD_FAILED);
        }
    }

    /**
     * 上传并修改用户背景图片
     *
     * @param file 上传的文件
     * @return 返回文字结果
     */
    @PostMapping("/uploadUserBackgroundImage")
    public Result<Object> uploadUserBackgroundImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return Result.fail(MessageConstant.COMMON_ERROR);
        }
        Integer uid = ThreadLocalUtil.getCurrentUid();

        log.info("uid为 {} 的用户 上传并修改背景图片", uid);
        //获取原始文件名 并获取文件拓展名 并且用uuid拼接
        String originalFilename = file.getOriginalFilename();
        String extension = Objects.requireNonNull(originalFilename).substring(originalFilename.lastIndexOf("."));
        String objectName = UUID.randomUUID() + extension;
        //若扩展名不是 jpg 或 png
        if (!(extension.equals(".jpg") || extension.equals(".png"))) {
            return Result.fail(MessageConstant.UPLOAD_FILE_UNMATCHED);
        }
        //上传文件
        try {
            String filePath = aliOssUtil.upload(file.getBytes(), objectName);

            //上传成功
            userService.uploadUserBackgroundImage(filePath, uid);
            return Result.success(MessageConstant.CHANGE_BACKGROUND_SUCCESS);
        } catch (Exception e) {
            //上传失败
            return Result.fail(MessageConstant.UPLOAD_FAILED);
        }
    }
    /**
     * 根据数字1-8设置用户的 头像/背景图片 为默认的八张图片
     */
    @PutMapping("/setDefaultPictureByNum")
    public Result<String> setDefaultPictureByNum(@RequestBody SetDefaultPictureDTO setDefaultPictureDTO){
        int background = setDefaultPictureDTO.getBackground();
        int userProfilePicture = setDefaultPictureDTO.getUserProfilePicture();
        if (background < 0 || background > 8 || userProfilePicture < 0 || userProfilePicture > 8) {
            return Result.fail(MessageConstant.SET_PICTURE_FAILED);
        }
        Integer uid = ThreadLocalUtil.getCurrentUid();

        if (background != 0) {
            String backgroundUrl = staticProperties.getUrl() + background + background + ".jpg";
            userMapper.uploadUserBackgroundImage(backgroundUrl,uid);
        }
        if (userProfilePicture != 0) {
            String userProfilePictureUrl = staticProperties.getUrl() + userProfilePicture + ".jpg";
            userMapper.uploadUserProfilePicture(userProfilePictureUrl, uid);
        }

        return Result.success(MessageConstant.SET_SUCCESS);
    }
}
