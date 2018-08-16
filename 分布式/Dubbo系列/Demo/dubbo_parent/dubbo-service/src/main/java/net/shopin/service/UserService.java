package net.shopin.service;

import net.shopin.bean.User;

import java.util.List;

/**
 * <p>ClassName:UserService</p>
 * <p>Description:	</p>
 * <p>Company: www.shopin.net</p>
 *
 * @author zhangyong@shopin.cn
 * @version 1.0
 * @date 2018/6/6 11:50
 */
public interface UserService {
    public List<User> selectUserList();
}
