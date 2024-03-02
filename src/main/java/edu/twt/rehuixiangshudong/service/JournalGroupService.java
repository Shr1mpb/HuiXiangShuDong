package edu.twt.rehuixiangshudong.service;

import edu.twt.rehuixiangshudong.zoo.dto.JournalGroupDTO;
import edu.twt.rehuixiangshudong.zoo.vo.JournalGroupVO;
import edu.twt.rehuixiangshudong.zoo.vo.JournalVO;

import java.util.List;

public interface JournalGroupService {
    /**
     * 创建日记串
     * @param journalGroupDTO 创建日记串传输数据模型
     */
    void createJournalGroup(JournalGroupDTO journalGroupDTO);

    /**
     * 获取指定id的日记串
     * @param uid 用户的uid
     * @param journalGroupId 指定日记串id
     * @return
     */
    JournalGroupVO getJournalGroup(Integer uid, Integer journalGroupId);

    /**
     * 获取日记串中的全部日记
     * @param uid 用户的uid
     * @param journalGroupId 传输日记串id
     * @return List集合
     */
    List<JournalVO> getJournalsInJournalGroup(Integer uid, Integer journalGroupId);

    /**
     * 修改日记串名字
     * @param journalGroupName 修改后日记串名字
     * @param uid 用户uid
     * @param journalGroupId 日记串id
     */
    void changeJournalGroupName(String journalGroupName, Integer uid, Integer journalGroupId);

    /**
     * 删除指定id的日记串
     * @param uid 用户uid
     * @param journalGroupId 要删除的日记串id
     */
    void deleteJournalGroup(Integer uid, Integer journalGroupId);
}
