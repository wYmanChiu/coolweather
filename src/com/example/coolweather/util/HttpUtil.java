package com.example.coolweather.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {

	public static void sendHttpRequest(final String address, 
			final HttpCallbackListener listener) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				HttpURLConnection conn = null;
				InputStream in = null;
				BufferedReader reader = null;
				try {
					URL url = new URL(address);
					conn = (HttpURLConnection) url.openConnection();
					conn.setConnectTimeout(10 * 1000);
					conn.setRequestMethod("GET");
					conn.setReadTimeout(10 * 1000);
					in = conn.getInputStream();
					reader = new BufferedReader(new InputStreamReader(in));
					StringBuffer response = new StringBuffer();
					String line;
					while ((line = reader.readLine()) != null) {
						response.append(line);
					}
					if (listener != null) {
						//调用onFinish()方法
						listener.onFinish(response.toString());
					}
				} catch (Exception e) {
					if (listener != null) {
						listener.onError(e);
					}
				} finally {
					try {
						if (reader != null) {
							reader.close();
						}
						if (in != null) {
							in.close();
						}
						if (conn != null) {
							conn.disconnect();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
}
