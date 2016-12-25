package com.jun.weixin.mediafile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.el.parser.ParseException;
import org.apache.http.client.ClientProtocolException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.jun.weixin.utils.HttpUtil;

public class MediaApi {
	
	private String accessToken;
	
	/** 获取临时文件素材 **/
	private static String MEDIA_GET = "https://api.weixin.qq.com/cgi-bin/media/get?access_token=%s&media_id=%s";
	/** 上传临时文件素材 **/
	private static String MEDIA_UPLOAD = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=%s&type=%s";
	/** 获取素材总数 **/
	private static String MATERIAL_GET_COUNT = "https://api.weixin.qq.com/cgi-bin/material/get_count?access_token=%s&agentid=%s";
	/** 获取素材列表 **/
	private static String MATEIRAL_BATCHGET = "https://api.weixin.qq.com/cgi-bin/material/batchget?access_token=%s";
	
	/** 多媒体文件类型 ***/
	public static final String MEDIA_TYPE_IMAGE = "image";
	public static final String MEDIA_TYPE_VOICE = "voice";
	public static final String MEDIA_TYPE_VIDEO = "video";
	public static final String MEDIA_TYPE_FILE = "file";
	
	/**
	 * 构造函数
	 * @param accessToken
	 */
	public MediaApi(String accessToken) {
		this.accessToken = accessToken;
	}
	
	/**
	 * 获取临时媒体文件
	 * @param mediaId
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public byte[] getMedia(String mediaId) throws IllegalStateException, IOException {
		
		// 请求url地址
		String url = String.format(MEDIA_GET, this.accessToken, mediaId);
		// 请求回来的数据
		byte[] b = HttpUtil.getInstance().doGetForStream(url);
		
		return b;
	}
	
	/**
	 * 获取临时媒体文件
	 * @param fileAbsolutePath
	 * @param mediaId
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public File getMedia(String fileAbsolutePath, String mediaId) throws IllegalStateException, IOException {
		// 得到二进制
		File file = null;
		BufferedOutputStream bufferedOutputStream = null;
		try {
			byte[] bytes = this.getMedia(mediaId);
			
			file = new File(fileAbsolutePath);
			
			// 将二进制写进文件当中
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
			bufferedOutputStream.write(bytes);
			bufferedOutputStream.flush();
		} finally {
			if (bufferedOutputStream != null) {
				try {
					bufferedOutputStream.close();
				} catch (Exception e) {
				}
			}
		}
		
		return file;
	}
	
	/**
	 * 上传临时文件素材
	 * @param file
	 * @param type
	 * @return
	 * @throws ClientProtocolException
	 * @throws JSONException
	 * @throws IOException
	 */
	public JSONObject uploadMedia(File file, String type) throws ClientProtocolException, JSONException, IOException {
		JSONObject json = null;
		// 请求的url地址
		String url = String.format(MEDIA_UPLOAD, this.accessToken, type);
		//
		json = HttpUtil.getInstance().doPost(file, file.getName(), url);
		
		return json;
	}
	
	/**
	 * 上传多媒体图片并返回media_id
	 * @param file
	 * @return
	 * @throws ClientProtocolException
	 * @throws JSONException
	 * @throws IOException
	 */
	public String uploadMediaImage(File file) throws ClientProtocolException, JSONException, IOException {
		// 
		JSONObject json = this.uploadMedia(file, MediaApi.MEDIA_TYPE_IMAGE);
		
		String media_id = json.getString("media_id");
		
		return media_id;
	}
	
	/**
	 * 获取素材总数
	 * 本接口可以获取应用素材总数以及每种类型素材的数目。
	 * http://qydev.weixin.qq.com/wiki/index.php?title=%E8%8E%B7%E5%8F%96%E7%B4%A0%E6%9D%90%E6%80%BB%E6%95%B0
	 * @param agentid
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public JSONObject materialGetCount(int agentid) throws ClientProtocolException, IOException {
		JSONObject json = null;
		// 请求的URL地址
		String url = String.format(MATERIAL_GET_COUNT, this.accessToken, agentid);
		//
		String result = HttpUtil.getInstance().doGet(url);
		
		// 解析成JSON对象
		json = JSON.parseObject(result);
		
		return json;
	}
	
	/**
	 * 获取素材列表 
	 * 本接口可以获取应用素材素材列表。
	 * http://qydev.weixin.qq.com/wiki/index.php?title=%E8%8E%B7%E5%8F%96%E7%B4%A0%E6%9D%90%E5%88%97%E8%A1%A8
	 * @param type
	 * @param agentid
	 * @param offset
	 * @param count
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	public JSONObject materialBatchget(String type, int agentid, int offset, int count) throws ParseException, IOException {
		JSONObject json = null;
		// 请求的URL地址
		String url = String.format(MATEIRAL_BATCHGET, this.accessToken);
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("type", type);
		params.put("agentid", agentid+"");
		params.put("offset", offset+"");
		params.put("count", count+"");
		
		String result = HttpUtil.getInstance().doPost(url, params);
		
		// 解析成JSON对象
		json = JSON.parseObject(result);
		
		return json;
	}
	
}
