package edu.twt.rehuixiangshudong.zoo.task;

import edu.twt.rehuixiangshudong.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@Slf4j
public class AddedTask {
    @Autowired
    private UserMapper userMapper;
    /**
     * 每 30 分钟执行一次
     * 更新所有用户的字数
     */
    @Scheduled(cron = "0 0/30 * * * ?")
    public void updateTextCount() throws InterruptedException {
        //获取数据库中现有的uid
        List<Integer> currentAllUids = userMapper.getCurrentAllUids();
        //遍历每个uid 并且为他们更新字数
        try {
            for (int uid : currentAllUids) {
                int userTextCount = userMapper.getUserTextCount(uid);
                userMapper.setUserTextCount(userTextCount,uid);
            }
            log.info("更新所有用户字数成功！");
        } catch (Exception e) {
            log.error("更新字数失败！");
        }
    }

    /**
     * 每天执行一次
     * 更新用户记录天数
     * cron表达式：每天 凌晨2:00
     */
    @Scheduled(cron = "0 0 2 1/1 * ?")
    public void updateWriteDays() throws InterruptedException {
        //获取数据库中现有的uid
        List<Integer> currentAllUids = userMapper.getCurrentAllUids();
        //遍历每个uid 并且为他们更新记录天数
        try {
            for (int uid : currentAllUids) {
                int userWriteDays = userMapper.getUserWriteDays(uid);
                userMapper.setUserWriteDays(userWriteDays,uid);
            }
            log.info("更新所有用户记录天数成功！");
        } catch (Exception e) {
            log.error("更新记录天数失败！");
        }
    }

    /**
     * 每5分钟执行一次
     */
//    @Scheduled(cron = "0 0/3 * * * ?")
    public void ttt() {

    }
}
