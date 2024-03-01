package edu.twt.rehuixiangshudong.zoo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 日记串类
 * 数据库中的字段即为此处字段驼峰转下划线
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JournalGroup {
    private int journalGroupId;//日记串id
    private String journalGroupName;//日记串名称 可空
    private LocalDateTime createAt;//日记串创建时间
    private LocalDateTime modifiedAt;//日记串修改时间
    private int isDeleted;//日记串是否被删除 0未删除 1已删除
    private int journalCount;//日记串含有的日记数量
    private int userIdAt;//日记串所属的用户

}
