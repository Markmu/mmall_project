package com.mmall.task;


import com.mmall.service.IOrderService;
import com.mmall.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CloseOrderTask {

    @Autowired
    private IOrderService iOrderService;


    @Scheduled(cron = "0 0/1 * * * ?")
    public void closeOrderTaskV1() {
        log.info("关闭订单定时任务启动");
        int hours = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour"));
        iOrderService.closeOrders(hours);
        log.info("关闭订单定时任务结束");
    }


}
