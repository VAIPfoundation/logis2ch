package com.sdc2ch.service.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpClient {

	private String u;
	private Map<String, String> header;
	private ObjectMapper mapper = new ObjectMapper();
	
	public HttpClient(String url) {
		this.u = url;
		this.header = new HashMap<>();
	}
	
	public void addHeader(String key, String value) {
		header.put(key, value);
	}
	
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> send(String contents) throws IOException {
		HttpURLConnection conn = null;
		InputStream ins = null;
		try {

			URL url = new URL(u);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(1000);
			conn.setDoOutput(true);
			conn.setReadTimeout(30000);
			conn.setRequestMethod("POST");
			
			for(String k : header.keySet()) {
				conn.addRequestProperty(k, header.get(k));
			}
			



			OutputStream outputStream = conn.getOutputStream();
			outputStream.write(contents.getBytes("UTF-8"));
			outputStream.flush();
			outputStream.close();

			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				log.warn("{}", conn.getContent());
			}
			ins = conn.getInputStream();

			if (ins != null) {
				StringBuffer sb = new StringBuffer();

				BufferedReader reader = new BufferedReader(new InputStreamReader(ins, "UTF-8"));

				String s;

				while ((s = reader.readLine()) != null) {
					sb.append(s);
				}
				reader.close();
				HashMap mapped = mapper.readValue(sb.toString().getBytes("UTF-8"), HashMap.class);
				return mapped;
			}

		} finally {
			if (conn != null)
				conn.disconnect();
			if (ins != null)
				ins.close();
		}
		return null;
	}
}
