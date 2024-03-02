package edu.twt.rehuixiangshudong.zoo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
/**
 * 获取用户全部信息时用
 * 返回生日时使用@JsonFormat返回中文数据
 * 返回性别时遵循 0未知 1男 2女
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "返回用户信息时的数据格式")
public class UserInfoVO implements Serializable {
    @ApiModelProperty("用户的uid")
    private Integer uid;
    @ApiModelProperty("用户的用户名")
    private String username;
    @ApiModelProperty("用户的昵称")
    private String nickname;
    @ApiModelProperty("用户的性别")
    private String gender;
    @ApiModelProperty("用户的生日")
    @JsonFormat(pattern="yyyy年MM月dd日",timezone = "GMT+8")
    private LocalDateTime birthDate;
    @ApiModelProperty("日记的数量")
    private Integer journalCount;
    @ApiModelProperty("日记串的数量")
    private Integer journalGroupCount;
    @ApiModelProperty("用户定位")
    private String location;
    @ApiModelProperty("用户背景图片URL")
    private String backgroundImage;
    @ApiModelProperty("用户头像URL")
    private String userProfilePicture;

}