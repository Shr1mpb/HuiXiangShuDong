package edu.twt.rehuixiangshudong.service;

import edu.twt.rehuixiangshudong.zoo.dto.JournalGroupDTO;

public interface JournalGroupService {
    /**
     * 创建日记串
     * @param journalGroupDTO 创建日记串传输数据模型
     */
    void createJournalGroup(JournalGroupDTO journalGroupDTO);
}
