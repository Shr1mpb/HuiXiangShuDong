package edu.twt.rehuixiangshudong.zoo.dto;

import io.swagger.annotations.Api;
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
    @ApiModelProperty("0代表查询具体到日 1代表查询具体到月 2代表查询具体到年")
    private int range;
    @ApiModelProperty("日记创建日期 格式yyyy-MM-dd")
    private String date;
    @ApiModelProperty("后续转换用到的年份 查询具体到月的日期时会使用")
    private String year;


}
