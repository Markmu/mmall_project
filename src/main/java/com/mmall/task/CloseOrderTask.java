package com.mmall.task;


import com.mmall.service.IOrderService;
import com.mmall.util.Const;
import com.mmall.util.PropertiesUtil;
import com.mmall.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CloseOrderTask {

    @Autowired
    private IOrderService iOrderService;


//    @Scheduled(cron = "0 0/1 * * * ?")
    public void closeOrderTaskV1() {
        log.info("关闭订单定时任务启动");
        int hours = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour"));
        iOrderService.closeOrders(hours);
        log.info("关闭订单定时任务结束");
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    public void closeOrderTaskV2() {
        log.info("关闭订单定时任务启动");
        Long timeout = Long.parseLong(PropertiesUtil.getProperty("lock.timeout", "5000"));
        Long setnxResult = RedisShardedPoolUtil.setnx(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, String.valueOf(System.currentTimeMillis() + timeout));
        if (setnxResult != null && setnxResult.intValue() == 1) {
            // setnx成功，获取锁
            this.closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        } else {
            log.info("获取分布式锁{}失败", Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        }
        log.info("关闭订单定时任务结束");

    }

    private void closeOrder(String lockName) {
        RedisShardedPoolUtil.expire(lockName, 50);
        log.info("获取 {}, ThreadName:{}", lockName, Thread.currentThread().getName());
        int hours = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour"));
        iOrderService.closeOrders(hours);
        RedisShardedPoolUtil.del(lockName);
        log.info("释放 {}, ThreadName:{}", lockName, Thread.currentThread().getName());
        log.info("--------------------------------------------------------");
    }


}
