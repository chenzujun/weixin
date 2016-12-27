package com.jun.weixin.message.event;

/**
 * 
 * @类描述：上报地理位置事件
 * @创建人：chenjun
 * @修改人：chenjun
 * @修改时间：2016年12月27日
 * @修改备注：
 */
public class LocationEvent extends BaseEvent {
    // 地理位置纬度
    private String Latitude;
    // 地理位置经度
    private String Longitude;
    // 地理位置精度
    private String Precision;

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getPrecision() {
        return Precision;
    }

    public void setPrecision(String precision) {
        Precision = precision;
    }
}