package edu.twt.rehuixiangshudong.service;

import edu.twt.rehuixiangshudong.zoo.dto.JournalGroupDTO;
import edu.twt.rehuixiangshudong.zoo.vo.JournalGroupVO;

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
}
