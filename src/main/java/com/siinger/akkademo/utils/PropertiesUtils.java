package com.siinger.akkademo.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * ClassName: PropertiesUtils <br/>
 * Function: 工具类，获取配置信息 <br/>
 * date: 2016年8月5日 下午5:34:16 <br/>
 *
 * @author siinger
 * @version 
 * @since JDK 1.7
 */
public class PropertiesUtils {

	private static Properties properties = new Properties();

	public static void load(String fileName) {
		Properties pros = new Properties();
		InputStream in = null;
		try {
			in = PropertiesUtils.class.getClassLoader().getResource(fileName).openStream();
			pros.load(in);
			properties.putAll(pros);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String get(String key) {
		return properties.getProperty(key);
	}

	public static void main(String[] args) {
		load("agent.properties");
	}
}