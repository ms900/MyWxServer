package com.itheima.mywxserver.menu;

import com.alibaba.fastjson.JSONObject;
import com.itheima.mywxserver.util.NetUtils;

import java.io.IOException;

public class DeleteMenuApp {

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


        //4. 携带此凭据，调用微信公众号提供的高级接口（删除自定义菜单）
        String deleteMenuUrl = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token="+access_token;


        //6. 发起删除自定义菜单的请求
        String netDatas1 = NetUtils.getNetDatas(deleteMenuUrl);

        //7. 打印执行结果，查看是否删除成功
        System.out.println(netDatas1);


    }



}
