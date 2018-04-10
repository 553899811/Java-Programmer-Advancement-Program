package net.shopin;

import net.shopin.utils.JxRedisTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * <p>ClassName:TestRedis</p>
 * <p>Description:	</p>
 * <p>Company: www.shopin.net</p>
 *
 * @author zhangyong@shopin.cn
 * @version 1.0
 * @date 2018/4/9 17:48
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class TestRedis {

    @Autowired
    JxRedisTemplate<String> redisTemplate;

    @Test
    public void test() {
        redisTemplate.set("2333", "zhang");
        System.out.println(redisTemplate.get("2333"));
    }
}
