package com.jun.weixin.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.el.parser.ParseException;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Http请求封装类<br/>
 * HttpClient使用版本4.3
 * 
 */
public class HttpUtil {
	
	private static final Logger logger = LogManager.getLogger(HttpUtil.class);
	
	private CloseableHttpClient client = HttpClientBuilder.create().build();  
	private static HttpUtil instance = new HttpUtil();
	
	private static final int SOCKET_TIMEOUT = 10000;//10秒超时传输超时
	private static final int CONNECT_TIMEOUT = 30000;//30秒连接超时
	
	private HttpUtil() {
	}
	
	public static HttpUtil getInstance(){
		return instance;
	}
	
	public CloseableHttpClient getClient() {
		return this.client;
	}
	
	/**
	 * 使用GET方式请求数据。
	 * @param uri
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public String doGet(String url) throws ClientProtocolException, IOException {
		HttpGet httpget = null;
		CloseableHttpResponse response = null;
		try {
			logger.info("httpclient do get. url="+url);
			httpget = new HttpGet(url);
			RequestConfig requestConfig = RequestConfig.custom()
					.setConnectTimeout(CONNECT_TIMEOUT)
					.setSocketTimeout(SOCKET_TIMEOUT).build();//设置请求和传输超时时间
			httpget.setConfig(requestConfig);
			// 执行请求
			response = client.execute(httpget);
			// 获取返回数据
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity, "UTF-8");
		} finally {
			try {
				if(response != null) 
					response.close();
			} catch (IOException e) {
			}
		}
	}
	
	/**
	 * 使用GET方式请求数据。
	 * @param uri
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	public byte[] doGetForStream(String url) throws IllegalStateException, IOException {
		HttpGet httpget = null;
		CloseableHttpResponse response = null;
		try {
			logger.info("httpclient do get for binary. url="+url);
			httpget = new HttpGet(url);
			
			// 执行请求
			response = client.execute(httpget);
			// 获取返回数据
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				InputStream is = response.getEntity().getContent();
				return inputStream2Byte(is);
			}
		} finally {
			try {
				if(response != null) 
					response.close();
			} catch (IOException e) {
			}
		}
		return null;
	}
	
	/**
	 * 根据url地址获取内容长度。
	 * @param uri
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public long doGetForContentLength(String url) throws ClientProtocolException, IOException {
		HttpGet httpget = null;
		CloseableHttpResponse response = null;
		try {
			logger.info("httpclient do get for content-length. url="+url);
			httpget = new HttpGet(url);
			RequestConfig requestConfig = RequestConfig.custom()
					.setConnectTimeout(CONNECT_TIMEOUT)
					.setSocketTimeout(SOCKET_TIMEOUT).build();//设置请求和传输超时时间
			httpget.setConfig(requestConfig);
			
			// 执行请求
			response = client.execute(httpget);
			// 获取返回数据
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				return response.getEntity().getContentLength();
			}
		} finally {
			try {
				if(response != null) 
					response.close();
			} catch (IOException e) {
			}
		}
		return 0;
	}
	
	/**
	 * 将InputStream转换成byte数组  
	 * @param in
	 * @return
	 * @throws IOException
	 */
    public static byte[] inputStream2Byte(InputStream in) throws IOException {
    	int bufferSize = 102400;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
        byte[] data = new byte[bufferSize];  
        int count = -1;  
		while ((count = in.read(data)) != -1) {
			outStream.write(data, 0, count);  
		}
        data = null;  
        return outStream.toByteArray();  
    }
	
