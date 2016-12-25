package com.jun.weixin.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.alibaba.fastjson.util.IOUtils;

public class FileUtil {
    public static byte[] transFile2Bytes(File file) {
	byte[] buffer = null;
	FileInputStream fis = null;
	ByteArrayOutputStream bos = null;
	try {
	    fis = new FileInputStream(file);
	    bos = new ByteArrayOutputStream(1000);
	    byte[] b = new byte[1000];
	    int n;
	    while ((n = fis.read(b)) != -1) {
		bos.write(b, 0, n);
	    }
	    fis.close();
	    bos.close();
	    buffer = bos.toByteArray();
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	} finally {
	    try {
		if (null != fis) {
		    fis.close();
		    fis = null;
		}
	    } catch (Exception e2) {
		e2.printStackTrace();
	    }
	    try {
		if (null != bos) {
		    bos.close();
		    bos = null;
		}
	    } catch (Exception e2) {
		e2.printStackTrace();
	    }
	}
	return buffer;
    }

    public static void TransBytes2File(File file, byte[] bytes) {
	BufferedOutputStream bos = null;
	FileOutputStream fos = null;
	try {
	    File dir = file.getParentFile();
	    if (!dir.exists() && dir.isDirectory()) {// �ж��ļ�Ŀ¼�Ƿ����
		dir.mkdirs();
	    }
	    if (!file.exists()) {
		file.createNewFile();
	    }
	    fos = new FileOutputStream(file);
	    bos = new BufferedOutputStream(fos);
	    bos.write(bytes);
	    bos.flush();
	    fos.flush();
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    if (bos != null) {
		try {
		    bos.close();
		    bos = null;
		} catch (IOException e1) {
		    e1.printStackTrace();
		}
	    }
	    if (fos != null) {
		try {
		    fos.close();
		    fos = null;
		} catch (IOException e1) {
		    e1.printStackTrace();
		}
	    }
	}
    }

    public static void saveToFile(String destUrl, String filePath) {
	FileOutputStream fos = null;
	BufferedInputStream bis = null;
	HttpURLConnection httpUrl = null;
	URL url = null;
	int BUFFER_SIZE = 1024;
	byte[] buf = new byte[BUFFER_SIZE];
	int size = 0;
	try {
	    url = new URL(destUrl);
	    httpUrl = (HttpURLConnection) url.openConnection();
	    httpUrl.connect();
	    bis = new BufferedInputStream(httpUrl.getInputStream());
	    fos = new FileOutputStream(filePath);
	    while ((size = bis.read(buf)) != -1) {
		fos.write(buf, 0, size);
	    }
	    fos.flush();
	} catch (IOException e) {
	} catch (ClassCastException e) {
	} finally {
	    try {
		fos.close();
		bis.close();
		httpUrl.disconnect();
	    } catch (IOException e) {
		e.printStackTrace();
	    } catch (NullPointerException e) {
		e.printStackTrace();
	    }
	}
    }
}
