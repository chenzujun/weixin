package com.jun.weixin.message.resp;

import com.jun.weixin.message.base.BaseMessage;
import com.jun.weixin.message.base.Voice;

public class VoiceMessage extends BaseMessage {
    // 语音
    private Voice Voice;

    public Voice getVoice() {
        return Voice;
    }

    public void setVoice(Voice voice) {
        Voice = voice;
    }
}