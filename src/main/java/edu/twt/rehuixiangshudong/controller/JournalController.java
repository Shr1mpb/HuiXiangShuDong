package edu.twt.rehuixiangshudong.controller;

import edu.twt.rehuixiangshudong.service.JournalService;
import edu.twt.rehuixiangshudong.zoo.constant.MessageConstant;
import edu.twt.rehuixiangshudong.zoo.dto.JournalDTO;
import edu.twt.rehuixiangshudong.zoo.result.Result;
import edu.twt.rehuixiangshudong.zoo.util.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        journalDTO.setUserIdAt(uid);

        log.info("uid为 {} 的用户创建日记 {}",uid,journalDTO);
        journalService.createJournal(journalDTO);

        return Result.success(MessageConstant.CREATE_JOURNAL_SUCCESS);
    }
}
