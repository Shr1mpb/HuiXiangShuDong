package edu.twt.rehuixiangshudong.service;

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
}
