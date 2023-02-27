package com.itheima.mywxserver.util;

import com.itheima.mywxserver.entity.WxMessage;
import com.thoughtworks.xstream.XStream;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMLUtil {
    /*
    *
     * 实现使用dom4j解析xml数据的工具方法
     * 将xml数据解析出来，并封装到一个map集合中返回
     */
    public static Map<String, String>  xmlToMap(HttpServletRequest request) throws IOException, DocumentException {
        //1.1 创建解析器对象SAXReader
        SAXReader reader = new SAXReader();
        //1.2调用reader方法，将xml格式数据加载到内容中，形成document对象
        Document document = reader.read(request.getInputStream());
        //1.3 获取xml的根节点对象
        Element rootElement = document.getRootElement();
        //1.4 获取根节点中的所有子节点集合对象
        List<Element> elements = rootElement.elements();

        //将接收的消息数据，存储到一个Map集合中
        HashMap<String,String> message = new HashMap<>();

        for (int i = 0; i < elements.size(); i++) {
            //将接收的消息数据，存储到一个Map集合中

            message.put(elements.get(i).getName(),elements.get(i).getText());
        }
        return message;

    }

    /**
     * 实现使用xstream封装xml格式的数据的工具方法
     *  将指定的java对象封装成xml格式的字符串数据
     */
    public static String javaToXml(Map<String ,String> message){
        //2.1 封装响应结果数据
        WxMessage wxMessage = new WxMessage();
        wxMessage.setContent(message.get("Content"));
        wxMessage.setMsgType("text");
        wxMessage.setCreateTime(System.currentTimeMillis()+"");
        wxMessage.setFromUserName(message.get("ToUserName")); //原消息的接收者变成响应数据的发送者
        wxMessage.setToUserName(message.get("FromUserName")); //原消息的发送者变成响应数据的接收者

        //2.2 将回复的结果数据封装成xml格式的数据返回
        XStream xStream = new XStream();

        //自定义xml响应结果数据根标签名称为：xml
        xStream.alias("xml",WxMessage.class);
        String responseXMLData = xStream.toXML(wxMessage);

        return responseXMLData;
    }



























}
