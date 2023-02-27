package com.itheima.mywxserver.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * 文件上传
 */
public class UploadResUtil {

    public static void main(String[] args) throws Exception{
        //1. 获取操作/调用  凭证：access_token
        String accessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wxf66dce5cd9ee1202&secret=2077bc3c61779391b4b36331d4927ced";

        //2. 发送get请求，获取当前url地址提供的返回的响应结果数据
        String netDatas = NetUtils.getNetDatas(accessTokenUrl);

        //3. 解析JSON响应结果数据，解析出access_token
        JSONObject accessTokenObj = JSONObject.parseObject(netDatas);
        //根据key获取value（字符串类型）
        String access_token = accessTokenObj.getString("access_token");
        System.out.println(access_token);


        //上传视频资源
        //本地多媒体文件地址
        String file ="D:\\桌面\\weixin_workspace\\gugua.mp4";  //需要修改成自己电脑上的某个视频资源（大小有限制）
        curlFileUpload(access_token,file,"video");

    }

    /**
     * 模拟curl上传本地文件
     */
    public static void curlFileUpload(String access_token, String filePath,String type) throws Exception{
        String mediaUploadUrl = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token="+access_token+"&type="+type;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String result = "";
        try {
            File media = new File(filePath);
            mediaUploadUrl =replaceURLByFile(access_token,media,type,mediaUploadUrl);
            if (mediaUploadUrl!=null){
                HttpPost httpPost = new HttpPost(mediaUploadUrl);
                MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                //上传多媒体文件
                builder.addBinaryBody("media", media);
                HttpEntity entity = builder.build();
                httpPost.setEntity(entity);
                // 执行提交
                HttpResponse response = httpClient.execute(httpPost);
                HttpEntity responseEntity = response.getEntity();
                if (responseEntity != null) {
                    result = EntityUtils.toString(responseEntity, Charset.forName("UTF-8"));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 校验文件类型
     */
    private static String replaceURLByFile(String ACCESS_TOKEN,File media,String type,String mediaUploadUrl) {
        if (media.isFile()){
            //image 校验
            if (type.equals("image")){
                if (!checkFileType(type,getFileSuffix(media))|| getFileSize(media)>10*1024*1024){
                    return null;
                }
            }else if (type.equals("voice")){
                if (!checkFileType(type,getFileSuffix(media)) || getFileSize(media)>2*1024*1024){
                    return null;
                }
            }else if (type.equals("video")){
                if (!"MP4".equalsIgnoreCase(getFileSuffix(media)) || getFileSize(media)>2*1024*1024){
                    return null;
                }
            }else if (type.equals("thumb")){
                if (!"JPG".equalsIgnoreCase(getFileSuffix(media)) || getFileSize(media)>64*1024){
                    return null;
                }
            }else {
                return null;
            }

        }else {
            return null;
        }
        mediaUploadUrl=mediaUploadUrl.replace("ACCESS_TOKEN",ACCESS_TOKEN).replace("TYPE",type);
        return mediaUploadUrl;
    }

    /**
     * 获取文件后缀名
     */
    public static String getFileSuffix(File file) {
        String suffix="";
        String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        return suffix;
    }

    /**
     * 判断文件类型
     */

    public static boolean checkFileType(String type,String mediaType){
        boolean ble = false;
        if (type.equals("image")){
            if (mediaType.equalsIgnoreCase("PNG") || mediaType.equalsIgnoreCase("JPEG") || mediaType.equalsIgnoreCase("JPG") || mediaType.equalsIgnoreCase("GIF")){
                ble=true;
            }
        }else if(mediaType.equalsIgnoreCase("AMR") || mediaType.equalsIgnoreCase("MP3") ){
            ble=true;

        }
        return ble;
    }

    /**
     * 获取文件大小
     */
    public static long getFileSize(File file) {
        if (!file.exists() || !file.isFile()) {
            return -1;
        }
        return file.length();
    }

}

