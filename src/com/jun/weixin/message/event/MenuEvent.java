package com.jun.weixin.message.event;

/**
 * 
 * @类描述：自定义菜单事件
 * @创建人：chenjun
 * @修改人：chenjun
 * @修改时间：2016年12月27日
 * @修改备注：
 */
public class MenuEvent extends BaseEvent {
    // 事件KEY值，与自定义菜单接口中KEY值对应
    private String EventKey;

    public String getEventKey() {
        return EventKey;
    }

    public void setEventKey(String eventKey) {
        EventKey = eventKey;
    }
}