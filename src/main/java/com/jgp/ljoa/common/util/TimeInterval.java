package com.jgp.ljoa.common.util;/**
 * Created by Administrator on 2018/10/15.
 */

import java.time.Duration;

/**
 * 项目   bank
 * 作者   liujinxu
 * 时间   2018/10/15
 */
public class TimeInterval {
    public static String timeInterval(Duration duration){
        long l = duration.toMillis();//毫秒
        l = Math.abs(l / 1000);//秒
        int hours = (int) l / (60 * 60);//小时
        int second1 = (int) l % (60 * 60);
        int minutes = second1 / 60;
        int second2 = second1 % 60;
        return hours + "小时" + minutes + "分钟" + second2 + "秒";
    }
}
