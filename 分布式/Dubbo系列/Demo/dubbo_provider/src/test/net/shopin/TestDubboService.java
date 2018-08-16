package net.shopin;

import com.alibaba.dubbo.config.annotation.Reference;
import net.shopin.bean.User;
import net.shopin.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = DubboProviderApplication.class)
public class TestDubboService {

    @Reference(version = "1.0.0",group = "userService")
    private UserService userService;

    @Test
    public void test() {
        List<User> users = userService.selectUserList();
        System.out.println(users);
    }
}
