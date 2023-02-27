package com.itheima.mywxserver.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class NetUtils {

	/**
	 * 发送get请求，获取指定url地址的资源数据
	 */
	public static String getNetDatas(String urlStr) {
		StringBuilder sb = new StringBuilder();
		try {
			// 1. 创建url对象
			URL url = new URL(urlStr);
			// 2. 创建url网络连接对象，连接url指定的资源路径
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			// 3. 建立连接
			con.connect();
			// 4. 获取当前url指定的资源结果数据
			InputStream is = con.getInputStream();

			int size = is.available();
			byte[] bs = new byte[size];
			int len = -1;

			//5. 将读取到的结果数据封装到字符串对象中
			while ((len = is.read(bs)) != -1) {
				sb.append(new String(bs, 0, len));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 发送post请求，请求指定的url路径，传递请求参数，接收处理结果
	 */
	public static String  sendPostRequest(String urlPath ,String data) throws IOException {

		//1. 创建URL对象
		URL url = new URL(urlPath);

		//2. 创建yrl网络链接对象，和url资源路径指定的服务器建立连接
		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		//3. 发送数据，设置数据为可发送的状态
		con.setDoOutput(true);

		//4. 获取输出流，传递自定义菜单数据，发送给微信公众号服务器
		OutputStream os = con.getOutputStream();
		//5. 写自定义菜单的数据 ，发送给微信公众号服务器
		os.write(data.getBytes());

		//6. 关闭输出流（释放资源）
		os.close();

		//7. 接收微信公众号返回的处理结果
		InputStream is = con.getInputStream();

		int size = is.available();
		byte[] bs = new byte[size];
		int len = -1;

		StringBuilder sb = new StringBuilder();
		//5. 将读取到的结果数据封装到字符串对象中
		while ((len = is.read(bs)) != -1) {
			sb.append(new String(bs, 0, len));
		}

		//6. 返回post请求，响应的结果数据
		return sb.toString();

	}




}
