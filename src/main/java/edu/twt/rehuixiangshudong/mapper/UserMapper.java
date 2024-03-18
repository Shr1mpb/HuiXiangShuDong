package edu.twt.rehuixiangshudong.mapper;

import edu.twt.rehuixiangshudong.zoo.dto.UserInfoDTO;
import edu.twt.rehuixiangshudong.zoo.entity.User;
import edu.twt.rehuixiangshudong.zoo.vo.UserInfoVO;
import org.apache.ibatis.annotations.*;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface UserMapper {

//下面三个方法用于更新用户表的最后登录时间

    /**
     * 根据uid更新最后登录时间
     *
     * @param uid  uid
     * @param time 更新后时间
     */
    @Update("update users set last_login_time = #{time} where uid = #{uid}")
    void updateLastLoginTimeByUid(int uid, Timestamp time);

    /**
     * 根据username更新最后登录时间
     *
     * @param username 用户名
     * @param time     更新后时间
     */
    @Update("update users set last_login_time = #{time} where username = #{username}")
    void updateLastLoginTimeByUsername(String username, Timestamp time);

    /**
     * 根据username获取最后登录时间
     *
     * @param username 用户名
     * @return 最后登录时间Timestamp
     */
    @Select("select last_login_time from users where username = #{username}")
    Timestamp getLastLoginTimeByUsername(String username);

    /**
     * 根据用户名查询User信息
     *
     * @param username 用户名
     * @return User对象
     */
    @Select("select uid,username,password,nickname,gender,birth_date,journal_count," +
            "journal_group_count,location,background_image,search_history,user_profile_picture" +
            " from users where username = #{username}")
    User getByUsername(String username);

    /**
     * 注册功能 向数据库插入数据
     *
     * @param username 用户名
     * @param password 密码
     */
    @Insert("insert into users (username,password) values (#{username},#{password})")
    void register(String username, String password);

    /**
     * 根据uid获取用户数据
     *
     * @param uid 用户uid
     * @return User对象
     */
    @Select("select uid,username,password,nickname,gender,birth_date,journal_count," +
            "journal_group_count,location,background_image,search_history,user_profile_picture" +
            " from users where uid = #{uid}")
    User getByUid(Integer uid);

    /**
     * 修改密码
     *
     * @param uid         用户uid
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    @Update("update users set password = #{newPassword} WHERE uid = #{uid} and password = #{oldPassword}")
    void changePassword(Integer uid, String oldPassword, String newPassword);

    /**
     * 修改用户信息
     *
     * @param userInfoDTO 传递用户信息
     * @param uid         传递uid
     *                    注意：mapper含有两个及以上的参数 需要使用@Param注解
     */
    void changeUserInfo(@Param("userInfo") UserInfoDTO userInfoDTO, Integer uid);

    /**
     * 获取用户信息
     *
     * @param uid ThreadLocalUtil类从token中获取的uid
     * @return 返回UserInfoVo类
     */
    @Select("select uid,username,nickname,gender,birth_date,write_days,journal_count,journal_group_count,text_count,location,background_image,user_profile_picture" +
            " from users where uid = #{uid}")
    UserInfoVO getUserInfo(Integer uid);

    /**
     * 上传并更改用户头像
     *
     * @param filePath 用户头像地址
     * @param uid      用户uid
     */
    @Update("update users set user_profile_picture = #{filePath} where uid = #{uid}")
    void uploadUserProfilePicture(String filePath, Integer uid);

    /**
     * 上传并更改用户背景图片
     *
     * @param filePath 文件路径
     * @param uid      用户uid
     */
    @Update("update users set background_image = #{filePath} where uid = #{uid}")
    void uploadUserBackgroundImage(String filePath, Integer uid);

    /**
     * 更新用户的日记数量
     */
    @Update("update users set journal_count = journal_count + #{count} where uid = #{userIdAt}")
    void updateJournalCount(int count, Integer userIdAt);

    /**
     * 更新用户的日记串数量
     */
    @Update("update users set journal_group_count = journal_group_count + #{count} where uid = #{userIdAt}")
    void updateJournalGroupCount(int count, Integer userIdAt);

    /**
     * 获取用户日记总字数(未删除)
     */
    @Select("select SUM(char_length(journal_text)) from journals where user_id_at = #{uid} and is_deleted = 0")
    Integer getUserTextCount(int uid);

    /**
     * 设置用户日记总字数
     */
    @Select("update users set text_count = #{textCount} where uid = #{uid}")
    void setUserTextCount(int textCount,int uid);

    /**
     * 获取用户记录天数(未删除)
     */
    @Select("select count(distinct DATE(created_at)) from journals where user_id_at = #{uid} and is_deleted = 0")
    int getUserWriteDays(int uid);

    /**
     * 设置用户记录天数
     */
    @Update("update users set write_days = #{writeDays} where uid = #{uid}")
    void setUserWriteDays(int writeDays,int uid);

    /**
     * 获取现有的全部uid
     */
    @Select("select distinct uid from users")
    List<Integer> getCurrentAllUids();


}
