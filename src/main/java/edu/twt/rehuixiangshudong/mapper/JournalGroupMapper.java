package edu.twt.rehuixiangshudong.mapper;

import edu.twt.rehuixiangshudong.zoo.dto.JournalGroupDTO;
import edu.twt.rehuixiangshudong.zoo.vo.JournalGroupVO;
import edu.twt.rehuixiangshudong.zoo.vo.JournalVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
    @Select("SELECT journal_id,journal_title,created_at,modified_at,location,shared_count,journal_group_id_at,journal_text " +
            "FROM journals " +
            "where journal_group_id_at = #{journalGroupId} and user_id_at = #{uid} " +
            "ORDER BY created_at DESC")
    List<JournalVO> getJournalsInJournalGroup(Integer uid, Integer journalGroupId);
}
