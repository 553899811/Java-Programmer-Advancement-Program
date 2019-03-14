package net.shopin.bean;

import java.util.HashMap;

/**
 * <p>ClassName:Solution</p>
 * <p>Description:	</p>
 * <p>Company: www.shopin.net</p>
 *
 * @author zhangyong@shopin.cn
 * @version 1.0
 * @date 2018/6/12 6:25
 */
public class Solution {
    private static HashMap<Integer, String> hashMap = new HashMap<Integer, String>(2, 0.75f);

    public static void main(String[] args) {
        hashMap.put(5, "C");
        new Thread("Thread-1") {
            @Override
            public void run() {
                hashMap.put(7, "B");
                System.out.println(hashMap);
            }
        }.start();
        new Thread("thread-2"){
            @Override
            public void run(){
                hashMap.put(3,"A");
                System.out.println(hashMap);
            }
        }.start();
    }
}
