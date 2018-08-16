package net.shopin.service;

import net.shopin.bean.Item;
/**
 * <p>ClassName:ItemService</p>
 * <p>Description:	</p>
 * <p>Company: www.shopin.net</p>
 *
 * @author zhangyong@shopin.cn
 * @version 1.0
 * @date 2018/6/11 14:33
 */
public interface ItemService {

    public Item getItemById(long id);

    public Item getItemByName(String name);
}
