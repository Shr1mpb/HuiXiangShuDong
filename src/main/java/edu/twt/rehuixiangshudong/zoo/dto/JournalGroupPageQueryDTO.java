package edu.twt.rehuixiangshudong.zoo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "分页查询日记串数据传输模型")
public class JournalGroupPageQueryDTO {
    @ApiModelProperty("页码")
    private int page;
    @ApiModelProperty("页码包含结果数")
    private int pageSize;
    @ApiModelProperty("日记串名字(模糊匹配)")
    private String journalGroupName;
    @ApiModelProperty("日记串所属用户uid")
    private Integer userIdAt;

}
