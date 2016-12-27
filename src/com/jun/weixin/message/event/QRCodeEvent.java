package com.jun.weixin.message.event;

/**
 * 
 * @类描述：扫描带参数二维码事件
 * @创建人：chenjun
 * @修改人：chenjun
 * @修改时间：2016年12月27日
 * @修改备注：
 */
public class QRCodeEvent extends BaseEvent {
    // 事件KEY值
    private String EventKey;
    // 用于换取二维码图片
    private String Ticket;

    public String getEventKey() {
        return EventKey;
    }

    public void setEventKey(String eventKey) {
        EventKey = eventKey;
    }

    public String getTicket() {
        return Ticket;
    }

    public void setTicket(String ticket) {
        Ticket = ticket;
    }
}