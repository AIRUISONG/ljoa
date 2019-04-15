package com.jgp.ljoa.reporter.common;

import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2018/8/13.
 */
/*
作者  SSF
时间   2018/8/13
*/
//float 转大写汉字
public class Num2Rmb {
    private String[] hanArr = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
    private String[] unitArr = { "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟" };
    private String[] unitFen = { "角", "分", "厘"};
    public String toHanStr(double numStr) {

        DecimalFormat decimalFormat = new DecimalFormat("############.###");
        String format = decimalFormat.format(numStr);

        String result = "";
        String[] strs=format.split("\\.");
        String zheng=strs[0];
        String xiao="";
        if(strs.length>=2){
            xiao=strs[1];
        }
        int numLen = zheng.length();
        for (int i = 0; i < numLen; i++) {
            int num = zheng.charAt(i) - 48;
            if (i != numLen - 1 && num != 0) {
                result += hanArr[num] + unitArr[numLen - 2 - i];
            } else {
                result += hanArr[num];
            }
        }
        String min="";
        for(int j=0;j<xiao.length();j++){
            int i = xiao.charAt(j) - 48;
            min+= hanArr[i] + unitFen[j];
        }
        String[] split = result.split("[+(零)]");
        String aaa = split[0];
        for (int j = 1;j <split.length ; j++) {
            if(StringUtils.isNotEmpty(split[j])){
                aaa += "零" + split[j];
            }
        }
        if(!StringUtils.isNotEmpty(xiao)){
            return aaa+"元整";
        }

        return aaa+"元"+min;
    }
    public static void main(String[] args) {
        Num2Rmb nr = new Num2Rmb();
        System.out.println(nr.toHanStr(5050050.21));

    }
}

