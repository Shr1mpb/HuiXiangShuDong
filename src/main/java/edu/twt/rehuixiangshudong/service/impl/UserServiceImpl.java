package edu.twt.rehuixiangshudong.service.impl;

import edu.twt.rehuixiangshudong.mapper.UserMapper;
import edu.twt.rehuixiangshudong.service.UserService;
import edu.twt.rehuixiangshudong.zoo.constant.MessageConstant;
import edu.twt.rehuixiangshudong.zoo.dto.UserRegisterAndLoginDTO;
import edu.twt.rehuixiangshudong.zoo.entity.User;
import edu.twt.rehuixiangshudong.zoo.exception.AccountAlreadyExistException;
import edu.twt.rehuixiangshudong.zoo.exception.AccountNotFoundException;
import edu.twt.rehuixiangshudong.zoo.exception.LoginFailedException;
import edu.twt.rehuixiangshudong.zoo.exception.RegisterFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

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
}
