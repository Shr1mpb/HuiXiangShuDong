package edu.twt.rehuixiangshudong.service;

import edu.twt.rehuixiangshudong.zoo.dto.JournalDTO;

public interface JournalService {
    /**
     * 创建日记
     * @param journalDTO 传输日记的模版
     */
    void createJournal(JournalDTO journalDTO);
}
