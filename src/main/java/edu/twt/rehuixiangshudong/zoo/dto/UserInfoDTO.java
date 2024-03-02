package edu.twt.rehuixiangshudong.zoo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.util.Date;

/**
 * 设置与修改用户信息的数据传输模型
 * 传输生日时使用@DateTimeFormat保证传输正常
 */
@Data
@ApiModel(description = "设置与更改用户信息数据传输模型")
public class UserInfoDTO implements Serializable {
    @ApiModelProperty("用户昵称")
    private String nickname;
    @ApiModelProperty("用户性别 0未知 1男 2女")
    private int gender;
    @ApiModelProperty("用户生日")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;
    @ApiModelProperty("用户地址")
    private String location;
    @ApiModelProperty("用户界面的背景图片")
    private String backgroundImage;
    @ApiModelProperty("用户头像")
    private String userProfilePicture;
    @ApiModelProperty("用户搜索记录")
    private String searchHistory;

}
