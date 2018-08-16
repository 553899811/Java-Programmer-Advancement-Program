package net.shopin.bean;

import java.io.Serializable;

/**
 * <p>ClassName:Item</p>
 * <p>Description:	</p>
 * <p>Company: www.shopin.net</p>
 *
 * @author zhangyong@shopin.cn
 * @version 1.0
 * @date 2018/6/11 14:30
 */
public class Item implements Serializable {
    private static final long serialVersionUID = 3789768688362408490L;
    private Long itemId;
    private String itemName;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemId=" + itemId +
                ", itemName='" + itemName + '\'' +
                '}';
    }
}
