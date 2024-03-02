package edu.twt.rehuixiangshudong.service.impl;

import edu.twt.rehuixiangshudong.mapper.JournalGroupMapper;
import edu.twt.rehuixiangshudong.service.JournalGroupService;
import edu.twt.rehuixiangshudong.zoo.constant.MessageConstant;
import edu.twt.rehuixiangshudong.zoo.dto.JournalGroupDTO;
import edu.twt.rehuixiangshudong.zoo.exception.CreateJournalGroupFailedException;
import edu.twt.rehuixiangshudong.zoo.exception.GetJournalGroupFailedException;
import edu.twt.rehuixiangshudong.zoo.exception.GetJournalsFailedException;
import edu.twt.rehuixiangshudong.zoo.vo.JournalGroupVO;
import edu.twt.rehuixiangshudong.zoo.vo.JournalVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JournalGroupServiceImpl implements JournalGroupService {
    @Autowired
    private JournalGroupMapper journalGroupMapper;

    /**
     * 创建日记串
     * @param journalGroupDTO 创建日记串传输数据模型
     */
    @Override
    public void createJournalGroup(JournalGroupDTO journalGroupDTO) {
        try {
            //如果传输的名字是空字符串 则设置为“未设置”
            if (journalGroupDTO.getJournalGroupName().equals("")) {
                journalGroupDTO.setJournalGroupName(MessageConstant.EMPTY_DATA);
            }
            journalGroupMapper.createJournalGroup(journalGroupDTO);
        } catch (Exception e) {
            throw new CreateJournalGroupFailedException(MessageConstant.CREATE_JOURNAL_GROUP_FAILED);
        }
    }

    /**
     * 获取指定ID的日记串
     * @param uid 用户的uid
     * @param journalGroupId 指定日记串id
     * @return 返回日记串对象
     */
    @Override
    public JournalGroupVO getJournalGroup(Integer uid, Integer journalGroupId) {
        //获取数据 失败返回错误结果
        try {
            JournalGroupVO journalGroupVO = journalGroupMapper.getJournalGroup(uid, journalGroupId);
            return journalGroupVO;
        } catch (Exception e) {
            throw new GetJournalGroupFailedException(MessageConstant.GET_JOURNALGROUP_FAILED);
        }
    }

    /**
     * 获取指定日记串id中的日记
     * @param uid 用户的uid
     * @param journalGroupId 传输日记串id
     * @return 返回List集合
     */
    @Override
    public List<JournalVO> getJournalsInJournalGroup(Integer uid, Integer journalGroupId) {
        //先判断日记串属不属于自己
        JournalGroupVO journalGroupVO = journalGroupMapper.getJournalGroup(uid,journalGroupId);
        if (journalGroupVO == null) {//如果日记串不存在 返回日记串不存在字样
            throw new GetJournalsFailedException(MessageConstant.JOURNAL_GROUP_MATCH_FAILED);
        }
        //日记串存在且属于自己 进行查询
        try {
            List<JournalVO> journalVOS = journalGroupMapper.getJournalsInJournalGroup(uid, journalGroupId);
            return journalVOS;
        } catch (Exception e) {
            throw new GetJournalsFailedException(MessageConstant.GET_JOURNALS_FAILED);
        }
    }
}
