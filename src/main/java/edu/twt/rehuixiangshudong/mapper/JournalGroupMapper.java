package edu.twt.rehuixiangshudong.mapper;

import edu.twt.rehuixiangshudong.zoo.dto.JournalGroupDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface JournalGroupMapper {
    /**
     * 创建日记串
     * @param journalGroupDTO 创建日记串传输的数据 包含uid和日记串的名称
     */
    @Insert("insert into journal_groups (journal_group_name, user_id_at) values (#{journalGroupName},#{userIdAt})")
    void createJournalGroup(JournalGroupDTO journalGroupDTO);
}
