package com.itheima.mywxserver.entity;

/**
 * 图文消息组成部分对象
 */
public class Articles {

    Item item;


    @Override
    public String toString() {
        return "Articles{" +
                "item=" + item +
                '}';
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
