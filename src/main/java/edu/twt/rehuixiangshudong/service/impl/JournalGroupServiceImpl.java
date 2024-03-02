package edu.twt.rehuixiangshudong.service.impl;

import edu.twt.rehuixiangshudong.mapper.JournalGroupMapper;
import edu.twt.rehuixiangshudong.mapper.JournalMapper;
import edu.twt.rehuixiangshudong.service.JournalGroupService;
import edu.twt.rehuixiangshudong.zoo.constant.MessageConstant;
import edu.twt.rehuixiangshudong.zoo.dto.JournalGroupDTO;
import edu.twt.rehuixiangshudong.zoo.exception.*;
import edu.twt.rehuixiangshudong.zoo.vo.JournalGroupVO;
import edu.twt.rehuixiangshudong.zoo.vo.JournalVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JournalGroupServiceImpl implements JournalGroupService {
    @Autowired
    private JournalGroupMapper journalGroupMapper;
    @Autowired
    private JournalMapper journalMapper;

    /**
     * 创建日记串
     * @param journalGroupDTO 创建日记串传输数据模型
     */
    @Override
    public void createJournalGroup(JournalGroupDTO journalGroupDTO) {
        try {
            //如果传输的名字是空字符串 则设置为“未设置”
            if (journalGroupDTO.getJournalGroupName().isEmpty()) {
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
            return journalGroupMapper.getJournalGroup(uid, journalGroupId);
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
            return journalGroupMapper.getJournalsInJournalGroup(uid, journalGroupId);
        } catch (Exception e) {
            throw new GetJournalsFailedException(MessageConstant.GET_JOURNALS_FAILED);
        }
    }

    /**
     * 修改日记串名字
     * @param journalGroupName 修改后日记串名字
     * @param uid 用户uid
     * @param journalGroupId 日记串id
     */
    @Override
    public void changeJournalGroupName(String journalGroupName, Integer uid, Integer journalGroupId) {
        //先看看日记串存在不存在
        JournalGroupVO journalGroupVO = journalGroupMapper.getJournalGroup(uid, journalGroupId);
        if (journalGroupVO == null) {
            throw new ChangeJournalGroupNameFailedException(MessageConstant.GET_JOURNALGROUP_ERROR);
        }

        try {
            journalGroupMapper.changeJournalGroupName(journalGroupName, uid,journalGroupId);
        } catch (Exception e) {
            throw new ChangeJournalGroupNameFailedException(MessageConstant.CHANGE_JGNAME_FAILED);
        }
    }

    /**
     * 删除指定id的日记串
     * @param uid 用户uid
     * @param journalGroupId 要删除的日记串id
     */
    @Override
    public void deleteJournalGroup(Integer uid, Integer journalGroupId) {
        //先判断日记串是否存在 不存在返回错误信息
        JournalGroupVO journalGroupVO = journalGroupMapper.getJournalGroup(uid, journalGroupId);
        if (journalGroupVO == null) {
            throw new DeleteJournalGroupFailedException(MessageConstant.GET_JOURNALGROUPS_FAILED);
        }
        //再判断日记串中是否含有日记 如果含有则删除失败
        List<JournalVO> journalsInJournalGroup = journalGroupMapper.getJournalsInJournalGroup(uid, journalGroupId);
        if (journalsInJournalGroup != null && !(journalsInJournalGroup.isEmpty())) {//日记串不空的情况
            throw new DeleteJournalGroupFailedException(MessageConstant.DELETE_JOURNAL_GROUP_FAILED);
        }
        //进行删除
        try {
            journalGroupMapper.deleteJournalGroup(uid, journalGroupId);
        } catch (Exception e) {
            throw new DeleteJournalGroupFailedException(MessageConstant.DELETE_JOURNAL_GROUP_ERROR);
        }
    }

    /**
     * 添加日记到指定日记串
     * @param uid 用户的uid
     * @param journalId 日记id
     * @param journalGroupId 日记串id
     */
    @Override
    public void addJournalToJournalGroup(int uid, int journalId, int journalGroupId) {
        //先判断日记是否位于日记串中
        JournalVO journalVO1 = journalMapper.checkJournalInJournalGroup(journalId,journalGroupId);
        if (journalVO1 != null) {
            throw new AddJournalToJournalGroupFailedException(MessageConstant.ADD_JOURNAL_FAILED_EXIST);
        }
        //再判断日记和日记串是否属于自己
        JournalVO journalVO = journalMapper.getJournalByJID2(uid,journalId);
        JournalGroupVO journalGroupVO = journalGroupMapper.getJournalGroup(uid,journalGroupId);
        //不属于自己 给出添加失败结果
        if (journalVO == null || journalGroupVO == null) {
            throw new AddJournalToJournalGroupFailedException(MessageConstant.ADD_JOURNAL_FAILED);
        }


        //属于自己且不包含在该日记串中，进行更改
        journalGroupMapper.addJournalToJournalGroup(uid,journalId,journalGroupId);

    }
}
