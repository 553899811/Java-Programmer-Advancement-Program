package net.shopin.job;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * <p>ClassName:TestSerialJob</p>
 * <p>Description:	</p>
 * <p>Company: www.shopin.net</p>
 *
 * @author zhangyong@shopin.cn
 * @version 1.0
 * @date 2018/4/10 10:28
 */
@Component
@EnableScheduling
public class TestSerialJob {

    @Scheduled(fixedRate = 3 * 1000)
    public void test() {
        System.out.println("串行JOB-1-现在的时间是:" + System.currentTimeMillis());
        /**
         * 为了测试串行JOB是一个线程按照顺序执行的,让线程sleep 2s ,之后再执行,看下一个job的打印时间;
         */
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Scheduled(fixedRate = 3 * 1000)
    public void test2() {
        System.out.println("串行JOB-2-现在的时间是:" + System.currentTimeMillis());
    }

}
