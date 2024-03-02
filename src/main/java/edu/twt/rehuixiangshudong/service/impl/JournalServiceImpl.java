package edu.twt.rehuixiangshudong.service.impl;

import edu.twt.rehuixiangshudong.mapper.JournalGroupMapper;
import edu.twt.rehuixiangshudong.mapper.JournalMapper;
import edu.twt.rehuixiangshudong.service.JournalService;
import edu.twt.rehuixiangshudong.zoo.constant.MessageConstant;
import edu.twt.rehuixiangshudong.zoo.dto.JournalDTO;
import edu.twt.rehuixiangshudong.zoo.exception.CreateJournalFailedException;
import edu.twt.rehuixiangshudong.zoo.exception.GetJournalsFailedException;
import edu.twt.rehuixiangshudong.zoo.exception.ModifyJournalFailedException;
import edu.twt.rehuixiangshudong.zoo.exception.SetTopJournalFailedException;
import edu.twt.rehuixiangshudong.zoo.vo.JournalGroupVO;
import edu.twt.rehuixiangshudong.zoo.vo.JournalVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JournalServiceImpl implements JournalService {
    @Autowired
    private JournalMapper journalMapper;
    @Autowired
    private JournalGroupMapper journalGroupMapper;

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
                journalDTO.setJournalGroupIdAt(0);
            journalMapper.createJournal(journalDTO);
        } catch (Exception e) {
            throw new CreateJournalFailedException(MessageConstant.CREATE_JOURNAL_FAILED);
        }
    }

    /**
     * 在日记串中创建日记功能
     * @param journalDTO 传输日记的相关信息
     */

    @Override
    public void createJournalAtJournalGroup(JournalDTO journalDTO) {
        //先判断日记串是不是自己的 如果不是返回错误结果
        JournalGroupVO journalGroup = journalGroupMapper.getJournalGroup(journalDTO.getUserIdAt(), journalDTO.getJournalGroupIdAt());
        if (journalGroup == null) {
            throw new CreateJournalFailedException(MessageConstant.JOURNAL_GROUP_MATCH_FAILED);
        }

        try {
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
        if (journalDTO == null) {
            throw new ModifyJournalFailedException(MessageConstant.COMMON_ERROR);
        }

        //先看看日记属不属于自己 不属于自己则返回错误信息
        JournalVO journalVO = journalMapper.getJournalByJID(journalDTO);
        if (journalVO == null) {
            throw new ModifyJournalFailedException(MessageConstant.MODIFY_JOURNAL_ERROR);
        }

        //调用Mapper 修改日记信息 修改失败返回错误信息
        //限制topJournal传输0/1
        if (journalDTO.getTopJournal() != null) {
            int topJournal = journalDTO.getTopJournal();
            if(topJournal != 0 && topJournal!= 1 ){
                throw new SetTopJournalFailedException(MessageConstant.SET_TOPJOURNAL_FAILED);
            }
        }
        //限制isDeleted传输0/1
        if (journalDTO.getIsDeleted() != null) {
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

    /**
     * 根据日记id获取日记信息
     * @param journalDTO 传输日记id和uid
     * @return 返回JournalVO对象
     */
    @Override
    public JournalVO getJournalByJID(JournalDTO journalDTO) {

        try {
            return journalMapper.getJournalByJID(journalDTO);
        } catch (Exception e) {
            throw new GetJournalsFailedException(MessageConstant.GET_JOURNALS_FAILED);
        }
    }
}