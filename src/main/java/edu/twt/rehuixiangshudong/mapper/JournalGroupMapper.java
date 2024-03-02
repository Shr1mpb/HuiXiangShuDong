package edu.twt.rehuixiangshudong.mapper;

import edu.twt.rehuixiangshudong.zoo.dto.JournalGroupDTO;
import edu.twt.rehuixiangshudong.zoo.vo.JournalGroupVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
}
