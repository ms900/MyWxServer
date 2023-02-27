package com.itheima.mywxserver.entity;

/**
 * 封装丰富的图文   响应结果数据的
 */
public class WxImgTextMessage {

    private String ToUserName;//接收方帐号（收到的OpenID）
    private String FromUserName;//开发者微信号
    private String CreateTime;//消息创建时间 （整型）
    private String MsgType;//消息类型，图文为news
    private String ArticleCount;//图文消息个数；
    private Articles Articles;//图文消息信息


    @Override
    public String toString() {
        return "WxImgTextMessage{" +
                "ToUserName='" + ToUserName + '\'' +
                ", FromUserName='" + FromUserName + '\'' +
                ", CreateTime='" + CreateTime + '\'' +
                ", MsgType='" + MsgType + '\'' +
                ", ArticleCount='" + ArticleCount + '\'' +
                ", Articles=" + Articles +
                '}';
    }

    public String getToUserName() {
        return ToUserName;
    }

    public void setToUserName(String toUserName) {
        ToUserName = toUserName;
    }

    public String getFromUserName() {
        return FromUserName;
    }

    public void setFromUserName(String fromUserName) {
        FromUserName = fromUserName;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getMsgType() {
        return MsgType;
    }

    public void setMsgType(String msgType) {
        MsgType = msgType;
    }

    public String getArticleCount() {
        return ArticleCount;
    }

    public void setArticleCount(String articleCount) {
        ArticleCount = articleCount;
    }

    public Articles getArticles() {
        return Articles;
    }

    public void setArticles(Articles articles) {
        Articles = articles;
    }
}
