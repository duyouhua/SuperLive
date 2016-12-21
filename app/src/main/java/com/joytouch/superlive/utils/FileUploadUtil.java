package com.joytouch.superlive.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.UUID;

public class FileUploadUtil {
	public static String fileUploadBreak="fileUploadBreak";
	
	public static String uploadImage(String urlString,
			Map<String, String> params, Map<String, Bitmap> bmMap)
			throws Exception {
		DataOutputStream dos;
		String  boundary =  UUID.randomUUID().toString();  //边界标识   随机生成
		String twoLine = "--";
		String end = "\r\n";
		URL url = new URL(urlString);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setConnectTimeout(5000);
		conn.setReadTimeout(30000);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setRequestProperty("Charset", "utf-8");
		conn.setRequestProperty("Content-Type",
				"multipart/form-data; boundary=" + boundary);

		dos = new DataOutputStream(conn.getOutputStream());
		StringBuffer sb = new StringBuffer();
		if (params != null) {
			for (String key : params.keySet()) {
				sb.append(end + twoLine + boundary + end);
				sb.append("Content-Disposition:form-data;name=\"").append(key)
						.append("\"" + end);
				sb.append(end);
				sb.append(params.get(key));
			}
		}
		sb.append(end);
		dos.write(sb.toString().getBytes());
		/*
		 * sb.append(end + twoLine + boundary + end);
		 * sb.append("Content-Disposition:form-data;name=\"").append("version")
		 * .append("\"" + end); sb.append(end); sb.append("1");
		 * 
		 * sb.append(end + twoLine + boundary + end);
		 * sb.append("Content-Disposition:form-data;name=\"").append("phone")
		 * .append("\"" + end); sb.append(end); sb.append("1");
		 * 
		 * sb.append(end + twoLine + boundary + end);
		 * sb.append("Content-Disposition:form-data;name=\"").append("action")
		 * .append("\"" + end); sb.append(end); sb.append("2");
		 * 
		 * sb.append(end + twoLine + boundary + end);
		 * sb.append("Content-Disposition:form-data;name=\"").append("str")
		 * .append("\"" + end); sb.append(end); sb.append("M");
		 * 
		 * sb.append(end + twoLine + boundary + end);
		 * sb.append("Content-Disposition:form-data;name=\"").append("token")
		 * .append("\"" + end); sb.append(end);
		 * sb.append("5D1EF6E5-AECC-2FCF-1228-4817BBCFC5DA"); sb.append(end);
		 */
		if (bmMap != null) {
			for (String key : bmMap.keySet()) {

				StringBuffer fileBuffer = new StringBuffer();
				fileBuffer.append(twoLine + boundary + end);
				fileBuffer.append("Content-Disposition:form-data;name=\"")
						.append(key).append("\";").append("filename=\"")
						.append("cc.png").append("\"" + end);
				fileBuffer.append("Content-Type: ").append("image/png")
						.append(end);
				fileBuffer.append(end);
				dos.write(fileBuffer.toString().getBytes());

				/*
				 * dis = new DataInputStream(new FileInputStream(new
				 * File(path))); int lenth = 0; byte[] bytes = new byte[1024];
				 * while ((lenth = dis.read(bytes)) != -1) {
				 * } dis.close();
				 */

				dos.write(bmToByte(bmMap.get(key)));

				dos.write(end.getBytes());
			}
		}
		String string = twoLine + boundary + twoLine + end;
		dos.write(string.getBytes());
		dos.flush();
		dos.close();
		
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			if(conn!=null){
				conn.disconnect();
			}
			return fileUploadBreak;
		}
		/* 读取服务器信息 */
		BufferedReader br = new BufferedReader(new InputStreamReader(
				conn.getInputStream()));
		String getString = null;
		StringBuffer getBuffer = new StringBuffer();
		while ((getString = br.readLine()) != null) {
			getBuffer.append(getString);
			
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				if(conn!=null){
					conn.disconnect();
				}
				return fileUploadBreak;
			}
		}
		br.close();
		if (conn!=null) {
			conn.disconnect();
			conn=null;
		}
		if(getBuffer.length()==0){
			return "";
		}
		return getBuffer.toString();
	}
	public static String uploadImageJpg(String urlString,
			Map<String, String> params, Map<String, Bitmap> bmMap)
					throws Exception {
		DataOutputStream dos;
		String  boundary =  UUID.randomUUID().toString();  //边界标识   随机生成
		String twoLine = "--";
		String end = "\r\n";
		URL url = new URL(urlString);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setConnectTimeout(5000);
		conn.setReadTimeout(30000);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setRequestProperty("Charset", "utf-8");
		conn.setRequestProperty("Content-Type",
				"multipart/form-data; boundary=" + boundary);
		
		dos = new DataOutputStream(conn.getOutputStream());
		StringBuffer sb = new StringBuffer();
		if (params != null) {
			for (String key : params.keySet()) {
				sb.append(end + twoLine + boundary + end);
				sb.append("Content-Disposition:form-data;name=\"").append(key)
				.append("\"" + end);
				sb.append(end);
				sb.append(params.get(key));
			}
		}
		sb.append(end);
		dos.write(sb.toString().getBytes());
		if (bmMap != null) {
			for (String key : bmMap.keySet()) {
				
				StringBuffer fileBuffer = new StringBuffer();
				fileBuffer.append(twoLine + boundary + end);
				fileBuffer.append("Content-Disposition:form-data;name=\"")
				.append(key).append("\";").append("filename=\"")
				.append("aa.jpeg").append("\"" + end);
				fileBuffer.append("Content-Type: ").append("image/jpeg")
				.append(end);
				fileBuffer.append(end);
				dos.write(fileBuffer.toString().getBytes());
				
				dos.write(bmToByteForJpg(bmMap.get(key)));
				
				dos.write(end.getBytes());
			}
		}
		String string = twoLine + boundary + twoLine + end;
		dos.write(string.getBytes());
		dos.flush();
		dos.close();
		
		/* 读取服务器信息 */
		BufferedReader br = new BufferedReader(new InputStreamReader(
				conn.getInputStream()));
		String getString = null;
		StringBuffer getBuffer = new StringBuffer();
		while ((getString = br.readLine()) != null) {
			getBuffer.append(getString);
		}
		br.close();
		if (conn!=null) {
			conn.disconnect();
			conn=null;
		}
		if(getBuffer.length()==0){
			return "";
		}
		return getBuffer.toString();
	}

	public static byte[] bmToByte(Bitmap bm) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bm.compress(CompressFormat.PNG, 100, bos);
		return bos.toByteArray();
	}
	public static byte[] bmToByteForJpg(Bitmap bm) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bm.compress(CompressFormat.JPEG, 60, bos);
		return bos.toByteArray();
	}

	/**
	 * 上传文件
	 * 
	 * @param urlStr
	 * @param textMap
	 * @param fileMap
	 * @return
	 */
	public static String formUpload(String urlStr, Map<String, String> textMap,
			Map<String, String> fileMap) {
		String res = "";
		HttpURLConnection conn = null;
		String BOUNDARY = "---------------------------123821742118716"; // boundary就是request头和上传文件内容的分隔符
		try {
			URL url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(30000);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
			conn.setRequestProperty("Content-Type",
					"multipart/form-data; boundary=" + BOUNDARY);

			OutputStream out = new DataOutputStream(conn.getOutputStream());
			// text
			if (textMap != null) {
				StringBuffer strBuf = new StringBuffer();
				for(String inputName:textMap.keySet()){
					String inputValue = textMap.get(inputName);
					if (inputValue == null) {
						continue;
					}
					strBuf.append("\r\n").append("--").append(BOUNDARY)
							.append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\""
							+ inputName + "\"\r\n\r\n");
					strBuf.append(inputValue);
				}
				out.write(strBuf.toString().getBytes());
			}
			
			

			// file
			if (fileMap != null) {
				for(String inputName:fileMap.keySet()) {
					String inputValue = fileMap.get(inputName);
					if (inputValue == null) {
						continue;
					}
					File file = new File(inputValue);
					String filename = file.getName();
					String contentType = null; 
							//new MimeTypeMap().getContentType(file);
					if (filename.endsWith(".png")) {
						contentType = "image/png";
					}
					if (contentType == null || contentType.equals("")) {
						contentType = "application/octet-stream";
					}

					StringBuffer strBuf = new StringBuffer();
					strBuf.append("\r\n").append("--").append(BOUNDARY)
							.append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\""
							+ inputName + "\"; filename=\"" + filename
							+ "\"\r\n");
					strBuf.append("Content-Type:" + contentType + "\r\n\r\n");

					out.write(strBuf.toString().getBytes());

					DataInputStream in = new DataInputStream(
							new FileInputStream(file));
					int bytes = 0;
					byte[] bufferOut = new byte[1024];
					//int a=0;
					while ((bytes = in.read(bufferOut)) != -1) {
						out.write(bufferOut, 0, bytes);
						//Log.e("fileloadUtil", "图片上传..."+(a++));
						try {
							Thread.sleep(1);
						} catch (InterruptedException e) {
							// TODO: handle exception
						//	Log.e("FileUploadUtil", "interrupted");
							if(in!=null){
								in.close();
							}
							
							if(out!=null){
								out.close();
							}
							if(conn!=null){
								conn.disconnect();
							}
							return fileUploadBreak;
						}
					}
					in.close();
				}
			}

			byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
			out.write(endData);
			out.flush();
			out.close();
			
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO: handle exception
				if(conn!=null){
					conn.disconnect();
				}
				return fileUploadBreak;
			}
			
			// 读取返回数据
			StringBuffer strBuf = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				strBuf.append(line).append("\n");

				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					if(conn!=null){
						conn.disconnect();
					}
					return fileUploadBreak;
				}
			}
			res = strBuf.toString();
			reader.close();
			reader = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
		return res;
	}
	
}