	/**
	 * 使用Post方式请求数据
	 * @param url 请求数据的Url
	 * @param jsonStr 请求参数
	 * @return
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public String doPost(String url, Map<String, String> params) throws ParseException, IOException{
		return this.doPost(url, params, "UTF-8");
	}
	
	public String doPost(String url, Map<String, String> params, String charset) throws ParseException, IOException{

		// 参数校验
		if(url == null || "".equals(url)) {
			throw new IllegalArgumentException("The url can't be empty!");
		}
		HttpPost httpPost = null;
		CloseableHttpResponse response = null;
		// 组织请求参数
		try {
			logger.info("httpclient do post. url="+url);
			httpPost = new HttpPost(url);
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			if(params != null && params.size() > 0) {
				Set<Entry<String, String>> sets = params.entrySet();
				Iterator<Entry<String, String>> it = sets.iterator();
				while(it.hasNext()) {
					Entry<String, String> entry = it.next();
					NameValuePair nameValue = new BasicNameValuePair(entry.getKey(), entry.getValue());
					paramsList.add(nameValue);
				}
			}
			UrlEncodedFormEntity urlEntity = new UrlEncodedFormEntity(paramsList, charset);
			httpPost.setEntity(urlEntity);
			RequestConfig requestConfig = RequestConfig.custom()
					.setConnectTimeout(CONNECT_TIMEOUT)
					.setSocketTimeout(SOCKET_TIMEOUT).build();//设置请求和传输超时时间
			httpPost.setConfig(requestConfig);
			
			// 执行请求
			response = client.execute(httpPost); 
			// 返回数据
			int statusCode = response.getStatusLine().getStatusCode();
			if(statusCode == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				return EntityUtils.toString(entity, charset);
			}
		} finally {
			try {
				if(response != null) 
					response.close();
			} catch (IOException e) {
			}
		}
		return null;
	
	}
	
	public String doPost(String url, String param) throws ClientProtocolException, IOException {
		
		CloseableHttpResponse response = null;
		String responseContent = null;
		try {
			HttpPost httppost = new HttpPost(url);
			RequestConfig requestConfig = RequestConfig.custom()
					.setConnectTimeout(CONNECT_TIMEOUT)
					.setSocketTimeout(SOCKET_TIMEOUT).build();//设置请求和传输超时时间
			httppost.setConfig(requestConfig);
			
			StringEntity myEntity = new StringEntity(param, Consts.UTF_8);
			httppost.setEntity(myEntity);
			
			// 执行请求
			response = client.execute(httppost);
			// 返回数据
			HttpEntity entity = response.getEntity();
			responseContent = EntityUtils.toString(entity, Consts.UTF_8);
		} finally {
			try {
				if (response != null) {
					response.close();
				}
			} catch (Exception e) {
				// ignore
			}
		}
		return responseContent;
	}
	
	public JSONObject doPost(File file, String filename, String url) throws ClientProtocolException, IOException {
		HttpPost request = new HttpPost(url);
		HttpResponse response = null;
		try {
			
			MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
			multipartEntityBuilder.setMode(HttpMultipartMode.STRICT);
			multipartEntityBuilder.addBinaryBody("media", file, ContentType.create("application/octet-stream", Consts.UTF_8), filename);
			//multipartEntityBuilder.addBinaryBody("media", file);
			request.setEntity(multipartEntityBuilder.build());
			
			logger.info("httpclient do post, upload file." + url);
			response = client.execute(request);
			final StringBuilder respBuilder = new StringBuilder();
			
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				String respStr = null;
				while (null != (respStr = reader.readLine())) {
					respBuilder.append(respStr);
				}
				return JSON.parseObject(respBuilder.toString());
			}
		}  finally {
			request.releaseConnection();
		}
		return null;
	}
	
	public File downloadFromUrl(String url, String dir) {
		File file = null;
		try {  
            URL httpurl = new URL(url);  
            String fileName = this.getFileNameFromUrl(url);  
            
            file = new File(dir + fileName);  
            
            FileUtils.copyURLToFile(httpurl, file);  
        } catch (Exception e) {
        	logger.error("", e);
        }   
        return file;  
    }
	
	public String getFileNameFromUrl(String url) {
		String name = new Long(System.currentTimeMillis()).toString() + Math.random() + ".X";  
        int index = url.lastIndexOf("/");  
        if(index > 0){  
            name = url.substring(index + 1);  
            if(name.trim().length()>0){  
                return name;  
            }  
        }  
        return name;  
    }  
}
