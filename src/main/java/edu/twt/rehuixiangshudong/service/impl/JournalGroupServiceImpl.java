package edu.twt.rehuixiangshudong.service.impl;

import edu.twt.rehuixiangshudong.mapper.JournalGroupMapper;
import edu.twt.rehuixiangshudong.service.JournalGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JournalGroupServiceImpl implements JournalGroupService {
    @Autowired
    private JournalGroupMapper journalGroupMapper;
}
