package edu.twt.rehuixiangshudong.mapper;

import edu.twt.rehuixiangshudong.zoo.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.sql.Timestamp;

@Mapper
public interface UserMapper {

//下面三个方法用于更新用户表的最后登录时间
    /**
     * 根据uid更新最后登录时间
     * @param uid   uid
     * @param time 更新后时间
     */
    @Update("update USERS set last_login_time = #{time} where uid = #{uid}")
    void updateLastLoginTimeByUid(int uid, Timestamp time);

    /**
     * 根据username更新最后登录时间
     * @param username 用户名
     * @param time 更新后时间
     */
    @Update("update USERS set last_login_time = #{time} where username = #{username}")
    void updateLastLoginTimeByUsername(String username, Timestamp time);

    /**
     * 根据username获取最后登录时间
     * @param username 用户名
     * @return 最后登录时间Timestamp
     */
    @Select("select last_login_time from USERS where username = #{username}")
    Timestamp getLastLoginTimeByUsername(String username);

    /**
     * 根据用户名查询User信息
     * @param username 用户名
     * @return User对象
     */
    @Select("select uid,username,password,nickname,gender,birth_date,journal_count," +
            "journal_group_count,location,background_image,search_history,user_profile_picture" +
            " from USERS where username = #{username}")
    User getByUsername(String username);
    /**
     * 注册功能 向数据库插入数据
     * @param username 用户名
     * @param password 密码
     */
    @Insert("insert into USERS (username,password) values (#{username},#{password})")
    void register(String username, String password);
}
