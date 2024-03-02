package edu.twt.rehuixiangshudong.service;

import edu.twt.rehuixiangshudong.zoo.dto.JournalDTO;

public interface JournalService {
    /**
     * 创建日记
     * @param journalDTO 传输日记的模版
     */
    void createJournal(JournalDTO journalDTO);

    /**
     * 修改日记信息 为空的内容不会更改
     * @param journalDTO 传输日记的信息
     */
    void modifyJournal(JournalDTO journalDTO);
}
