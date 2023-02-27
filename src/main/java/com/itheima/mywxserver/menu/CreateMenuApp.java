package com.itheima.mywxserver.menu;

import com.alibaba.fastjson.JSONObject;
import com.itheima.mywxserver.util.NetUtils;
import com.itheima.mywxserver.util.NetUtils;

import java.io.IOException;

public class CreateMenuApp {

    /**
     * 创建自定义菜单
     */
    public static void main(String[] args) throws IOException {
        //1. 获取操作/调用  凭证：access_token
        String accessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wxf66dce5cd9ee1202&secret=2077bc3c61779391b4b36331d4927ced";

        //2. 发送get请求，获取当前url地址提供的返回的响应结果数据
        String netDatas = NetUtils.getNetDatas(accessTokenUrl);
        //{"access_token":"64_B4gYGT_c9ccX1aIRhsjAgSlNMi6yDTS-oMyevNEMIaUYxmb4CY-EWCD3LxDI9FPjOwzUzxuFN3xvO2Dm2V_lkVbYa5wYTFuVrjcx7mz46kKcNfGKDohG-w4r4KYYDAjAGACHE","expires_in":7200}

        //3. 解析JSON响应结果数据，解析出access_token
        JSONObject accessTokenObj = JSONObject.parseObject(netDatas);
        //根据key获取value（字符串类型）
        String access_token = accessTokenObj.getString("access_token");
        System.out.println(access_token);

        //4. 携带此凭据，调用微信公众号提供的高级接口（自定义菜单接口）
        String createMenuUrl = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token="+access_token;

        //5. 向微信公众号服务器指定的创建菜单的地址发起post请求，携带设置好的菜单数据
        //设计公众号的菜单具体的内容
        String menuContent = "  {\n" +
                "   \"button\":\n" +
                "\t [\n" +
                "\t\t  {\n" +
                "\t\t\t\"type\":\"view\",\n" +
                "\t\t\t\"name\":\"黑马旅游网\",\n" +
                "\t\t\t\"url\":\"https://20764lg022.yicp.fun/show.html\"\n" +
                "\t\t  },\n" +
                "\t\t  \n" +
                "\t\t  {\n" +
                "           \"name\":\"友情连接\",\n" +
                "           \"sub_button\":\n" +
                "\t\t\t   [\n" +
                "\t\t\t\t   {\t\n" +
                "\t\t\t\t\t   \"type\":\"view\",\n" +
                "\t\t\t\t\t   \"name\":\"百度一下\",\n" +
                "\t\t\t\t\t   \"url\":\"http://www.baidu.com/\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t   \"type\":\"click\",\n" +
                "\t\t\t\t\t   \"name\":\"公众号功能介绍\",\n" +
                "\t\t\t\t\t   \"key\":\"k1\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t   \"type\":\"click\",\n" +
                "\t\t\t\t\t   \"name\":\"查看作者头像\",\n" +
                "\t\t\t\t\t   \"key\":\"k2\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t   \"type\":\"click\",\n" +
                "\t\t\t\t\t   \"name\":\"作者语音\",\n" +
                "\t\t\t\t\t   \"key\":\"k3\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t   \"type\":\"click\",\n" +
                "\t\t\t\t\t   \"name\":\"搞笑视频\",\n" +
                "\t\t\t\t\t   \"key\":\"k4\"\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t]\n" +
                "\t\t\t}\n" +
                "\t]\t\t\n" +
                " }";
        String createMenuResult = NetUtils.sendPostRequest(createMenuUrl, menuContent);

        System.out.println("创建菜单的结果数据："+createMenuResult);
    }
}
