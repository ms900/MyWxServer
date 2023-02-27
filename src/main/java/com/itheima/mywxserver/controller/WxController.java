package com.itheima.mywxserver.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.itheima.mywxserver.entity.Articles;
import com.itheima.mywxserver.entity.Item;
import com.itheima.mywxserver.entity.WxImgTextMessage;
import com.itheima.mywxserver.entity.WxMessage;
import com.itheima.mywxserver.util.NetUtils;
import com.itheima.mywxserver.util.SHA1Util;
import com.itheima.mywxserver.util.XMLUtil;
import com.thoughtworks.xstream.XStream;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController //设置当前类是一个处理器类
public class WxController {

       /*
            <xml>
              <ToUserName><![CDATA[toUser]]></ToUserName>
              <FromUserName><![CDATA[fromUser]]></FromUserName>
              <CreateTime>1348831860</CreateTime>
              <MsgType><![CDATA[text]]></MsgType>
              <Content><![CDATA[this is a test]]></Content>
              <MsgId>1234567890123456</MsgId>
              <MsgDataId>xxxx</MsgDataId>
              <Idx>xxxx</Idx>
            </xml>
        */

    //此处理post请求的方法，实现接收处理微信公众号服务器发送的消息
    @PostMapping("/wx")
    public String handleMsg(HttpServletRequest request) throws IOException, DocumentException {  //request对象中封装了xml格式的请求数据
        //使用dom4j解析公众号的发送的信息
        //1.调用工具类解析xml格式的数据
        Map<String, String> message = XMLUtil.xmlToMap(request);

        System.out.println(message);

        //2.判断当前微信公众号服务器发送的数据类型
        if(message.get("MsgType").equals("event") ){

            //进行详细的事件类型判断
            if( message.get("Event").equals("subscribe")){
                //当前公众号被用户关注了/取消关注了
                //被关注时  回复结果数据返回给微信公众号服务器
                //2.1 自定义一个Java类型的图文消息对象
                WxImgTextMessage wxImgTextMessage = new WxImgTextMessage();
                //设置需要返回的图文信息
                wxImgTextMessage.setFromUserName(message.get("ToUserName"));
                wxImgTextMessage.setToUserName(message.get("FromUserName"));
                wxImgTextMessage.setMsgType("news");
                wxImgTextMessage.setCreateTime(System.currentTimeMillis()+"");
                wxImgTextMessage.setArticleCount("1");

                //设置图文消息信息
                Articles articles = new Articles();
                //设置具体的图文信息内容
                Item item = new Item();
                item.setTitle("欢迎大爷，大娘关注本公众号！");//设置   图文消息的标题信息
                item.setDescription("点击查看，本公众号详情介绍！"); //设置   图文消息的描述信息
                //如果想获取一张图片在微信公众号服务器中的地址信息，
                //可以先上传一张图片到公众号，在后台查看生成的图片地址
                item.setPicUrl("http://mmbiz.qpic.cn/mmbiz_jpg/M2mQ9s1WUaXfXtbIH7RNephhc07XYQyNMZJEFxxNG1ibLz6yqkIiaqF977biawB4UIo2qrHibVOUL1CQ4BicUh3tBGA/0");//设置   图文消息的图片链接

                //编写自定义简介页面：用户点击简介，跳转自定义页面
                //如果使用的映射工具是ngrok，此处就直接填写：http://www.baidu.com
                item.setUrl("https://baike.baidu.com/item/%E9%A9%AC%E4%BF%9D%E5%9B%BD/50106525?fr=aladdin"); //设置   图文消息的跳转链接
                articles.setItem(item);
                wxImgTextMessage.setArticles(articles);//设置图片消息的具体内容

                //2.2 使用xStream将图文对象，转换成xml格式，回复数据给微信公众号服务器
                XStream xStream = new XStream();
                //自定义xml响应结果数据根标签名称为：xml
                xStream.alias("xml",WxImgTextMessage.class);
                String responseXMLData = xStream.toXML(wxImgTextMessage);
                return responseXMLData;
            }else if(message.get("EventKey").equals("k1")){ //单击指定的click按钮事件（根据传递过来的key进行区分）
                //用户点击了公众号功能介绍
                String result = "<xml>\n" +
                        "  <ToUserName>"+message.get("FromUserName")+"</ToUserName>\n" +
                        "  <FromUserName>"+message.get("ToUserName")+"</FromUserName>\n" +
                        "  <CreateTime>12345678</CreateTime>\n" +
                        "  <MsgType><![CDATA[text]]></MsgType>\n" +
                        "  <Content>1.查看天气\n2.查看新闻\n3.恶搞图片\n4.手机号归属地查询\n5.QQ号码测凶吉</Content>\n" +
                        "</xml>\n" +
                        "\n";
                return  result;
            }else if(message.get("EventKey").equals("k2")){ //单击指定的click按钮事件（根据传递过来的key进行区分）
                //用户点击了查看头像
                String result = "<xml>\n" +
                        "  <ToUserName>"+message.get("FromUserName")+"</ToUserName>\n" +
                        "  <FromUserName>"+message.get("ToUserName")+"</FromUserName>\n" +
                        "  <CreateTime>12345678</CreateTime>\n" +
                        "  <MsgType><![CDATA[image]]></MsgType>\n" +
                        "  <Image>\n" +
                        "    <MediaId>qNzF32eiQp_HoSqZh_W1_xGF49tq0BAUBjBF1q7KpJbkss7-tnB12MYmeSUV3vWl</MediaId>\n" +
                        "  </Image>\n" +
                        "</xml>\n" +
                        "\n";
                return  result;
            }else if(message.get("EventKey").equals("k3")){ //单击指定的click按钮事件（根据传递过来的key进行区分）
                //用户点击了语音
                String result = "<xml>\n" +
                        "  <ToUserName>"+message.get("FromUserName")+"</ToUserName>\n" +
                        "  <FromUserName>"+message.get("ToUserName")+"</FromUserName>\n" +
                        "  <CreateTime>12345678</CreateTime>\n" +
                        "  <MsgType><![CDATA[voice]]></MsgType>\n" +
                        "  <Voice>\n" +
                        "    <MediaId>qNzF32eiQp_HoSqZh_W1_5Y2nTqI2KQnrMPlvalKRV3MlYHI6dcwWnfnld4QyDLP</MediaId>\n" +
                        "  </Voice>\n" +
                        "</xml>\n" +
                        "\n";
                return  result;
            }else if(message.get("EventKey").equals("k4")){ //单击指定的click按钮事件（根据传递过来的key进行区分）
                //用户点击了视频
                String result = "<xml>\n" +
                        "  <ToUserName>"+message.get("FromUserName")+"</ToUserName>\n" +
                        "  <FromUserName>"+message.get("ToUserName")+"</FromUserName>\n" +
                        "  <CreateTime>12345678</CreateTime>\n" +
                        "  <MsgType><![CDATA[video]]></MsgType>\n" +
                        "  <Video>\n" +
                        "    <MediaId>KA7BvwGBXP5rgJyl5YkyKO-787ZPbKu-5DMlHKPewydZ8iwgF96IYlqDkTpUi0Ax</MediaId>\n" +
                        "    <Title>单身狗</Title>\n" +
                        "    <Description>孤寡叫不停</Description>\n" +
                        "  </Video>\n" +
                        "</xml>\n" +
                        "\n";
                return  result;
            }
        }else{
            //3.当前接收到微信用户的普通消息
            String userMsg = message.get("Content"); //你好
            //3.1 判断当前用户需要进行的操作
            if(userMsg.startsWith("1.")){
                //1.马鞍山   1.南京   1.北京
                String city = userMsg.substring(userMsg.lastIndexOf(".")+1);

                //当前用户需要获取今天的天气信息
                String url = "http://apis.juhe.cn/simpleWeather/query?city="+city+"&key=6ee7a36edd3152fc070cb6d41b00a170";
                //使用网络工具类，向第三方数据提供商，发起获取数据的请求，接收返回的json格式的结果数据
                String datas = NetUtils.getNetDatas(url);

                //解析期望获取的结果
                String weatherResult = "";
                //使用fastJson提供的JSONObject将json字符串数据，转换成java对象
                JSONObject weatherObject = JSONObject.parseObject(datas);
                //获取天气信息：result对应天气信息
                JSONObject resultObject = weatherObject.getJSONObject("result");
                String cityName = resultObject.getString("city");
                //获取具体的天气信息：realtime对应天气信息详情
                JSONObject realtimeObject = resultObject.getJSONObject("realtime");
                String temperature = realtimeObject.getString("temperature");//获取温度
                String info = realtimeObject.getString("info");//获取天气
                String direct = realtimeObject.getString("direct");//获取风向
                String power = realtimeObject.getString("power");//获取风力

                //拼接返回的天气结果
                weatherResult += "城市名称："+cityName +"\n天气："+info +"\n温度："+temperature+"℃\n风向："+direct+power;
                message.put("Content",weatherResult);
            }else if(userMsg.startsWith("2.")){
                //当前用户需要获取今天的新闻头条
                String newsUrl = "http://v.juhe.cn/toutiao/index?type=&page=&page_size=&is_filter=&key=c8fe066ae002d20891fbc48a1783a1ee";

                //使用网络工具NetUtils获取当前地址提供的新闻资讯数据
                String netDatas = NetUtils.getNetDatas(newsUrl);
                //1. 使用fastjson提供的方法，将新闻相关的json字符串数据，转换成java对象
                JSONObject newsObj = JSONObject.parseObject(netDatas);
                //2. 获取result这个key对应的value(json对象类型)
                JSONObject resultObj = newsObj.getJSONObject("result");
                //3. 获取data这个key对应的多条新闻数据（jsonArray对象类型）
                JSONArray jsonArray = resultObj.getJSONArray("data");
                //定义最终封装返回的新闻数据
                String  newsResult = "";
                //4. 循环遍历新闻数组对象，获取并解析  数据中每一条新闻对象
                for (int i = 0; i < 5; i++) {
                    //获取数组中的每一条新闻对象
                    JSONObject news = (JSONObject) jsonArray.get(i);

                    //5. 解析新闻的具体的内容
                    String title = news.getString("title");
                    String date = news.getString("date");
                    String url = news.getString("url");
                    newsResult += "资讯序号："+(i+1)+"\n新闻标题："+title+"\n发布时间："+date+"\n详情链接："+url+"\n\n";
                    //newsResult += "资讯序号："+(i+1)+"\n新闻标题："+title+"\n发布时间："+date+"\n详情链接：<a href='"+url +"'>点击查看</a>\n\n";
                }
                //将拼接完毕的新闻数据封装到message集合中，返回给微信公众号服务器
                message.put("Content",newsResult);
            }else if(userMsg.startsWith("3.")){
                //给用户生成一张恶搞图片
                String username = userMsg.substring(userMsg.lastIndexOf(".")+1);
                System.out.println("username:"+username);
                message.put("Content","<a href='http://121.41.106.137/img?username="+ URLEncoder.encode(username)+"'>查看图片</a>");
            }else if(userMsg.startsWith("4.")){
                String phone = userMsg.substring(userMsg.lastIndexOf(".") + 1);
                String phoneurl = "http://apis.juhe.cn/mobile/get?phone=" + phone + "&dtype=&key=d7396eb64488ba44bb74cc4cc55320a3";
                String phonedatas = NetUtils.getNetDatas(phoneurl);
                String phoneResult = "";

                JSONObject phoneObject = JSONObject.parseObject(phonedatas);

                JSONObject resultObject = phoneObject.getJSONObject("result");
                String provinceName = resultObject.getString("province");
                String cityName = resultObject.getString("city");
                String zipName = resultObject.getString("zip");
                String companyName = resultObject.getString("company");

                phoneResult += "省级名称：" + provinceName + "\n市级名称：" + cityName + "\n邮政编码：" + zipName + "\n运营商：" + companyName;
                message.put("Content", phoneResult);
            }else if(userMsg.startsWith("5.")){

                String qqnum = userMsg.substring(userMsg.lastIndexOf(".") + 1);
                String qqurl = "http://japi.juhe.cn/qqevaluate/qq?qq=" + qqnum + "&key=17ab4d0b17d42adeab0aa8a2ef92dc82";//使用网络工具类，向第三方数据提供商，发起获取数据的请求，接收返回的json格式的结果数据
                String qqdatas = NetUtils.getNetDatas(qqurl);//解析期望获取的结果
                String qqResult = "";//使用fastJson提供的JSONObject将json字符串数据，转换成java对象
                JSONObject qqObject = JSONObject.parseObject(qqdatas);//获取天气信息：result对应qq信息
                JSONObject resultObject = qqObject.getJSONObject("result");//获取具体的qq信息：data对应qq信息详情
                JSONObject dataObject = resultObject.getJSONObject("data");
                String conclusion = dataObject.getString("conclusion");//获取总结
                String analysis = dataObject.getString("analysis");//获取分析
                qqResult += "总结：" + conclusion + "\n分析：" + analysis + "";
                message.put("Content", qqResult);
            }else{
                //如果没有选择获取公众号提供的功能数据，接入智能机器人进行聊天
                //将接收到用户消息，转发给智能聊天小机器人，接收机器人的回复结果数据
                String machineResponse = NetUtils.getNetDatas("http://api.qingyunke.com/api.php?key=free&appid=0&msg="+userMsg);

                // {"result":0,"content":"你猜"}
                //截取需要返回的回复数据
                //1. 将json字符串，转换成java对象
                JSONObject jsonObject = JSONObject.parseObject(machineResponse);

                //2. 获取对象的指定key的属性value值
                String value = jsonObject.getString("content");

                //将机器人的回复结果数据返回给微信公众号服务器
                message.put("Content",value);
            }

            //2. 回复数据给微信公众号服务器
            String responseXMLData = XMLUtil.javaToXml(message);//调用工具类封装xml格式的数据

            return responseXMLData;
        }

        return null;
    }


