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

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "获取日记信息时的返回数据格式")
public class JournalVO implements Serializable {
    @ApiModelProperty("日记的id")
    private int journalId;
    @ApiModelProperty("日记的标题")
    private String journalTitle;
    @ApiModelProperty("日记的创建时间")
    @JsonFormat(pattern = "yyyy年MM月dd日 HH-mm-ss")
    private LocalDateTime createdAt;
    @ApiModelProperty("日记的修改时间")
    @JsonFormat(pattern = "yyyy年MM月dd日 HH-mm-ss")
    private LocalDateTime modifiedAt;
    @ApiModelProperty("日记的创建地点")
    private String location;
    @ApiModelProperty("日记被分享的次数")
    private int sharedCount;
    @ApiModelProperty("日记所属的日记串Id")
    private int journalGroupIdAt;
    @ApiModelProperty("日记的内容")
    private String journalText;
    @ApiModelProperty("用户是否置顶日记 0否 1是")
    private int topJournal;
}