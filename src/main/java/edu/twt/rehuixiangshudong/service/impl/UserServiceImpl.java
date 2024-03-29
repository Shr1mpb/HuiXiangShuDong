package edu.twt.rehuixiangshudong.service.impl;

import edu.twt.rehuixiangshudong.mapper.UserMapper;
import edu.twt.rehuixiangshudong.service.UserService;
import edu.twt.rehuixiangshudong.zoo.constant.MessageConstant;
import edu.twt.rehuixiangshudong.zoo.dto.ChangePasswordDTO;
import edu.twt.rehuixiangshudong.zoo.dto.UserInfoDTO;
import edu.twt.rehuixiangshudong.zoo.dto.UserRegisterAndLoginDTO;
import edu.twt.rehuixiangshudong.zoo.entity.User;
import edu.twt.rehuixiangshudong.zoo.exception.*;
import edu.twt.rehuixiangshudong.zoo.util.AliOssUtil;
import edu.twt.rehuixiangshudong.zoo.vo.UserInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AliOssUtil aliOssUtil;

    /**
     * 注册服务
     * @param userRegisterAndLoginDTO 传输用户名和密码
     * @return 返回User数据
     */
    @Override
    public User register(UserRegisterAndLoginDTO userRegisterAndLoginDTO) {
        String username = userRegisterAndLoginDTO.getUsername();
        String password = userRegisterAndLoginDTO.getPassword();

        User userExist = userMapper.getByUsername(username);

        //如果用户名已存在，返回错误信息
        if (userExist != null) {//用户有东西 表示已存在
            throw new AccountAlreadyExistException(MessageConstant.ACCOUNT_EXISTS);
        }
        //如果用户名不存在，则进行注册
        //若注册失败（如用户名或密码过长）返回错误结果

        try {
            userMapper.register(username,password);
        } catch (Exception e) {
            throw new RegisterFailedException(MessageConstant.REGISTER_FAILED);
        }


        //注册成功，返回查询的结果
        //返回前更新用户的最后登录时间
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        userMapper.updateLastLoginTimeByUsername(username,timestamp);
        //返回查询结果
        return userMapper.getByUsername(username);
    }

    /**
     * 登录服务
     * @param userRegisterAndLoginDTO 传输用户名和密码
     * @return 返回User对象
     */
    @Override
    public User login(UserRegisterAndLoginDTO userRegisterAndLoginDTO) {
        String username = userRegisterAndLoginDTO.getUsername();
        String password = userRegisterAndLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        User user = userMapper.getByUsername(username);

        //2、处理异常情况（用户名不存在、密码不对）
        if (user == null) {
            //用户名不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOTFOUND);
        }

        //密码比对
        if (!password.equals(user.getPassword())) {
            //密码错误
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }


        //登录成功 返回实体对象
        //返回前更新用户最后的登录时间
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        userMapper.updateLastLoginTimeByUsername(username,timestamp);
        return user;
    }

    /**
     * 修改密码服务
     * @param changePasswordDTO 传输旧密码和新密码
     * @param uid 用户的uid(从ThreadLocal中获取token中的uid)
     */
    @Override
    public void changePassWord(ChangePasswordDTO changePasswordDTO,Integer uid) {
        String oldPassword = changePasswordDTO.getOldPassword();
        String newPassword = changePasswordDTO.getNewPassword();
        //先检查旧密码是否正确，如果不正确则返回错误结果
        User user = userMapper.getByUid(uid);
        //uid未查询到用户防止空指针异常
        //旧密码不匹配 返回错误结果
        if (user == null || !user.getPassword().equals(oldPassword)){
            throw new ChangePasswordFailedException(MessageConstant.CHANGE_PASSWORD_FAILED);
        }
        //旧密码正确，密码进行修改
        userMapper.changePassword(uid, oldPassword, newPassword);
        //修改密码后 更新用户最后登录时间
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        userMapper.updateLastLoginTimeByUid(uid,timestamp);

    }

    /**
     * 登出
     * @param uid 用户uid
     */
    @Override
    public void logout(Integer uid) {
        //进行登出操作，即更新用户的最后登录时间即可
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        userMapper.updateLastLoginTimeByUid(uid, timestamp);
    }

    /**
     * 设置和更改用户信息服务
     * @param changeUserInfoDTO 传输用户信息
     * @param uid 用户uid
     */
    @Override
    public void changeUserInfo(UserInfoDTO changeUserInfoDTO, Integer uid) {

        try {
            userMapper.changeUserInfo(changeUserInfoDTO, uid);
        } catch (Exception e) {
            throw new NicknameAlreadyExistException(MessageConstant.NICKNAME_EXIST);
        }
    }

    /**
     * 获取用户信息
     * @param uid 从token中获取的uid
     * @return 返回用户信息
     */
    @Override
    public UserInfoVO getUserInfo(Integer uid) {

        try {
            UserInfoVO userInfoVO = userMapper.getUserInfo(uid);
            if (userInfoVO == null) {//非空判断 防止空指针异常
                throw new GetUserInfoFailedException(MessageConstant.GET_USERINFO_FAILED);
            }
            //设置性别
            if (userInfoVO.getGender().equals("1")) {
                userInfoVO.setGender("男");
            } else if (userInfoVO.getGender().equals("2")) {
                userInfoVO.setGender("女");
            } else {
                userInfoVO.setGender("未知");
            }
            //设置记录天数

            return userInfoVO;
        } catch (Exception e) {
            throw new GetUserInfoFailedException(MessageConstant.GET_USERINFO_FAILED);
        }
    }


    /**
     * 上传并更改用户头像 上传成功后删除原有文件
     * @param filePath 上传成功后文件路径
     */
    @Override
    public void uploadUserProfilePicture(String filePath,Integer uid) {
        //先获取原有图片url 再获取原有图片名字
        UserInfoVO userInfoVO = userMapper.getUserInfo(uid);
        String userProfilePicture = userInfoVO.getUserProfilePicture();
        String oldName = null;
        if (userProfilePicture != null) {//用户原本有图片
            oldName = userProfilePicture.substring(userProfilePicture.lastIndexOf("/") + 1);
        }
        //数据库修改图片url
        userMapper.uploadUserProfilePicture(filePath, uid);
        //尝试删除旧url
        try {
            if (oldName != null) {
                aliOssUtil.delete(oldName);
                log.info("删除旧OSS文件成功！所属用户{}",uid);
            }
        } catch (Exception e) {
            log.error("删除OSS旧文件失败！{}",userProfilePicture);
        }


    }

    /**
     * 上传并更改背景图片 上传成功后删除原有文件
     * @param filePath 上传成功后文件路径
     * @param uid 用户的uid
     */
    @Override
    public void uploadUserBackgroundImage(String filePath, Integer uid) {
        //先获取原有图片url 再获取原有图片名字
        UserInfoVO userInfoVO = userMapper.getUserInfo(uid);
        String backgroundImage = userInfoVO.getBackgroundImage();
        String oldName = null;
        if (backgroundImage != null) {//如果原本有背景图片 获得oldName
            oldName = backgroundImage.substring(backgroundImage.lastIndexOf("/") + 1);
        }
        //数据库修改图片url
        userMapper.uploadUserBackgroundImage(filePath, uid);
        //尝试删除旧url
        try {
            if (oldName != null) {
                aliOssUtil.delete(oldName);
                log.info("删除旧OSS文件成功！所属用户{}",uid);
            }
        } catch (Exception e) {
            log.error("删除OSS旧文件失败！{}",backgroundImage);
        }


    }

    /**
     * 上传并更改日历图 上传成功后删除原有文件
     * @param filePath 文件路径
     * @param uid 用户uid
     */
    @Override
    public void uploadCalendarPicture(String filePath, Integer uid) {
        //先获取原有图片url 再获取原有图片名字
        String calendarPicture = userMapper.getCalendarPicture(uid);
        String oldName = null;
        if (calendarPicture != null) {//如果原本有背景图片 获得oldName
            oldName = calendarPicture.substring(calendarPicture.lastIndexOf("/") + 1);
        }
        //数据库修改图片url
        userMapper.uploadCalendarPicture(filePath, uid);
        //尝试删除旧url
        try {
            if (oldName != null) {
                aliOssUtil.delete(oldName);
                log.info("删除旧OSS文件成功！所属用户{}",uid);
            }
        } catch (Exception e) {
            log.error("删除OSS旧文件失败！{}",calendarPicture);
        }
    }
}
