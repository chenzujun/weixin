package com.jun.weixin.message.resp;

import com.jun.weixin.message.base.BaseMessage;
import com.jun.weixin.message.base.Music;

public class MusicMessage extends BaseMessage {
    // 音乐
    private Music Music;

    public Music getMusic() {
        return Music;
    }

    public void setMusic(Music music) {
        Music = music;
    }
}