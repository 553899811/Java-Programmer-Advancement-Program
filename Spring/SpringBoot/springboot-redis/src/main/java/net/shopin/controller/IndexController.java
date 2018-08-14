package net.shopin.controller;

import net.shopin.utils.ShopinRedisTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>ClassName:${CLASS} </p>
 * <p>Description: </p>
 * <p>Company: http://www.shopin.net</p>
 *
 * @author zhangyong@shopin.cn
 * @version 1.0.0
 * @Date 2018/7/3 15:06
 */
@RestController
public class IndexController {

    @Autowired
    ShopinRedisTemplate<String> redisTemplate;

    @RequestMapping("/getIndex")
    @ResponseBody
    public String getIndex() {
        redisTemplate.set("Shopin", "Happy");
        return redisTemplate.get("Shopin");
    }
}
