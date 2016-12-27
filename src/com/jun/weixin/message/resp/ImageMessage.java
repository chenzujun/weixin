package com.jun.weixin.message.resp;

import com.jun.weixin.message.base.BaseMessage;
import com.jun.weixin.message.base.Image;

public class ImageMessage extends BaseMessage {
    
    private Image Image;

    public Image getImage() {
        return Image;
    }

    public void setImage(Image image) {
        Image = image;
    }
}