    //此处理get请求的方法，实现自定义的web项目和微信公众号服务器进行接入操作
    //表示此处理器方法，只处理get请求
    @GetMapping("/wx")
    public String joinWxServer(String signature ,String timestamp,
                                String nonce,String echostr ){
        //1. 接收请求参数（四个）,直接在处理器方法参数列表中定义变量就可以接收请求参数了
        System.out.println("signature:"+signature);
        System.out.println("timestamp:"+timestamp);
        System.out.println("nonce:"+nonce);
        System.out.println("echostr:"+echostr);

        //2. 接入的具体操作
        //2.1将token、timestamp、nonce三个参数进行字典序排序
        String token = "mashuan123";  //需要和接入界面中定义的token内容保持一致

        //定义参数数组，方便进行排序
        String [] params = {token,timestamp,nonce};

        //调用数据工具类，进行排序
        Arrays.sort(params);

        //2.2将三个参数字符串拼接成一个字符串进行sha1加密
        String param = params[0]+params[1]+params[2];
        String sha1Result = SHA1Util.toSha1(param);

        //2.3开发者获得加密后的字符串可与 signature 对比，标识该请求来源于微信
        if(sha1Result.equalsIgnoreCase(signature)){
            //接入成功，直接返回echostr
            return echostr;
        }

        return  null;
    }
}
