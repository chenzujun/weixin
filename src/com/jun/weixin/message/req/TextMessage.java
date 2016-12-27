package com.jun.weixin.message.req;

import com.jun.weixin.message.base.BaseMessage;

public class TextMessage extends BaseMessage {
    // 消息内容
    private String Content;

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}
