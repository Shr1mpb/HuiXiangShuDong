package edu.twt.rehuixiangshudong.zoo.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 日记图片统一返回结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PictureResult {
    private String pictureId;
    private String pictureUrl;
}
