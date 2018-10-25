package com.sonu.resdemo.scripts;

import android.util.Log;

import org.apache.http.entity.mime.MultipartEntity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class JSONParser {

	static InputStream is = null;
	public JSONParser() {

	}

	public static String httpService(String mServiceName, Map<String,Object> params, String method) {
		String result = "";
		try {
			URL url = new URL(mServiceName);
			StringBuilder postData = new StringBuilder();
			for (Map.Entry<String,Object> param : params.entrySet()) {
				if (postData.length() != 0) postData.append('&');
				postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
				postData.append('=');
				postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
			}
			Log.i("LOGS",postData.toString());
			byte[] postDataBytes = postData.toString().getBytes("UTF-8");
			if (!method.equals("POST"))
				url = new URL(mServiceName+"?"+postData.toString());
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			if (method.equals("POST"))
				conn.setRequestMethod("POST");
			else
				conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			if (method.equals("post"))
			conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
			conn.setRequestProperty("User-Agent","Mozilla/5.0 ( compatible ) ");
			conn.setRequestProperty("Accept", "*/*");
			conn.setDoOutput(true);
			if (method.equals("post"))
			conn.getOutputStream().write(postDataBytes);

			try {
				is = conn.getInputStream();
			} catch(FileNotFoundException exception){
				//log.error(exception.getMessage(), exception);
				is = conn.getErrorStream();
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

			StringBuilder sb = new StringBuilder();
			for (int c; (c = reader.read()) >= 0;)
				sb.append((char)c);
			result = sb.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.e("result", result);

		return result;
	}

	public String downloadUrl(String urlString) throws IOException {
		String data = "";
		InputStream stream = null;
		HttpURLConnection urlConnection = null;
		try {

//			// Display our JSON in our browser (to show us how we need to implement our parser)
//			Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(urlString));
//			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			startActivity(intent);

			URL url = new URL(urlString);

			// Create a http connection to communicate with url
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.connect();

			// read in our data
			stream = urlConnection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(stream));
			StringBuffer sb = new StringBuffer();

			// read in our data in, and append it as a single data string
			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			data = sb.toString();
			br.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {

			stream.close();
			urlConnection.disconnect();
		}

		return data;
	}

	public static String multipost(String urlString, MultipartEntity reqEntity) {

		try {
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("POST");
			conn.setUseCaches(false);
			conn.setDoInput(true);
			conn.setDoOutput(true);

			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.addRequestProperty("Content-length", reqEntity.getContentLength() + "");
			conn.addRequestProperty(reqEntity.getContentType().getName(), reqEntity.getContentType().getValue());

			OutputStream os = conn.getOutputStream();
			reqEntity.writeTo(conn.getOutputStream());
			os.close();
			conn.connect();

			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				return readStream(conn.getInputStream());
			}


		} catch (java.net.SocketTimeoutException e) {
        Log.i("Exception in PHoto send",e.toString());
			return "NOT";
		}
		catch (Exception e) {
			Log.i("Exception in",e.toString());
			return "NOT";
		}
		return "NOT";
	}

	private static String readStream(InputStream instream) {
		StringBuilder sb = null;
		try {
			sb = new StringBuilder();
			BufferedReader r = new BufferedReader(new InputStreamReader(
					instream));
			for (String line = r.readLine(); line != null; line = r.readLine()) {
				sb.append(line);
			}

			instream.close();

		} catch (IOException e) {
			Log.i("IOException in read",e+"");
		}
		return sb.toString();

	}


}