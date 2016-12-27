package com.jun.weixin.message.resp;

import com.jun.weixin.message.base.BaseMessage;
import com.jun.weixin.message.base.Video;

public class VideoMessage extends BaseMessage {
    // 视频
    private Video Video;

    public Video getVideo() {
        return Video;
    }

    public void setVideo(Video video) {
        Video = video;
    }
}