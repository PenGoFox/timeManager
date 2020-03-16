package timeManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.json.JSONObject;

public class FileSaver {
	public FileSaver(String filename) {
		this.filename = filename;
		this.setErrorString("");
	}
	
	public boolean save(Table table) {
		FileOutputStream fileOutputStream;
		Writer writer;
		
		try {
			fileOutputStream = new FileOutputStream(this.filename);
			writer = new OutputStreamWriter(fileOutputStream, "utf-8"); // 解决不能写入中文的问题
		} catch (FileNotFoundException e) {
			this.setErrorString("Failed to open the json file!");
			System.out.println("Failed to open the json file!");
			return false;
		} catch (UnsupportedEncodingException e) {
			this.setErrorString("unsupported encoding exception, FileSaver.java:save()");
			System.out.println("unsupported encoding exception, FileSaver.java:save()");
			return false;
		}
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("table", table.getAllPlan());
		
		try {
			writer.write(jsonObject.toString());
		} catch (IOException e) {
			System.out.println("Failed to save to the json file!");
			this.setErrorString("Failed to save to the json file!");
			return false;
		}
		
		try {
			// 必须先关闭writer，再关闭fileOutputStream，否则会有异常
			writer.close();
			fileOutputStream.close();
		} catch (IOException e) {
			System.out.println("Failed to close the json file!");
			this.setErrorString("Failed to close the json file!");
			return false;
		}
		
		return true;
	}
	
	public Map<PlanTime, String> read() {
		Map<PlanTime, String> planMap = new HashMap<PlanTime, String>();
		
		File file = new File(this.filename);
		FileInputStream fileInputStream;
		
		try {
			fileInputStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			System.out.println("Failed to open the json file!");
			this.setErrorString("Failed to open the json file!");
			return null;
		}
		
		Long fileLength = file.length();
		byte[] b = new byte[fileLength.intValue()];
		
		String string = new String();
		
		try {
			fileInputStream.read(b);
		} catch (IOException e) {
			System.out.println("Failed to read the json file!");
			this.setErrorString("Failed to read the json file!");
			return null;
		}
		
		try {
			string = new String(b, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			this.setErrorString("unsupported encoding exception, FileSaver.java:read()");
			System.out.println("unsupported encoding exception, FileSaver.java:read()");
			return null;
		}
		
		// 从string中载入jsonObject
		JSONObject jsonObject = JSONObject.fromObject(string);
		// 从json中载入table对象
		jsonObject = jsonObject.getJSONObject("table");
		
		if ("null".equals(jsonObject.toString())) {
			this.setErrorString("Failed to read table object!");
			return null;
		}
		
		String key = new String();
		String value = new String();
		for (Iterator<?> iterator = jsonObject.keys(); iterator.hasNext();) {
			key = (String)iterator.next();
			value = jsonObject.getString(key.toString());
			
			System.out.println(key);
			planMap.put(PlanTime.valueOf(key), value);
		}
		
		try {
			fileInputStream.close();
		} catch (IOException e) {
			System.out.println("Failed to close the file!");
			this.setErrorString("Failed to close the file!");
		}
		
		return planMap;
	}
	
	// 提供给外部获取错误信息
	public String getErrorString() {
		return this.errorString;
	}
	
	// 内部设置错误信息
	public void setErrorString(String errorString) {
		this.errorString = errorString;
	}
	
	private String filename;
	private String errorString;
}
