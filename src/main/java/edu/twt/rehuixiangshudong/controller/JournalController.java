package edu.twt.rehuixiangshudong.controller;

import edu.twt.rehuixiangshudong.service.JournalService;
import edu.twt.rehuixiangshudong.zoo.constant.MessageConstant;
import edu.twt.rehuixiangshudong.zoo.dto.JournalDTO;
import edu.twt.rehuixiangshudong.zoo.result.Result;
import edu.twt.rehuixiangshudong.zoo.util.ThreadLocalUtil;
import edu.twt.rehuixiangshudong.zoo.vo.JournalVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@Slf4j
public class JournalController {
    @Autowired
    private JournalService journalService;
    /**
     * 创建日记
     * @param journalDTO 传输日记信息
     * @return 返回成功消息
     */
    @PostMapping("/createJournal")
    public Result<Object> createJournal(@RequestBody JournalDTO journalDTO){
        if (journalDTO == null) {
            return Result.fail(MessageConstant.COMMON_ERROR);
        }
        //获取token中的uid,并将其设置到journalDTO中
        Integer uid = ThreadLocalUtil.getCurrentUid();
        journalDTO.setUserIdAt(uid);

        log.info("uid为 {} 的用户创建日记 {}",uid,journalDTO);
        journalService.createJournal(journalDTO);

        return Result.success(MessageConstant.CREATE_JOURNAL_SUCCESS);
    }

    /**
     * 修改日记
     * @param journalDTO 传输修改日记的参数
     * @return 返回修改成功信息
     */
    @PutMapping("/modifyJournal")
    public Result<Object> modifyJournal(@RequestBody JournalDTO journalDTO){
        //获取token中的uid
        Integer uid = ThreadLocalUtil.getCurrentUid();

        log.info("uid为 {} 的用户修改日记 {}",uid,journalDTO);

        if (journalDTO != null) {//非空判断 防止空指针异常
            journalDTO.setUserIdAt(uid);
        }else {
            return Result.fail(MessageConstant.MODIFY_JOURNAL_FAILED);
        }
        journalService.modifyJournal(journalDTO);

        return Result.success(MessageConstant.MODIFY_JOURNAL_SUCCESS);

    }

    /**
     * 根据日记id获取日记信息
     * @param journalDTO 传输日记id
     */
    @GetMapping("/getJournalByJID")
    public Result<JournalVO> getJournalByJID(@RequestBody JournalDTO journalDTO) {
        //根据token获取uid
        Integer uid = ThreadLocalUtil.getCurrentUid();
        if (journalDTO != null) {
            journalDTO.setUserIdAt(uid);
            log.info("uid为 {} 的用户获取指定日记id为 {} 的日记信息",uid,journalDTO.getJournalId());
        }else {
            return Result.fail(MessageConstant.GET_JOURNALS_FAILED);
        }


        JournalVO journal = journalService.getJournalByJID(journalDTO);
        if (journal == null) {
            return Result.fail(MessageConstant.NO_JOURNAL_FOUND);
        }
        return Result.success(journal);

    }

    @PostMapping("/createJournalAtJournalGroup")
    public Result<Object> createJournalAtJournalGroup(@RequestBody JournalDTO journalDTO) {
        //获取token中的uid,并将其设置到journalDTO中
        Integer uid = ThreadLocalUtil.getCurrentUid();
        journalDTO.setUserIdAt(uid);
        Integer journalGroupId = journalDTO.getJournalGroupIdAt();
        log.info("uid为 {} 的用户在 日记串{} 中创建日记 {}",uid,journalGroupId,journalDTO);

        journalService.createJournalAtJournalGroup(journalDTO);

        return Result.success(MessageConstant.CREATE_JOURNAL_SUCCESS);
    }

}
