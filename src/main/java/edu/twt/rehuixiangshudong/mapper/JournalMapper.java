package edu.twt.rehuixiangshudong.mapper;

import edu.twt.rehuixiangshudong.zoo.dto.JournalDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface JournalMapper {
    /**
     * 创建日记的接口
     * @param journalDTO 传输日记信息模型
     */
    @Insert("insert into journals (location,journal_title,journal_text,top_journal,journal_group_id_at,user_id_at)" +
            " values (#{location},#{journalTitle},#{journalText},#{topJournal},#{journalGroupIdAt},#{userIdAt})")
    void createJournal(JournalDTO journalDTO);

    /**
     * 修改日记信息
     * 为空的字样不会进行修改
     * @param journalDTO
     */
    void modifyJournal(JournalDTO journalDTO);
}
