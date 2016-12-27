package com.jun.weixin.service;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jun.weixin.api.SendMessageApi;
import com.jun.weixin.exception.WeChatException;
import com.jun.weixin.utils.HttpUtil;

/**
 * 
 * @类描述：客服接口-发消息
 * @创建人：chenjun
 * @修改人：chenjun
 * @修改时间：2016年12月28日
 * @修改备注：
 */
public class SendMessageServiceImpl implements SendMessageApi {

    public final String SEND_MESSAGE = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=%s";
    
    private String token;
    
    private HttpUtil httpUtil = HttpUtil.getInstance();
    
    @Override
    public boolean sendTextMessage(String touser, String countent) {
	String strJson = "{\"touser\" :\""+touser+"\",";
        strJson += "\"msgtype\":\"text\",";
        strJson += "\"text\":{";
        strJson += "\"content\":\""+countent+"\"";
        strJson += "}}";
        
        try {
	    return sendMsg(strJson);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return false;
    }

    @Override
    public boolean sendImageMessage() {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public boolean sendNewsMessage() {
	// TODO Auto-generated method stub
	return false;
    }
    
    private boolean sendMsg(String content) throws Exception {
	String url = String.format(SEND_MESSAGE, token);
	
	//http请求
	String response = httpUtil.doPost(url, content);
	
	JSONObject jobj = JSON.parseObject(response);
	if(jobj.getIntValue("errcode") != 0){
		throw new WeChatException(response);
	}
	return true;
    }

    public HttpUtil getHttpUtil() {
        return httpUtil;
    }

    public void setHttpUtil(HttpUtil httpUtil) {
        this.httpUtil = httpUtil;
    }

    public static void main(String[] args) throws Exception {
	HttpUtil httpUtil = HttpUtil.getInstance();
		
	// 获取token
	String token_url ="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx822bd6359065a449&secret=8eb09fc01bdcec95c2246ec751816957";
//	System.out.println(httpUtil.doGet(token_url).toString());
	
	String url = "https://api.weixin.qq.com/customservice/kfaccount/add?access_token=Tlo5fV4MeTuApt7H7S0wdSLjTyeqjLS24lB0Cn3JLx2yNUSvu9eJbSURf8CxIwalgAAyMUyZDPKehozIi95N8c3TqBEvPuadJop0VaHyQnAyMrgF61qNLifVclZm_p-OAVGaADAOJU";
	String strJson = "{\"kf_account\" :\"chen_zujun@163.com\",";
        strJson += "\"nickname\":\"客服\",";
        strJson += "\"password\":\"pswkf1\"";
        strJson += "}";
        
        System.out.println(httpUtil.doPost(url, strJson));
	
    }
}
