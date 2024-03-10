package edu.twt.rehuixiangshudong.service;

import edu.twt.rehuixiangshudong.zoo.dto.JournalDTO;
import edu.twt.rehuixiangshudong.zoo.dto.JournalPageQueryDTO;
import edu.twt.rehuixiangshudong.zoo.result.PageResult;
import edu.twt.rehuixiangshudong.zoo.result.PictureResult;
import edu.twt.rehuixiangshudong.zoo.vo.JournalVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface JournalService {
    /**
     * 创建日记
     * @param journalDTO 传输日记的模版
     */
    void createJournal(JournalDTO journalDTO);

    /**
     * 修改日记信息 为空的内容不会更改
     * @param journalDTO 传输日记的信息
     */
    void modifyJournal(JournalDTO journalDTO);

    /**
     * 根据日记id获取日记信息
     * @param journalDTO 传输日记id和uid
     * @return journalVO对象
     */
    JournalVO getJournalByJID(JournalDTO journalDTO);

    /**
     * 在日记串中创建日记
     * @param journalDTO 传输日记的相关信息
     */
    void createJournalAtJournalGroup(JournalDTO journalDTO);

    /**
     * 分页查询日记信息
     * @param journalPageQueryDTO 查询名称 页码 每页记录数
     * @return 返回PageResult结果
     */
    PageResult getJournalsByUid(JournalPageQueryDTO journalPageQueryDTO);

    /**
     * 获取日记的图片
     * @param uid 用户uid
     * @param journalId 要获取图片的日记id
     * @return 返回集合
     */
    List<PictureResult> getJournalPictures(Integer uid, int journalId);

    /**
     * 上传日记图片
     * @param journalId 要上传日记图片的日记的id
     * @param file 图片文件
     */
    void uploadJournalPicture(int uid,int journalId, MultipartFile file);

    /**
     * 删除日记图片
     * @param uid 用户uid
     * @param journalId 日记id
     * @param pictureId 图片id
     */
    void deleteJournalPicture(Integer uid, int journalId, int pictureId);
}
