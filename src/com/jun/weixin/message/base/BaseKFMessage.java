package com.jun.weixin.message.base;

public class BaseKFMessage {
    private String touser;//OPENID
    private String msgtype;
    
    public String getTouser() {
        return touser;
    }
    public void setTouser(String touser) {
        this.touser = touser;
    }
    public String getMsgtype() {
        return msgtype;
    }
    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }
    
}
