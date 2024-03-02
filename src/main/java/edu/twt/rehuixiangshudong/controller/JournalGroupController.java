package edu.twt.rehuixiangshudong.controller;

import edu.twt.rehuixiangshudong.service.JournalGroupService;
import edu.twt.rehuixiangshudong.zoo.constant.MessageConstant;
import edu.twt.rehuixiangshudong.zoo.dto.JournalGroupDTO;
import edu.twt.rehuixiangshudong.zoo.result.Result;
import edu.twt.rehuixiangshudong.zoo.util.ThreadLocalUtil;
import edu.twt.rehuixiangshudong.zoo.vo.JournalGroupVO;
import edu.twt.rehuixiangshudong.zoo.vo.JournalVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping
@Slf4j
public class JournalGroupController {
    @Autowired
    private JournalGroupService journalGroupService;

    /**
     * 创建日记串
     * @param journalGroupDTO 传输日记串信息
     * @return 返回信息
     */
    @PostMapping("/createJournalGroup")
    public Result<Object> createJournalGroup(@RequestBody JournalGroupDTO journalGroupDTO) {
        //获取token中的uid
        Integer uid = ThreadLocalUtil.getCurrentUid();

        log.info("uid为 {} 的用户创建日记串 {}",uid,journalGroupDTO);
        if (journalGroupDTO == null) {
            return Result.fail(MessageConstant.CREATE_JOURNAL_GROUP_FAILED);
        }
        journalGroupDTO.setUserIdAt(uid);

        journalGroupService.createJournalGroup(journalGroupDTO);

        return Result.success(MessageConstant.CREATE_JOURNAL_GROUP_SUCCESS);
    }
    /**
     * 获取指定id的日记串
     * @param journalGroupDTO 传输日记串id
     * @return JournalGroupVO模型
     */
    @GetMapping("/getJournalGroup")
    public Result<JournalGroupVO> getJournalGroup(@RequestBody JournalGroupDTO journalGroupDTO) {
        //获取token中的uid
        Integer uid = ThreadLocalUtil.getCurrentUid();
        if (journalGroupDTO == null) {
            return Result.fail(MessageConstant.COMMON_ERROR);
        }
        Integer journalGroupId = journalGroupDTO.getJournalGroupId();

        log.info("uid为 {} 的用户 查询 日记串id为 {} 的日记串 ",uid,journalGroupId);

        JournalGroupVO journalGroupVO = journalGroupService.getJournalGroup(uid, journalGroupId);

        //查询日记为空 定义返回结果
        if (journalGroupVO == null) {
            return Result.fail(MessageConstant.GET_JOURNALGROUP_ERROR);
        }
        return Result.success(journalGroupVO);
    }

    /**
     * 获取日记串中的全部日记
     * @param journalGroupDTO 传输日记串id
     * @return 返回List集合对象
     */
    @GetMapping("/getJournalsInJournalGroup")
    public Result<List<JournalVO>> getJournalsInJournalGroup(@RequestBody JournalGroupDTO journalGroupDTO) {
        //获取token中的uid
        Integer uid = ThreadLocalUtil.getCurrentUid();
        if (journalGroupDTO == null) {
            return Result.fail(MessageConstant.COMMON_ERROR);
        }
        Integer journalGroupId = journalGroupDTO.getJournalGroupId();

        log.info("uid为 {} 的用户 查询 日记串id为 {} 的日记串中的全部日记 ",uid,journalGroupId);

        List<JournalVO> journalVOS = journalGroupService.getJournalsInJournalGroup(uid, journalGroupId);

        return Result.success(journalVOS);

    }

}
