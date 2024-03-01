package edu.twt.rehuixiangshudong.zoo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户登录和注册时的返回数据模型
 * 用于返回数据
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "用户注册和登录返回的数据格式")
public class UserRegisterAndLoginVO {
    @ApiModelProperty("用户uid")
    private int uid;
    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("JWT令牌")
    private String token;
    @ApiModelProperty("用户昵称")
    private String nickname;
}
