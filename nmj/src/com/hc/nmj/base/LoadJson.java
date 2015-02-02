package com.hc.nmj.base;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.apache.http.client.ClientProtocolException;

import android.os.Environment;

public class LoadJson {

	// json文件保存路径
	private final static String ALBUM_PATH = Environment
			.getExternalStorageDirectory() + "/download_test/";

	public String getJson(String FileUrl) {

		// 把json文件地址正则转为文件名
		final String subUrl = FileUrl.replaceAll("[^\\w]", "");
		if (isFileExist(subUrl)) {//
			return getDiskFile(FileUrl);
		} else {
			try {
				String jsonstring = GetNetData.getResultForHttpGet(FileUrl);
				FileOutputStream outStream = new FileOutputStream(ALBUM_PATH
						+ subUrl, true);
				OutputStreamWriter writer = new OutputStreamWriter(outStream,
						"UTF-8");
				writer.write(jsonstring);
				writer.write("/n");
				writer.flush();
				writer.close();// 记得关闭
				outStream.close();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	// 检查文件是否存在
	public boolean isFileExist(String FileName) {

		File myFile = new File(ALBUM_PATH + FileName);

		if (!myFile.exists()) {
			return false;
		}
		return true;

	}

	// 从sd卡读取成字符串
	private String getDiskFile(String pathString) {
		String jsonStr = null;
		try {
			File file = new File(ALBUM_PATH + pathString);
			if (file.exists()) {
				StringBuffer sb = new StringBuffer();
				BufferedReader br = new BufferedReader(new FileReader(file));
				String line = "";
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
				br.close();
				jsonStr = sb.toString();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return jsonStr;
	}
}
