package edu.twt.rehuixiangshudong.mapper;

import edu.twt.rehuixiangshudong.zoo.dto.JournalDTO;
import edu.twt.rehuixiangshudong.zoo.vo.JournalVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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

    /**
     * 根据jurnalId查询日记
     * @param journalDTO 传输journalId
     */
    @Select("select journal_id,journal_title,created_at,modified_at,location,journal_id, journal_title, user_id_at, created_at, modified_at, location,shared_count ,is_deleted, journal_group_id_at,journal_text,top_journal " +
            "from journals " +
            "where user_id_at = #{userIdAt} and journal_id = #{journalId} and is_deleted = 0")
    JournalVO getJournalByJID(JournalDTO journalDTO);
    @Select("select journal_id,journal_title,created_at,modified_at,location,journal_id, journal_title, user_id_at, created_at, modified_at, location,shared_count ,is_deleted, journal_group_id_at,journal_text,top_journal " +
            "from journals " +
            "where user_id_at = #{userIdAt} and journal_id = #{journalId} and is_deleted = 0")
    JournalVO getJournalByJID2(int userIdAt , int journalId);

    /**
     * 查询指定日记是否包含于某日记串
     * @param journalId 日记id
     * @param journalGroupId 日记串id
     * @return
     */
    @Select("select journal_id from journals where journal_id = #{journalId} and journal_group_id_at = #{journalGroupId}")
    JournalVO checkJournalInJournalGroup(int journalId, int journalGroupId);
}
