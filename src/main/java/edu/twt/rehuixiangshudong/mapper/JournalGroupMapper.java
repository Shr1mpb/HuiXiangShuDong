package edu.twt.rehuixiangshudong.mapper;

import com.github.pagehelper.Page;
import edu.twt.rehuixiangshudong.zoo.dto.JournalGroupDTO;
import edu.twt.rehuixiangshudong.zoo.dto.JournalGroupPageQueryDTO;
import edu.twt.rehuixiangshudong.zoo.vo.JournalGroupVO;
import edu.twt.rehuixiangshudong.zoo.vo.JournalVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface JournalGroupMapper {
    /**
     * 创建日记串
     * @param journalGroupDTO 创建日记串传输的数据 包含uid和日记串的名称
     */
    @Insert("insert into journal_groups (journal_group_name, user_id_at) values (#{journalGroupName},#{userIdAt})")
    void createJournalGroup(JournalGroupDTO journalGroupDTO);

    /**
     * 获取指定ID的日记串
     * @param uid 用户uid
     * @param journalGroupId 日记串id
     * @return
     */
    @Select("SELECT journal_group_id,journal_group_name,created_at,modified_at,journal_count " +
            "FROM journal_groups " +
            "where user_id_at = #{uid} and is_deleted = 0 and journal_group_id = #{journalGroupId}")
    JournalGroupVO getJournalGroup(Integer uid, Integer journalGroupId);

    /**
     * 获取指定id日记串中的所有日记 根据创建时间降序排序
     * @param uid 用户uid
     * @param journalGroupId 指定的日记串id
     * @return 返回list结果
     */

    List<JournalVO> getJournalsInJournalGroup(@Param("uid") Integer uid, @Param("journalGroupId") Integer journalGroupId,@Param("asc") int asc);

    /**
     * 修改日记串名字
     * @param journalGroupName 修改后日记串名字
     * @param uid 用户uid
     * @param journalGroupId 日记串id
     */
    @Update("UPDATE journal_groups set journal_group_name = #{journalGroupName} where journal_group_id = #{journalGroupId} and " +
            "user_id_at = #{uid} and is_deleted = 0")
    void changeJournalGroupName(String journalGroupName, Integer uid, Integer journalGroupId);

    /**
     * 删除日记串
     * @param uid 用户uid
     * @param journalGroupId 要删除的日记串id
     */
    @Update("UPDATE journal_groups set is_deleted = 1 where user_id_at = #{uid} and journal_group_id = #{journalGroupId}")
    void deleteJournalGroup(Integer uid, Integer journalGroupId);

    /**
     * 添加指定日记到指定日记串
     * @param uid 用户uid
     * @param journalId 日记id
     * @param journalGroupId 要添加进的日记串id
     */
    @Update("UPDATE journals set journal_group_id_at = #{journalGroupId} where user_id_at = #{uid} and journal_id = #{journalId}")
    void addJournalToJournalGroup(int uid, int journalId, int journalGroupId);

    /**
     * 从日记串中移除日记 (设置journal_group_id_at = 0)
     * @param uid 用户uid
     * @param journalId 日记id
     */
    @Update("UPDATE journals set journal_group_id_at = 0 where user_id_at = #{uid} and journal_id = #{journalId}")
    void deleteJournalFromJournalGroup(int uid, int journalId);

    /**
     * 分页查询日记串
     * @param journalGroupPageQueryDTO 页码、单页包含数、标题
     * @return PageHelper要求的Page对象
     */
    Page<JournalGroupVO> getJournalGroups(JournalGroupPageQueryDTO journalGroupPageQueryDTO);

    /**
     * 更新日记串包含的日记数量
     */
    @Update("update journal_groups set journal_count = journal_count + #{count} where user_id_at = #{uid} and journal_group_id = #{journalGroupIdAt}")
    void updateJournalCountOfJournalGroup(int count,Integer uid,Integer journalGroupIdAt);
}
