package com.weiwu.config;


import com.weiwu.service.OrdersService;
import com.weiwu.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Springboot 定时任务配置
 */
@Component
public class OrderJob {

    @Autowired
    private OrdersService ordersService;

    // http://cron.qqe2.com/  表达式书写网站
    @Scheduled(cron = "0 0 0/1 * * ?")
    public void autoCloseOrder() {
        System.out.println("执行定时任务，当前时间为："
                + DateUtil.getCurrentDateString(DateUtil.DATETIME_PATTERN));
        ordersService.closeOrder();
    }
}

/**
 * 定时任务缺点：
 *      订单量可能会很大，使用定时任务修改 有性能问题
 *      不支持集群，一般抽出一台专门的计算机用来执行定时任务
 *      目前修改订单是全表搜索，性能很差
 *
 *      替代方案可以使用MQ技术， 其中有一个技术 叫延时队列、或延迟任务
 *
 *
 */
