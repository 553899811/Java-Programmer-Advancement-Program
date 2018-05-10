package net.shopin.job;

import org.springframework.context.annotation.AdviceMode;
import org.springframework.core.Ordered;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * <p>ClassName:TestParallelJob</p>
 * <p>Description:	</p>
 * <p>Company: www.shopin.net</p>
 *
 * @author zhangyong@shopin.cn
 * @version 1.0
 * @date 2018/4/10 10:33
 */
@Component
@EnableScheduling
@EnableAsync(mode = AdviceMode.PROXY, proxyTargetClass = false,
        order = Ordered.HIGHEST_PRECEDENCE
)
public class TestParallelJob {

    @Scheduled(fixedRate = 3 * 1000)
    @Async
    public void test() {
        System.out.println("并行JOB-1-现在的时间是:" + System.currentTimeMillis());
        /**
         * 为了测试并行job,观察第一个job和第二个job 首次打印时间
         */
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Scheduled(fixedRate = 3 * 1000)
    @Async
    public void test2() {
        System.out.println("并行JOB-2-现在的时间是:" + System.currentTimeMillis());
    }
}
