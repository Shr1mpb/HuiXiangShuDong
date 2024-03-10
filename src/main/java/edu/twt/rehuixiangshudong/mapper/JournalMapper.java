package edu.twt.rehuixiangshudong.mapper;

import com.github.pagehelper.Page;
import edu.twt.rehuixiangshudong.zoo.dto.JournalDTO;
import edu.twt.rehuixiangshudong.zoo.dto.JournalPageQueryDTO;
import edu.twt.rehuixiangshudong.zoo.result.PictureResult;
import edu.twt.rehuixiangshudong.zoo.vo.JournalVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

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

    /**
     * 分页查询日记
     * @param journalPageQueryDTO 分页信息：日记标题、页码、每页记录数
     * @return 返回Page类
     */
    Page<JournalVO> getJournalsByUid(JournalPageQueryDTO journalPageQueryDTO);

    /**
     * 查询日记图片
     * @param journalId 日记id
     * @return 返回日记图片结果的集合 包含了图片id和图片的url
     */
    @Select("select picture_id,picture_url from journal_pictures where journal_id_at = #{journalId}")
    List<PictureResult> getJournalPictures(int journalId);

    /**
     * 上传日记图片
     * @param journalId 日记id
     * @param filePath 图片URL
     */
    @Insert("insert into journal_pictures (journal_id_at, picture_url) VALUES (#{journalId},#{filePath})")
    void uploadJournalPicture(int journalId, String filePath);
}
