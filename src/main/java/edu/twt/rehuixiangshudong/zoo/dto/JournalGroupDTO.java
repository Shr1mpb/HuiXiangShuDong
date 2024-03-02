package edu.twt.rehuixiangshudong.zoo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "传递日记串信息时的数据传输模型")
public class JournalGroupDTO implements Serializable {
    @ApiModelProperty("要添加或从中移除的日记的id")
    private Integer journalId;
    @ApiModelProperty("日记串的名字")
    private String journalGroupName;
    @ApiModelProperty("日记串的Id")
    private Integer journalGroupId;
    @ApiModelProperty("日记串是否被删除 0否 1是")
    private Integer isDeleted;
    @ApiModelProperty("日记串所属的用户")
    private Integer userIdAt;
}
