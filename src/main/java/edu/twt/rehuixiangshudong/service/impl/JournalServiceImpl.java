package edu.twt.rehuixiangshudong.service.impl;

import edu.twt.rehuixiangshudong.mapper.JournalMapper;
import edu.twt.rehuixiangshudong.service.JournalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JournalServiceImpl implements JournalService {
    @Autowired
    private JournalMapper journalMapper;
}
