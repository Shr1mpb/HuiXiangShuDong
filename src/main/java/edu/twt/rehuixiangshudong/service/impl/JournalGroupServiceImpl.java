package edu.twt.rehuixiangshudong.service.impl;

import edu.twt.rehuixiangshudong.mapper.JournalGroupMapper;
import edu.twt.rehuixiangshudong.service.JournalGroupService;
import edu.twt.rehuixiangshudong.zoo.constant.MessageConstant;
import edu.twt.rehuixiangshudong.zoo.dto.JournalGroupDTO;
import edu.twt.rehuixiangshudong.zoo.exception.CreateJournalGroupFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JournalGroupServiceImpl implements JournalGroupService {
    @Autowired
    private JournalGroupMapper journalGroupMapper;

    @Override
    public void createJournalGroup(JournalGroupDTO journalGroupDTO) {
        try {
            journalGroupMapper.createJournalGroup(journalGroupDTO);
        } catch (Exception e) {
            throw new CreateJournalGroupFailedException(MessageConstant.CREATE_JOURNAL_GROUP_FAILED);
        }
    }
}
