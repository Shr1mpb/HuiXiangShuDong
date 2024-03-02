package edu.twt.rehuixiangshudong.service;

import edu.twt.rehuixiangshudong.zoo.dto.ChangePasswordDTO;
import edu.twt.rehuixiangshudong.zoo.dto.UserInfoDTO;
import edu.twt.rehuixiangshudong.zoo.dto.UserRegisterAndLoginDTO;
import edu.twt.rehuixiangshudong.zoo.entity.User;
import edu.twt.rehuixiangshudong.zoo.vo.UserInfoVO;

public interface UserService {
    /**
     * 用户注册
     * @param userRegisterAndLoginDTO 传输用户名和密码
     * @return 返回User结果
     */
    User register(UserRegisterAndLoginDTO userRegisterAndLoginDTO);

    /**
     * 用户登录
     * @param userRegisterAndLoginDTO 传输用户名和密码
     * @return 返回User结果
     */
    User login(UserRegisterAndLoginDTO userRegisterAndLoginDTO);

    /**
     * 修改密码
     * @param changePasswordDTO 传输用户名和密码
     * @param uid 传输uid
     */
    void changePassWord(ChangePasswordDTO changePasswordDTO, Integer uid);

    /**
     * 登出功能
     * @param uid 用户uid
     */
    void logout(Integer uid);

    /**
     * 设置和更改用户信息
     * @param changeUserInfoDTO 传输用户信息
     * @param uid 用户uid
     */
    void changeUserInfo(UserInfoDTO changeUserInfoDTO, Integer uid);

    /**
     * 获取用户信息
     * @param uid 从token中获取的uid
     * @return 返回对象结果
     */
    UserInfoVO getUserInfo(Integer uid);
}
