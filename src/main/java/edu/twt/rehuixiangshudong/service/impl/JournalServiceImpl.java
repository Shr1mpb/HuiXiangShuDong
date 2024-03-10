package edu.twt.rehuixiangshudong.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import edu.twt.rehuixiangshudong.mapper.JournalGroupMapper;
import edu.twt.rehuixiangshudong.mapper.JournalMapper;
import edu.twt.rehuixiangshudong.mapper.UserMapper;
import edu.twt.rehuixiangshudong.service.JournalService;
import edu.twt.rehuixiangshudong.zoo.constant.MessageConstant;
import edu.twt.rehuixiangshudong.zoo.dto.JournalDTO;
import edu.twt.rehuixiangshudong.zoo.dto.JournalPageQueryDTO;
import edu.twt.rehuixiangshudong.zoo.exception.*;
import edu.twt.rehuixiangshudong.zoo.result.PageResult;
import edu.twt.rehuixiangshudong.zoo.result.PictureResult;
import edu.twt.rehuixiangshudong.zoo.result.Result;
import edu.twt.rehuixiangshudong.zoo.util.AliOssUtil;
import edu.twt.rehuixiangshudong.zoo.vo.JournalGroupVO;
import edu.twt.rehuixiangshudong.zoo.vo.JournalVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class JournalServiceImpl implements JournalService {
    @Autowired
    private JournalMapper journalMapper;
    @Autowired
    private JournalGroupMapper journalGroupMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AliOssUtil aliOssUtil;

    /**
     * 创建日记功能
     * 创建日记时 若journalGroupIdAt没有填写 则默认写0
     * @param journalDTO 传输日记的模版
     * journalGroupIdAt 数据库中不能为空
     *
     */
    @Override
    public void createJournal(JournalDTO journalDTO) {

        try {
                journalDTO.setJournalGroupIdAt(0);
            journalMapper.createJournal(journalDTO);
            //创建日记 用户日记+1
            userMapper.updateJournalCount(1,journalDTO.getUserIdAt());
        } catch (Exception e) {
            throw new CreateJournalFailedException(MessageConstant.CREATE_JOURNAL_FAILED);
        }
    }

    /**
     * 在日记串中创建日记功能
     * @param journalDTO 传输日记的相关信息
     */

    @Override
    public void createJournalAtJournalGroup(JournalDTO journalDTO) {
        //先判断日记串是不是自己的 如果不是返回错误结果
        JournalGroupVO journalGroup = journalGroupMapper.getJournalGroup(journalDTO.getUserIdAt(), journalDTO.getJournalGroupIdAt());
        if (journalGroup == null) {
            throw new CreateJournalFailedException(MessageConstant.JOURNAL_GROUP_MATCH_FAILED);
        }

        try {//在日记串中创建日记
            journalMapper.createJournal(journalDTO);
            //创建日记 用户日记数量 +1
            userMapper.updateJournalCount(1,journalDTO.getUserIdAt());
            //日记串包含的日记数量 +1
            journalGroupMapper.updateJournalCountOfJournalGroup(1,journalDTO.getUserIdAt(),journalDTO.getJournalGroupIdAt());
        } catch (Exception e) {
            throw new CreateJournalFailedException(MessageConstant.CREATE_JOURNAL_FAILED);
        }
    }

    /**
     * 修改日记信息（包含删除与置顶）
     * @param journalDTO 传输日记的信息
     */
    @Override
    public void modifyJournal(JournalDTO journalDTO) {
        if (journalDTO == null) {
            throw new ModifyJournalFailedException(MessageConstant.COMMON_ERROR);
        }

        //先看看日记属不属于自己 不属于自己则返回错误信息
        JournalVO journalVO = journalMapper.getJournalByJID(journalDTO);
        if (journalVO == null) {
            throw new ModifyJournalFailedException(MessageConstant.MODIFY_JOURNAL_ERROR);
        }

        //调用Mapper 修改日记信息 修改失败返回错误信息
        //限制topJournal传输0/1
        if (journalDTO.getTopJournal() != null) {
            int topJournal = journalDTO.getTopJournal();
            if(topJournal != 0 && topJournal!= 1 ){
                throw new SetTopJournalFailedException(MessageConstant.SET_TOPJOURNAL_FAILED);
            }
        }
        //限制isDeleted传输0/1
        if (journalDTO.getIsDeleted() != null) {
            int isDeleted = journalDTO.getIsDeleted();
            //限制topJournal传输0或1
            if(isDeleted != 0 && isDeleted  != 1 ){
                throw new SetTopJournalFailedException(MessageConstant.SET_TOPJOURNAL_FAILED);
            }
        }
        //如果删除日记 先让用户日记数-1
        if (journalDTO.getIsDeleted() == 1) {
            userMapper.updateJournalCount(-1,journalDTO.getUserIdAt());
        }
        try {
            journalMapper.modifyJournal(journalDTO);
        } catch (Exception e) {
            throw new ModifyJournalFailedException(MessageConstant.MODIFY_JOURNAL_FAILED);
        }
    }

    /**
     * 根据日记id获取日记信息
     * @param journalDTO 传输日记id和uid
     * @return 返回JournalVO对象
     */
    @Override
    public JournalVO getJournalByJID(JournalDTO journalDTO) {

        try {
            return journalMapper.getJournalByJID(journalDTO);
        } catch (Exception e) {
            throw new GetJournalsFailedException(MessageConstant.GET_JOURNALS_FAILED);
        }
    }

    /**
     * 分页查询日记信息
     * @param journalPageQueryDTO 查询名称 页码 每页记录数
     * @return PageResult结果
     */
    @Override
    public PageResult getJournalsByUid(JournalPageQueryDTO journalPageQueryDTO) {
        //select * from employ limit 0,10
        //引入PageHelper依赖 开始分页查询

        PageHelper.startPage(journalPageQueryDTO.getPage(), journalPageQueryDTO.getPageSize());
    //根据range提供查询的信息
        String date = journalPageQueryDTO.getDate();
        if (journalPageQueryDTO.getRange() == 1) {//按月份查询，将date直接设置为中间的月，并且设置年份为前面的年
            //先给year赋值,后给date赋值
            int startIndex = date.indexOf("-"); // 找到第一个'-'后面的位置 包前不包后
            int endIndex = date.indexOf("-", startIndex + 1); // 从startIndex开始找到第二个'-'的位置
            journalPageQueryDTO.setYear(date.substring(0, startIndex));
            journalPageQueryDTO.setDate(date.substring(startIndex + 1, endIndex));
        }else if (journalPageQueryDTO.getRange() == 2) {//按年查询，将date直接设置为年份
            int startIndex = date.indexOf("-"); // 找到第一个'-'后面的位置 包前不包后
            journalPageQueryDTO.setYear(date.substring(0,startIndex));
        }
        Page<JournalVO> page = journalMapper.getJournalsByUid(journalPageQueryDTO);

        List<JournalVO> result = page.getResult();
        long total  = page.getTotal();

        return new PageResult(total,result);


    }

    /**
     * 获取日记的图片
     * @param uid 用户uid
     * @param journalId 要获取图片的日记id
     * @return 返回集合
     */
    @Override
    public List<PictureResult> getJournalPictures(Integer uid, int journalId) {
        //判断日记是不是自己的 不是自己的返回错误信息
        JournalVO journal = journalMapper.getJournalByJID2(uid, journalId);
        if (journal == null) {
            throw new GetPicturesFailedException(MessageConstant.NO_JOURNAL_FOUND);
        }
        //日记是自己的 获取日记的图片信息
        return journalMapper.getJournalPictures(journalId);
    }

    @Override
    public void uploadJournalPicture(int uid,int journalId, MultipartFile file) {
        //1.先判断日记是不是属于自己
        JournalVO journal = journalMapper.getJournalByJID2(uid, journalId);
        if (journal == null) {
            throw new UploadJournalPictureFailedException(MessageConstant.NO_JOURNAL_FOUND);
        }
        //2.日记属于自己 再判断该日记的图片有没有超过9个
        List<PictureResult> journalPictures = journalMapper.getJournalPictures(journalId);
        int size = journalPictures.size();
        if (size > 9) {
            throw new UploadJournalPictureFailedException(MessageConstant.PICTURE_EXCEEDED);
        }
        //3.日记图片没超过九个 开始上传
            //获取原始文件名 并获取文件拓展名 并且用uuid拼接
            String originalFilename = file.getOriginalFilename();
            String extension = Objects.requireNonNull(originalFilename).substring(originalFilename.lastIndexOf("."));
            String objectName = UUID.randomUUID() + extension;
            //若扩展名不是 jpg 或 png
            if (!(extension.equals(".jpg") || extension.equals(".png"))) {
                throw new UploadJournalPictureFailedException(MessageConstant.UPLOAD_FILE_UNMATCHED);
            }
            //上传文件
            try {
                String filePath = aliOssUtil.upload(file.getBytes(), objectName);
                //上传成功 数据库中新增URL
                journalMapper.uploadJournalPicture(journalId,filePath);

            } catch (Exception e) {
                //上传失败
                throw new UploadJournalPictureFailedException(MessageConstant.UPLOAD_FAILED);
            }
    }
}