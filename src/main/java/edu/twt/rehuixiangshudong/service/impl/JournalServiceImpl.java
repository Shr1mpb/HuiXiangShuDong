package edu.twt.rehuixiangshudong.service.impl;

import edu.twt.rehuixiangshudong.mapper.JournalMapper;
import edu.twt.rehuixiangshudong.service.JournalService;
import edu.twt.rehuixiangshudong.zoo.constant.MessageConstant;
import edu.twt.rehuixiangshudong.zoo.dto.JournalDTO;
import edu.twt.rehuixiangshudong.zoo.exception.CreateJournalFailedException;
import edu.twt.rehuixiangshudong.zoo.exception.ModifyJournalFailedException;
import edu.twt.rehuixiangshudong.zoo.exception.SetTopJournalFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JournalServiceImpl implements JournalService {
    @Autowired
    private JournalMapper journalMapper;

    /**
     * 创建日记功能
     * 创建日记时 若journalGroupIdAt没有填写 则默认写0
     * @param journalDTO 传输日记的模版
     * journalGroupIdAt 数据库中不能为空
     *
     */
    @Override
    public void createJournal(JournalDTO journalDTO) {

        try {
            if (journalDTO.getJournalGroupIdAt() == null) {
                journalDTO.setJournalGroupIdAt(0);
            }
            journalMapper.createJournal(journalDTO);
        } catch (Exception e) {
            throw new CreateJournalFailedException(MessageConstant.CREATE_JOURNAL_FAILED);
        }
    }

    /**
     * 修改日记信息
     * @param journalDTO 传输日记的信息
     */
    @Override
    public void modifyJournal(JournalDTO journalDTO) {
        //调用Mapper 修改日记信息 修改失败返回错误信息
        //注意 如果用户传输的日记的JournalId与自己的uid不对应 则也会返回成功的字样

        //限制topJournal传输0/1
        if (journalDTO != null && journalDTO.getTopJournal() != null) {
            int topJournal = journalDTO.getTopJournal();
            if(topJournal != 0 && topJournal!= 1 ){
                throw new SetTopJournalFailedException(MessageConstant.SET_TOPJOURNAL_FAILED);
            }
        }
        //限制isDeleted传输0/1
        if (journalDTO != null && journalDTO.getIsDeleted() != null) {
            int isDeleted = journalDTO.getIsDeleted();
            //限制topJournal传输0或1
            if(isDeleted != 0 && isDeleted  != 1 ){
                throw new SetTopJournalFailedException(MessageConstant.SET_TOPJOURNAL_FAILED);
            }
        }

        try {
            journalMapper.modifyJournal(journalDTO);
        } catch (Exception e) {
            throw new ModifyJournalFailedException(MessageConstant.MODIFY_JOURNAL_FAILED);
        }
    }
}