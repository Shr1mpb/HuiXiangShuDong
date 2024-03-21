package edu.twt.rehuixiangshudong.controller;

import edu.twt.rehuixiangshudong.service.JournalService;
import edu.twt.rehuixiangshudong.zoo.constant.MessageConstant;
import edu.twt.rehuixiangshudong.zoo.dto.JournalDTO;
import edu.twt.rehuixiangshudong.zoo.dto.JournalPageQueryDTO;
import edu.twt.rehuixiangshudong.zoo.result.PageResult;
import edu.twt.rehuixiangshudong.zoo.result.PictureResult;
import edu.twt.rehuixiangshudong.zoo.result.Result;
import edu.twt.rehuixiangshudong.zoo.util.ThreadLocalUtil;
import edu.twt.rehuixiangshudong.zoo.vo.JournalVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping
@Slf4j
public class JournalController {
    @Autowired
    private JournalService journalService;
    private static final String DATE_PATTERN = "^\\d{4}-(0?[1-9]|1[0-2])-(0?[1-9]|[12][0-9]|3[01])$";
    /**
     * 创建日记
     * @return 返回成功消息
     */
    @PostMapping("/createJournal")
    public Result<Object> createJournal(List<MultipartFile> files, String location,String journalTitle,String journalText,Integer topJournal){

        JournalDTO journalDTO = new JournalDTO();
        journalDTO.setLocation(location);
        journalDTO.setJournalTitle(journalTitle);
        journalDTO.setJournalText(journalText);
        journalDTO.setTopJournal(topJournal);
        //获取token中的uid,并将其设置到journalDTO中
        Integer uid = ThreadLocalUtil.getCurrentUid();
        journalDTO.setUserIdAt(uid);

        log.info("uid为 {} 的用户创建日记 {}",uid,journalDTO);
        journalService.createJournal(journalDTO);

        //创建日记成功后，为日记上传日记图片
        if (!files.isEmpty()){
            for (MultipartFile file : files) {
                journalService.uploadJournalPicture(uid,journalDTO.getJournalId(),file);
            }
        }

        return Result.success(MessageConstant.CREATE_JOURNAL_SUCCESS);
    }

    /**
     * 修改日记
     * @param journalDTO 传输修改日记的参数
     * @return 返回修改成功信息
     */
    @PutMapping("/modifyJournal")
    public Result<Object> modifyJournal(@RequestBody JournalDTO journalDTO){
        if (journalDTO == null) {
            return Result.fail(MessageConstant.COMMON_ERROR);
        }
        //获取token中的uid
        Integer uid = ThreadLocalUtil.getCurrentUid();
        journalDTO.setUserIdAt(uid);

        log.info("uid为 {} 的用户修改日记 {}",uid,journalDTO);

        journalService.modifyJournal(journalDTO);
        if (journalDTO.getIsDeleted() == 1) {
            return Result.success(MessageConstant.DELETE_JOURNAL_SUCCESS);
        }
        if (journalDTO.getTopJournal() == 1) {
            return Result.success(MessageConstant.SET_TOPJOURNAL_SUCCESS);
        }
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

    /**
     * 在日记串中创建日记
     * @return 返回信息结果
     */
    @PostMapping("/createJournalAtJournalGroup")
    public Result<Object> createJournalAtJournalGroup(List<MultipartFile> files,String location,String journalTitle,String journalText,Integer topJournal,Integer journalGroupIdAt) {
        JournalDTO journalDTO = new JournalDTO();
        journalDTO.setLocation(location);
        journalDTO.setJournalTitle(journalTitle);
        journalDTO.setJournalText(journalText);
        journalDTO.setTopJournal(topJournal);
        journalDTO.setJournalGroupIdAt(journalGroupIdAt);

        //获取token中的uid,并将其设置到journalDTO中
        Integer uid = ThreadLocalUtil.getCurrentUid();
        journalDTO.setUserIdAt(uid);
        Integer journalGroupId = journalDTO.getJournalGroupIdAt();
        log.info("uid为 {} 的用户在 日记串{} 中创建日记 {}",uid,journalGroupId,journalDTO);

        //创建日记
        journalService.createJournalAtJournalGroup(journalDTO);
        //创建成功，为该日记上传文件(通过主键回显拿到日记id)
        if (!files.isEmpty()) {
            for (MultipartFile file : files) {
                journalService.uploadJournalPicture(uid, journalDTO.getJournalId(), file);
            }
        }

        return Result.success(MessageConstant.CREATE_JOURNAL_SUCCESS);
    }

    /**
     * 分页查询日记数据 参数形式Query
     * @param journalPageQueryDTO 传输查询用的 日记标题、页码、每页记录数
     * @return 返回查询结果
     */
    @GetMapping("/getJournalsByUid")
    public Result<PageResult> getJournalsByUid(JournalPageQueryDTO journalPageQueryDTO) {

        Pattern pattern = Pattern.compile(DATE_PATTERN);
        Matcher matcher = pattern.matcher(journalPageQueryDTO.getDate());
        if (journalPageQueryDTO.getDate().isEmpty()){//日期栏没填，不判断格式直接查询全部
            Integer uid = ThreadLocalUtil.getCurrentUid();
            journalPageQueryDTO.setUserIdAt(uid);
            log.info("uid为 {} 的用户分页查询日记 {}", uid, journalPageQueryDTO);

            PageResult pageResult = journalService.getJournalsByUid(journalPageQueryDTO);

            return Result.success(pageResult);
        }else if (!matcher.matches()) {//日期格式不对 直接返回错误信息
            return Result.fail(MessageConstant.ERROR_DATE_FORMAT);
        }else {
            return Result.fail(MessageConstant.GET_JOURNALS_FAILED);
        }


    }

    /**
     * 根据日记id获取日记图片
     * @param journalId 日记id
     * @return 返回格式
     *             {
     *                 pictureId:
     *                 pictureUrl:
     *             } ,
     *             {
     *                 pictureId:
     *                 pictureUrl:
     *             }
     */
    @GetMapping("/getJournalPictures")
    public Result<List<PictureResult>> getJournalPictures(int journalId) {
        Integer uid = ThreadLocalUtil.getCurrentUid();
        log.info("uid为 {} 的用户查询 日记id为{} 的日记图片",uid,journalId);

        List<PictureResult> pictures = journalService.getJournalPictures(uid,journalId);

        return Result.success(pictures);
    }

    /**
     * 上传日记图片
     * @param journalId 日记id
     * @param file 图片
     * @return 返回文字结果
     */
    @PostMapping("/uploadJournalPicture")
    public Result uploadJournalPicture(int journalId, MultipartFile file) {
        Integer uid = ThreadLocalUtil.getCurrentUid();
        log.info("uid为 {} 的用户上传了日记id{} 的日记图片", uid, journalId);

        journalService.uploadJournalPicture(uid,journalId, file);
        return Result.success(MessageConstant.UPLOAD_SUCCESS);
    }

    @PutMapping("/deleteJournalPicture")
    public Result deleteJournalPicture(int journalId,int pictureId) {
        Integer uid = ThreadLocalUtil.getCurrentUid();
        log.info("uid为 {} 的用户删除pictureId为 {} 的日记图片", uid, pictureId);

        journalService.deleteJournalPicture(uid, journalId, pictureId);
        return Result.success(MessageConstant.DELETE_JOURNALPICTURE_SUCCESS);
    }

}
