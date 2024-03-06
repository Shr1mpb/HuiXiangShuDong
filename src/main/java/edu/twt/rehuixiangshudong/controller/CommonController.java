//package edu.twt.rehuixiangshudong.controller;
//
//import edu.twt.rehuixiangshudong.zoo.constant.MessageConstant;
//import edu.twt.rehuixiangshudong.zoo.result.Result;
//import edu.twt.rehuixiangshudong.zoo.util.AliOssUtil;
//import edu.twt.rehuixiangshudong.zoo.util.ThreadLocalUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//import java.io.IOException;
//import java.util.Objects;
//import java.util.UUID;
//
//@RestController
//@RequestMapping
//@Slf4j
//public class CommonController {
//    @Autowired
//    private AliOssUtil aliOssUtil;
//
//    /**
//     * 文件上传
//     * @param file 上传的文件
//     * @return 返回结果
//     */
//    @PostMapping("/upload")
//    public Result<String> upload(MultipartFile file) {
//        Integer uid = ThreadLocalUtil.getCurrentUid();
//        log.info("用户 {} 文件上传: {}",uid,file);
//
//        try {
//            //获取原始文件名 并获取文件拓展名 并且用uuid拼接
//            String originalFilename = file.getOriginalFilename();
//            String extension = Objects.requireNonNull(originalFilename).substring(originalFilename.lastIndexOf("."));
//            String objectName = UUID.randomUUID() + extension;
//            //上传文件
//            String filePath = aliOssUtil.upload(file.getBytes(), objectName);
//            return Result.success(filePath);
//
//        } catch (IOException e) {
//            log.error("文件上传失败！:{}",e.getMessage());
//        }
//
//        return Result.fail(MessageConstant.UPLOAD_FAILED);
//    }
//}
