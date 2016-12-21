package com.joytouch.superlive.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

public class DownLoadUtils {

	private int id;
	private long fileLength;
	private long downloadLength;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getFileLength() {
		return fileLength;
	}

	public long getDownloadLength() {
		return downloadLength;
	}

	public boolean download(String path, String name, String urlDownload) {

		OutputStream os = null;
		InputStream is = null;
		try {

			File file = new File(path);
			file.mkdirs();

			file = new File(path, name);
			if (file.exists()) {
				file.delete();
			}
			file = new File(path, name + ".tmp");
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();

			URL url = new URL(urlDownload);
			URLConnection con = url.openConnection();
			is = con.getInputStream();
			fileLength = con.getContentLength();
			
			Map<String, List<String>> map = con.getHeaderFields();
			
			
//			for(String key:map.keySet()){
//				List<String> list = map.get(key);
//				for(String values:list){
//					Log.e(key, values);
//				}
//				
//			}
			
			// 1K的数据缓冲
			byte[] bs = new byte[1024];
			// 读取到的数据长度
			int len;
			// 输出的文件流
			os = new FileOutputStream(file);

			downloadLength = 0;
			// 开始读取
			while ((len = is.read(bs)) != -1) {
				os.write(bs, 0, len);

				downloadLength += len;
				// Log.e("download", d + "/" + contentLength);
			}

			file.renameTo(new File(path, name));

			return true;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} finally {
			// 完毕，关闭所有链接
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}

	public static void delete(String path) {
		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}
	}

	public static String getFileType(String urlDownload) {

		try {
			URL url = new URL(urlDownload);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			int ret = conn.getResponseCode();
			if (ret == 200) {
				String fullString = URLDecoder.decode(conn.getURL().toString(),
						"UTF-8");
				String nameString = fullString.substring(
						fullString.lastIndexOf("/") + 1).split("\\?")[0];
				String typeString = nameString.substring(nameString
						.indexOf("."));
				return typeString;
			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
