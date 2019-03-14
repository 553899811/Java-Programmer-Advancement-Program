package net.shopin;

import com.alibaba.dubbo.config.annotation.Reference;
import net.shopin.prodetail.service.ISsdProductDetailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * <p>ClassName:  </p>
 * <p>Description: </p>
 * <p>Company: http://www.shopin.net</p>
 *
 * @author zhangyong@shopin.cn
 * @version 1.0.0
 * @date 2018/8/15/015 11:07
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,classes = DubboConsumerApplication.class)
public class TestDubboService {

    @Reference(check = false, timeout = 1000 * 10, group = "materiel-q",version = "1.0.0")
    ISsdProductDetailService ssdProductDetailService;

    @Test
    public void testService(){
        if (ssdProductDetailService!=null){
            ssdProductDetailService.selectCompleteInventoryAreaRecordList(null);
        }else {
            System.out.println("== null");
        }
    }
}
