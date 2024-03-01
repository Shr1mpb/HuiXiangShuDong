package edu.twt.rehuixiangshudong.zoo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 修改密码传输数据模型
 */
@Data
@ApiModel(description = "修改密码时的传递数据模型")
public class ChangePasswordDTO implements Serializable {
    @ApiModelProperty("用户的旧密码")
    private String oldPassword;
    @ApiModelProperty ("用户的新密码")
    private String newPassword;
}
