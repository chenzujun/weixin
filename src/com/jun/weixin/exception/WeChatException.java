package com.jun.weixin.exception;

public class WeChatException extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	private Integer code = -1;
	
	public WeChatException(String msg){
		super(msg);
	}

	public WeChatException(Integer code, String msg){
		super(msg);
		this.code = code;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}
	
}
