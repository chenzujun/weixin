package com.jun.weixin.api;

/**
 * 
 * @类描述：发送消息服务
 * @创建人：chenjun
 * @修改人：chenjun
 * @修改时间：2016年12月27日
 * @修改备注：
 */
public interface SendMessageApi {

    public boolean sendTextMessage(String touser, String countent);
    
    public boolean sendImageMessage();
    
    public boolean sendNewsMessage();
	
}
