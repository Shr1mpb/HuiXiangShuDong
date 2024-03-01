package edu.twt.rehuixiangshudong.service;

import edu.twt.rehuixiangshudong.zoo.dto.ChangePasswordDTO;
import edu.twt.rehuixiangshudong.zoo.dto.UserRegisterAndLoginDTO;
import edu.twt.rehuixiangshudong.zoo.entity.User;

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
}
