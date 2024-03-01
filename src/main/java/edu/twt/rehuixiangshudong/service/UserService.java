package edu.twt.rehuixiangshudong.service;

import edu.twt.rehuixiangshudong.zoo.dto.UserRegisterAndLoginDTO;
import edu.twt.rehuixiangshudong.zoo.entity.User;

public interface UserService {
    /**
     *
     * @param userRegisterAndLoginDTO 传输用户名和密码
     * @return 返回User结果
     */
    User register(UserRegisterAndLoginDTO userRegisterAndLoginDTO);
}
