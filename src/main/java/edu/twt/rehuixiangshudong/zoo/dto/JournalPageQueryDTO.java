package edu.twt.rehuixiangshudong.zoo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 分页查询日记的数据传输模型
 */
@Data
@ApiModel(description = "分页查询日记的数据传输模型")
public class JournalPageQueryDTO implements Serializable {
    @ApiModelProperty("页码")
    private int page;
    @ApiModelProperty("每页记录数")
    private int pageSize;
    @ApiModelProperty("日记标题")
    private String journalTitle;
    @ApiModelProperty("用户uid")
    private Integer userIdAt;

}
