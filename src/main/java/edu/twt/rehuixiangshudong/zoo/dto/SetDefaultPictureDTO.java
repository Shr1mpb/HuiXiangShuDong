package edu.twt.rehuixiangshudong.zoo.dto;

import lombok.Data;

/**
 * 传递默认图片时的接收参数模型
 */
@Data
public class SetDefaultPictureDTO {
    private int background;
    private int userProfilePicture;
}
