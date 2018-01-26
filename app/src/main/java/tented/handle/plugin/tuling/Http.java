package tented.handle.plugin.tuling;

import java.io.*;
import java.net.*;
import org.json.*;

public class Http {
	JSONObject a = new JSONObject();
	public String charset = "UTF-8";

	private Cookie cookie = null;

	private HttpURLConnection conn = null;

	public Http() {
		cookie = new Cookie();
	}

	public Http(Cookie cookie) {
		this.cookie = cookie;
	}

	public void setCookie(Cookie cookie) {
		this.cookie = cookie;
	}

	public Cookie getCookie() {
		return cookie;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public int requestCode() {
		try {
			return conn.getResponseCode();
		} catch (IOException e) {
			return 0;
		}
	}

	public InputStream getInputStream() {
		try {
			return conn.getInputStream();
		} catch (IOException e) {
			return null;
		}
	}
	public long getLong(String paramString)
	{
		try
		{
			long l = this.a.getLong(paramString);
			return l;
		}
		catch (JSONException localJSONException)
		{
		}
		return 0;
	}
	public long getLong(long paramLong)
	{
		try
		{
			long l = this.a.getLong("" + paramLong);
			return l;
		}
		catch (JSONException localJSONException)
		{
		}
		return 0;
	}

	public byte[] get(String link, boolean isturn, String referer, int linkTime, int readTime, String... header) throws Exception {

		conn = (HttpURLConnection) new URL(link).openConnection();

		conn.setRequestProperty("Accept-Charset", charset);
		conn.setRequestProperty("Connection", "keep-alive");
		conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36");

		if (cookie.getSize() > 0) conn.setRequestProperty("Cookie", cookie.toString());
		if (referer != null) conn.setRequestProperty("Referer", referer);

		conn.setInstanceFollowRedirects(isturn);
		conn.setConnectTimeout(linkTime);
		conn.setReadTimeout(readTime);

		if (header.length % 2 == 0) {
			for (int i = 0; i < header.length; i+=2) {
				conn.setRequestProperty(header[i], header[i + 1]);
			}
		}

		conn.connect();

		for (int i = 0; conn.getHeaderFieldKey(i) != null; i++) {
			if (conn.getHeaderFieldKey(i).equals("Set-Cookie")) {
				cookie.addCookie(conn.getHeaderField(i));
			}
		}

		int len = -1;

		byte[] b = new byte[1024 * 4];

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		while ((len = conn.getInputStream().read(b)) != -1) {
			out.write(b, 0, len);
		}

		return out.toByteArray();
	}

	public byte[] post(String link, String param, boolean isturn, String referer, int linkTime, int readTime, String... header) throws Exception {

		conn = (HttpURLConnection) new URL(link).openConnection();

		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setUseCaches(false);

		conn.setRequestProperty("Accept-Charset", charset);
		conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36");

		if (cookie.getSize() > 0) conn.setRequestProperty("Cookie", cookie.toString());
		if (referer != null) conn.setRequestProperty("Referer", referer);

		conn.setInstanceFollowRedirects(isturn);
		conn.setConnectTimeout(linkTime);
		conn.setReadTimeout(readTime);

		DataOutputStream out = new DataOutputStream(conn.getOutputStream());
		out.writeBytes(param);
		out.flush();
		out.close();

		if (header.length % 2 == 0) {
			for (int i = 0; i < header.length; i+=2) {
				conn.setRequestProperty(header[i], header[i + 1]);
			}
		}

		conn.connect();

		for (int i = 0; conn.getHeaderFieldKey(i) != null; i++) {
			if (conn.getHeaderFieldKey(i).equals("Set-Cookie")) {
				cookie.addCookie(conn.getHeaderField(i));
			}
		}

		int len = -1;

		byte[] b = new byte[1024 * 4];

		ByteArrayOutputStream bout = new ByteArrayOutputStream();

		while ((len = conn.getInputStream().read(b)) != -1) {
			bout.write(b, 0, len);
		}

		return bout.toByteArray();
	}




}

