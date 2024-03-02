package edu.twt.rehuixiangshudong.controller;

import edu.twt.rehuixiangshudong.service.JournalGroupService;
import edu.twt.rehuixiangshudong.zoo.constant.MessageConstant;
import edu.twt.rehuixiangshudong.zoo.dto.JournalGroupDTO;
import edu.twt.rehuixiangshudong.zoo.result.Result;
import edu.twt.rehuixiangshudong.zoo.util.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}
