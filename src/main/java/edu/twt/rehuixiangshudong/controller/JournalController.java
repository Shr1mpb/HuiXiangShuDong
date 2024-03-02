package edu.twt.rehuixiangshudong.controller;

import edu.twt.rehuixiangshudong.service.JournalService;
import edu.twt.rehuixiangshudong.zoo.constant.MessageConstant;
import edu.twt.rehuixiangshudong.zoo.dto.JournalDTO;
import edu.twt.rehuixiangshudong.zoo.result.Result;
import edu.twt.rehuixiangshudong.zoo.util.ThreadLocalUtil;
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

        //获取token中的uid,并将其设置到journalDTO中
        Integer uid = ThreadLocalUtil.getCurrentUid();
        if (journalDTO != null) {//非空判断 防止空指针异常
            journalDTO.setUserIdAt(uid);
        }

        log.info("uid为 {} 的用户创建日记 {}",uid,journalDTO);
        journalService.createJournal(journalDTO);

        return Result.success(MessageConstant.CREATE_JOURNAL_SUCCESS);
    }

    /**
     * 修改日记
     * @param journalDTO
     * @return 返回修改成功信息
     */
    @PutMapping     ("/modifyJournal")
    public Result<Object> modifyJournal(@RequestBody JournalDTO journalDTO, @RequestHeader("token") String token){
        //获取token中的uid
        Integer uid = ThreadLocalUtil.getCurrentUid();

        log.info("uid为 {} 的用户修改日记 {}",uid,journalDTO);

        if (journalDTO != null) {//非空判断 防止空指针异常
            journalDTO.setUserIdAt(uid);
        }
        journalService.modifyJournal(journalDTO);

        return Result.success(MessageConstant.MODIFY_JOURNAL_SUCCESS);

    }

}
