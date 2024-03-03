package edu.twt.rehuixiangshudong.service;

import edu.twt.rehuixiangshudong.zoo.dto.JournalDTO;
import edu.twt.rehuixiangshudong.zoo.dto.JournalPageQueryDTO;
import edu.twt.rehuixiangshudong.zoo.result.PageResult;
import edu.twt.rehuixiangshudong.zoo.vo.JournalVO;

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

    /**
     * 根据日记id获取日记信息
     * @param journalDTO 传输日记id和uid
     * @return journalVO对象
     */
    JournalVO getJournalByJID(JournalDTO journalDTO);

    /**
     * 在日记串中创建日记
     * @param journalDTO 传输日记的相关信息
     */
    void createJournalAtJournalGroup(JournalDTO journalDTO);

    /**
     * 分页查询日记信息
     * @param journalPageQueryDTO 查询名称 页码 每页记录数
     * @return 返回PageResult结果
     */
    PageResult getJournalsByUid(JournalPageQueryDTO journalPageQueryDTO);
}
