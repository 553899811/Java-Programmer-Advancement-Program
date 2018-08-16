package net.shopin.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import net.shopin.bean.User;
import net.shopin.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * <p>ClassName:UserController</p>
 * <p>Description:	</p>
 * <p>Company: www.shopin.net</p>
 *
 * @author zhangyong@shopin.cn
 * @version 1.0
 * @date 2018/6/6 14:17
 */
@Controller
public class UserController {

//    @Reference
//    ISsdSupplyService ssdSupplyService;

    @Reference(timeout = 20000000,check = false,group = "userService")
    UserService userService;

/*    @RequestMapping("/selectSupply")
    @ResponseBody
    public List<SsdSupplyInfo> selectSupplyInfo() {
        return ssdSupplyService.queryAllSupplyInfo();
    }*/

    @RequestMapping("/selectUserList")
    @ResponseBody
    public List<User> selectUserList(){
        return userService.selectUserList();
    }
}
