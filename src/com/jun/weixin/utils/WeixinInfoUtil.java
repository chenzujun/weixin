package com.jun.weixin.utils;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.jun.weixin.utils.wxaes.AESConstant;
import com.jun.weixin.utils.wxaes.AesException;
import com.jun.weixin.utils.wxaes.WXBizMsgCrypt;

/**
 * 微信相关方法
 * 
 * @author VeganChen
 */
public class WeixinInfoUtil {
    private static final Logger log = LogManager.getLogger(WeixinInfoUtil.class);

    private static String create_nonce_str() {
	return UUID.randomUUID().toString();
    }

    private static String create_timestamp() {
	return Long.toString((System.currentTimeMillis()) / 1000);
    }

    /**
     * 验证回调url
     * 
     * @param msgSignature
     * @param timeStamp
     * @param nonce
     * @param echoStr
     * @return
     * @throws AesException
     */
    public static String verifyURL(String msgSignature, String timeStamp,
	    String nonce, String echoStr, String corpId) throws AesException {

	WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(AESConstant.AESTOKEN,
		AESConstant.ENCODING_AESKEY, corpId);

	String result = wxcpt
		.VerifyURL(msgSignature, timeStamp, nonce, echoStr);

	return result;
    }

    /**
     * 将微信密文解密成明文
     * 
     * @param msgSignature
     * @param timeStamp
     * @param nonce
     * @param echoStr
     * @return
     * @throws AesException
     */
    public static String decryptMsg(String msgSignature, String timeStamp,
	    String nonce, String echoStr, String suiteId) throws AesException {

	WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(AESConstant.AESTOKEN,
		AESConstant.ENCODING_AESKEY, suiteId);

	String result = wxcpt.DecryptMsg(msgSignature, timeStamp, nonce,
		echoStr);

	return result;
    }

    /**
     * 对明文加密返回微信密文
     * 
     * @param sRespData
     * @return
     * @throws AesException
     */
    public static String encryptMsg(String sRespData, String corpId)
	    throws AesException {
	WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(AESConstant.AESTOKEN,
		AESConstant.ENCODING_AESKEY, corpId);
	String nonce_str = create_nonce_str();
	String timestamp = create_timestamp();
	String result = wxcpt.EncryptMsg(sRespData, timestamp, nonce_str);
	return result;
    }

    /**
     * 从解析出的xml明文获取所需的参数数据
     * 
     * @param data
     * @param params
     * @return
     */
    public static Map<String, String> getParam(String sMsg, String[] params) {
	Map<String, String> result = new HashMap<String, String>();
	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

	try {
	    DocumentBuilder db = dbf.newDocumentBuilder();
	    StringReader sr = new StringReader(sMsg);
	    InputSource is = new InputSource(sr);
	    Document document = db.parse(is);

	    Element root = document.getDocumentElement();
	    for (int i = 0; i < params.length; i++) {
		NodeList nodelist1 = root.getElementsByTagName(params[i]);
		if (nodelist1.getLength() > 0) {
		    String value = nodelist1.item(0).getTextContent();
		    result.put(params[i], value);
		}
	    }
	} catch (Exception e) {
	    log.error("", e);
	}

	return result;
    }

}
