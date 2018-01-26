package tented.handle.plugin.tuling;

import java.util.*;

public class Cookie {

	private Map<String, String> map = new HashMap<>();

	public Cookie() {}

	public Cookie(String cookies) {

		String[] strs = cookies.split(";");

		for (int i = 0; i < strs.length; i++) {

			String[] kv = strs[i].split("=");

			if (kv.length > 1) {
				addCookie(kv[0].trim(), kv[1].trim());
			}

		}

	}
	public void setCookie(String cookie)
	{
		String[] c=cookie.split(";");
		for(String c1:c)
		{
			String key=c1.substring(0, c1.indexOf("="));
			String value=c1.substring(c1.indexOf("=")+1, c1.length());
			addCookie(key, value);
		}
	}
	public int getSize() {
		return map.size();
	}

	public void addCookie(String cookie) {

		String[] strs = cookie.split(";");

		for (int i = 0; i < strs.length; i++) {

			String[] kv = strs[i].split("=");

			if (kv.length > 1) {
				addCookie(kv[0].trim(), kv[1].trim());
			}

		}
	}

	public void addCookie(String key, String value) {
		map.put(key, value);
	}

	public String getValue(String key) {
		return map.get(key);
	}

	@Override
	public String toString() {

		StringBuffer cookie = new StringBuffer();

		Iterator<String> it = map.keySet().iterator();

		while (it.hasNext()) {

			String key = it.next();

			cookie.append(key + "=" + map.get(key) + "; ");
		}

		return cookie.toString();
	}
}

