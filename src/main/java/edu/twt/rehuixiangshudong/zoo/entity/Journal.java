package edu.twt.rehuixiangshudong.zoo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 日记类
 * 数据库中的字段即为此处字段驼峰转下划线
 * 使用MybatisPlus时 注意isDeleted字段
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Journal {
    private int journalId;//日记的id
    private String journalTitle;//日记的标题 可空
    private int userIdAt;//日记所属的用户
    private LocalDateTime createAt;//日记的创建时间
    private LocalDateTime modifiedAt;//日记的修改时间
    private String location;//日记的创建地点 可空
    private int sharedCount;//日记被分享的次数
    private int isDeleted;//日记是否被删除 0未删除 1已删除
    private int journalGroupIdAt;//日记所属的日记串 可空
    private String journalText;//日记的内容 可空
    private int topJournal;//用户是否置顶日记 0否 1是

}