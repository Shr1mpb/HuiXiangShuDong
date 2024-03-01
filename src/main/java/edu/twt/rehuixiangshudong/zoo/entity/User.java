package edu.twt.rehuixiangshudong.zoo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户类
 * 数据库中的字段即为此处字段驼峰转下划线
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer uid;//用户的uid
    private String username;//用户的用户名
    private String nickname;//用户昵称
    private String password;//用户的密码
    private int gender;//用户的性别 0未知 1男 2女
    private String birthDate;//用户的生日 可空
    private int journalCount;//用户拥有的日记数量
    private int journalGroupCount;//用户拥有的日记串数量
    private String location;//用户的地址 可空
    private String backgroundImage;//用户界面的背景图片 可空
    private String searchHistory;//用户搜索的历史记录备份 可空
    private LocalDateTime lastLoginTime;//用户最后登录的时间
    private String userProfilePicture;//用户的头像 可空

}
