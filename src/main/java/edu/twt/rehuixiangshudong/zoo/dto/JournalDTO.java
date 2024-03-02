package edu.twt.rehuixiangshudong.zoo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "创建和修改日记时的数据传输模型")
public class JournalDTO implements Serializable {
    @ApiModelProperty("日记的id")
    private Integer journalId;
    @ApiModelProperty("日记创建的地点")
    private String location;
    @ApiModelProperty("日记标题")
    private String journalTitle;
    @ApiModelProperty("日记内容")
    private String journalText;
    @ApiModelProperty("日记是否被置顶")
    private Integer topJournal;
    @ApiModelProperty("日记所属的日记串")
    private Integer journalGroupIdAt;
    @ApiModelProperty("日记是否被删除")
    private Integer isDeleted;
    @ApiModelProperty("日记所属用户的uid")
    private Integer userIdAt;
}
