package com.jgp.ljoa.reporter.common;

/**
 * Created by Administrator on 2018/8/13.
 */
/*
作者  SSF
时间   2018/8/13
*/

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;


public class PathUtil {

    public String getWebInfPath() {
        URL url = getClass().getProtectionDomain().getCodeSource()
                .getLocation();
        String path = url.toString();
//        path=path.replace("!","");

        int index = path.indexOf("WEB-INF");

        if (index == -1) {
            index = path.indexOf("classes");
        }

        if (index == -1) {
            index = path.indexOf("bin");
        }

        path = path.substring(0, index);

        if (path.startsWith("zip")) {
            path = path.substring(4);
        } else if (path.startsWith("file")) {
            path = path.substring(6);
        } else if (path.startsWith("jar")) {
            path = path.substring(10);
        }
        try {
            path = URLDecoder.decode(path, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (path.indexOf(":") < 0) {
            path = System.getProperty("file.separator") + path;
        }
        return path;
    }

    public String getWebPath() {
        return new File(this.getWebInfPath()).getParent();
    }

}
