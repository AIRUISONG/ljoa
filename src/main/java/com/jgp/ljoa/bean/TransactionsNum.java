package com.jgp.ljoa.bean;

/**
 * Created by Administrator on 2018/8/15 0015.
 */
public class TransactionsNum implements Comparable<TransactionsNum>{
    //区
    private String area;
    //房源数量
    private Integer num;
    //比例
    private Float scale;

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Float getScale() {
        return scale;
    }

    public void setScale(Float scale) {
        this.scale = scale;
    }

    @Override
    public int compareTo(TransactionsNum o) {
        int i=o.getNum()-this.getNum();
        return i;
    }
}
