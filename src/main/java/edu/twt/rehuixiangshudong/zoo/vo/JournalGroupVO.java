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
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "返回日记串信息的数据格式")
public class JournalGroupVO implements Serializable {
    @ApiModelProperty("日记串ID")
    private Integer journalGroupId;
    @ApiModelProperty("日记串名字")
    private String journalGroupName;
    @ApiModelProperty("日记串创建时间")
    @JsonFormat(pattern = "yyyy年MM月dd日 HH-mm-ss")
    private LocalDateTime createdAt;
    @ApiModelProperty("日记串修改时间")
    @JsonFormat(pattern = "yyyy年MM月dd日 HH-mm-ss")
    private LocalDateTime modifiedAt;
    @ApiModelProperty("日记串包含日记的数量")
    private Integer journalCount;
}
