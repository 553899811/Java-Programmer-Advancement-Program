/**
 * SerializeUtil.java
 * net.shopin.jx.web.common.util
 * <p>
 * Function： TODO
 * <p>
 * ver     date      		author
 * ──────────────────────────────────
 * 1.0   2016年11月19日  	 wangxiaoming
 * <p>
 * Copyright (c) 2016, TNT All Rights Reserved.
 */

package net.shopin.utils;

import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * <p>ClassName:SerializeUtil</p>
 * <p>Description:	</p>
 * <p>Company: www.shopin.net</p>
 *
 * @author wdg
 * @version 1.0
 * @Date 2017年6月13日下午3:15:09
 */
@Component
public class SerializeUtil<E> {

    public String serialize(E object) {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);

            String str = baos.toString("ISO-8859-1");
            return URLEncoder.encode(str, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                baos.close();
                oos.close();
            } catch (Exception e) {
//				e.printStackTrace();
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public E unserialize(String serializeStr) {
        String readStr = "";
        if (serializeStr == null || "".equals(serializeStr)) {
            return null;
        }
        try {
            readStr = URLDecoder.decode(serializeStr, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        ObjectInputStream ois = null;
        InputStream bais = null;
        try {
            bais = new ByteArrayInputStream(readStr.getBytes("ISO-8859-1"));
            ois = new ObjectInputStream(bais);
            return (E) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ois.close();
                bais.close();
            } catch (Exception e) {
//				e.printStackTrace();
            }
        }
        return null;
    }
}

