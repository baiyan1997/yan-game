package cn.baiyan.thread;


import cn.baiyan.db.DbService;
import cn.baiyan.db.utils.DbUtils;
import cn.baiyan.game.map.LoginManager;
import cn.baiyan.game.module.user.PlayerEnt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.*;

/**
 * 执行内容
 *
 * @author admin_Hzw
 */
class Task extends TimerTask {

    private static Logger logger = LoggerFactory.getLogger(TimerManager.class);

    public void run() {
        try {
            boolean b = DbUtils.executeSql("update playerent set energy = 100 where 1=1");
            logger.info("day reset energy success" + b);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


/**
 * 任务管理
 *
 * @author admin_Hzw
 */
public class TimerManager {

    private static Logger logger = LoggerFactory.getLogger(LoginManager.class);

    //时间间隔(一天)
    private static final long PERIOD_DAY = 24 * 60 * 60 * 1000;

    public TimerManager() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0); //凌晨1点
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date date = calendar.getTime(); //第一次执行定时任务的时间
        //如果第一次执行定时任务的时间 小于当前的时间
        //此时要在 第一次执行定时任务的时间加一天，以便此任务在下个时间点执行。如果不加一天，任务会立即执行。
        if (date.before(new Date())) {
            date = this.addDay(date, 1);
        }
        Timer timer = new Timer();
        Task task = new Task();
        //安排指定的任务在指定的时间开始进行重复的固定延迟执行。
        timer.schedule(task, date, PERIOD_DAY);
    }

    // firstTime--这是首次该任务将被执行的时间，即便设置时间为凌晨1点执行，如果不
    // 加一天，任务会立即执行的话，那么下次执行的时刻是在距此次执行任务时刻的24小 //时后执行，如果现在是14点执行了一次，那么明天14点才会执行第二次，而不是在
    //凌晨1点执行
    // 增加或减少天数
    public Date addDay(Date date, int num) {
        Calendar startDT = Calendar.getInstance();
        startDT.setTime(date);
        startDT.add(Calendar.DAY_OF_MONTH, num);
        return startDT.getTime();
    }

}
