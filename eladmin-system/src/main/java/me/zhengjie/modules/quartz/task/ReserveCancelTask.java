package me.zhengjie.modules.quartz.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.modules.yy.service.ReserveService;
import org.springframework.stereotype.Component;

/**
 * 预约取消任务
 *
 * @author yanjun
 * @date 2021-01-11 15:40
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ReserveCancelTask {

    private final ReserveService reserveService;

    /**
     * 自动取消
     */
    public void run() {
        reserveService.automaticCancel();
    }

}
