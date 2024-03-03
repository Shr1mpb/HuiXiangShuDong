package edu.twt.rehuixiangshudong.controller;

import edu.twt.rehuixiangshudong.service.JournalGroupService;
import edu.twt.rehuixiangshudong.zoo.constant.MessageConstant;
import edu.twt.rehuixiangshudong.zoo.dto.JournalGroupDTO;
import edu.twt.rehuixiangshudong.zoo.dto.JournalGroupPageQueryDTO;
import edu.twt.rehuixiangshudong.zoo.result.PageResult;
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

    /**
     * 修改日记串名称
     * @param journalGroupDTO 传入日记串id和日记串名称
     * @return 返回成功与否信息
     */
    @PutMapping("/changeJournalGroupName")
    public Result<Object> changeJournalGroupName(@RequestBody JournalGroupDTO journalGroupDTO) {
        //获取token中的uid
        Integer uid = ThreadLocalUtil.getCurrentUid();
        if (journalGroupDTO == null) {
            return Result.fail(MessageConstant.COMMON_ERROR);
        }

        //从DTO中获取数据
        String journalGroupName = journalGroupDTO.getJournalGroupName();
        Integer journalGroupId = journalGroupDTO.getJournalGroupId();
        log.info("uid为 {} 的用户修改日记串 {} 名称",uid,journalGroupId);

        journalGroupService.changeJournalGroupName(journalGroupName,uid,journalGroupId);

        return Result.success(MessageConstant.CHANGE_JGNAME_SUCCESS);

    }

    /**
     * 删除日记串的功能
     * @param journalGroupDTO 删除的日记串的id
     * @return 返回信息结果
     */
    @PutMapping("/deleteJournalGroup")
    public Result<Object> deleteJournalGroup(@RequestBody JournalGroupDTO journalGroupDTO) {
        if (journalGroupDTO == null) {
            return Result.fail(MessageConstant.COMMON_ERROR);
        }
        //获取token中的uid
        Integer uid = ThreadLocalUtil.getCurrentUid();
        Integer journalGroupId = journalGroupDTO.getJournalGroupId();

        log.info("uid为 {} 的用户 删除 日记串id为 {} 的日记串 ",uid,journalGroupId);

        journalGroupService.deleteJournalGroup(uid, journalGroupId);

        return Result.success(MessageConstant.DELETE_JOURNAL_GROUP_SUCCESS);
    }
    /**
     * 添加单个日记到日记串中
     * @param journalGroupDTO 要添加的日记id 添加进的日记串id
     * @return 返回信息结果
     */
    @PutMapping("/addJournalToJournalGroup")
    public Result<Object> addJournalToJournalGroup(@RequestBody JournalGroupDTO journalGroupDTO) {
        //获取token中的uid
        int uid = ThreadLocalUtil.getCurrentUid();
        int journalId = journalGroupDTO.getJournalId();
        int journalGroupId = journalGroupDTO.getJournalGroupId();
        log.info("uid为 {} 的用户添加日记id {} 到日记串id {} ",uid,journalId,journalGroupId);

        journalGroupService.addJournalToJournalGroup(uid, journalId, journalGroupId);

        return Result.success(MessageConstant.ADD_JOURNAL_SUCCESS);

    }

    /**
     * 把日记从日记串中移除
     * @param journalGroupDTO 要移除的日记串
     * @return 返回结果
     */
    @PutMapping("/deleteJournalFromJournalGroup")
    public Result<Object> deleteJournalFromJournalGroup(@RequestBody JournalGroupDTO journalGroupDTO) {
        if (journalGroupDTO == null) {
            return Result.fail(MessageConstant.COMMON_ERROR);
        }
        //获取token中的uid
        int uid = ThreadLocalUtil.getCurrentUid();
        int journalGroupId = journalGroupDTO.getJournalGroupId();
        int journalId = journalGroupDTO.getJournalId();

        journalGroupService.deleteJournalFromJournalGroup(uid, journalGroupId, journalId);

        return Result.success(MessageConstant.DELETE_JOURNAL_FROM_JG_SUCCESS);
    }

    /**
     * 分页查询日记串
     */
    @GetMapping("/getJournalGroups")
    public Result<PageResult> getJournalGroups(JournalGroupPageQueryDTO journalGroupPageQueryDTO) {
        if (journalGroupPageQueryDTO == null) {
            return Result.fail(MessageConstant.COMMON_ERROR);
        }
        //获取uid并设置
        Integer uid = ThreadLocalUtil.getCurrentUid();
        journalGroupPageQueryDTO.setUserIdAt(uid);
        log.info("uid为 {} 的用户分页查询日记串 {}", uid, journalGroupPageQueryDTO);

        PageResult pageResult = journalGroupService.getJournalGroups(journalGroupPageQueryDTO);

        return Result.success(pageResult);
    }
}
