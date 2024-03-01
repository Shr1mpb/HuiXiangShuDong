package edu.twt.rehuixiangshudong.zoo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册和登录时的传输数据模型
 */
@Data
@ApiModel(description = "用户注册与登录传输数据模型")
public class UserRegisterAndLoginDTO implements Serializable {
    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("密码")
    private String password;
}
