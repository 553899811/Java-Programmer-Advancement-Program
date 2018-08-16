package net.shopin.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import net.shopin.bean.User;
import net.shopin.service.UserService;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>ClassName:UserServiceImpl</p>
 * <p>Description:	</p>
 * <p>Company: www.shopin.net</p>
 *
 * @author zhangyong@shopin.cn
 * @version 1.0
 * @date 2018/6/6 11:50
 */
@Service(timeout = 1000, group = "userService",version = "1.0.0")
public class UserServiceImpl implements UserService {

    @Override
    public List<User> selectUserList() {
        ArrayList<User> arrayList = new ArrayList<User>();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setAge(i + 10);
            user.setUsername("Hello" + i);
            arrayList.add(user);
        }
        return arrayList;
    }
}
