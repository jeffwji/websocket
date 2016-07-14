package net.tinybrick.wbsocket.endpoint;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.HandshakeResponse;
import javax.ws.rs.core.NewCookie;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ClientEndpointConfigurator extends ClientEndpointConfig.Configurator {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	private static List<NewCookie> cookies = new ArrayList<NewCookie>();

	public static void setCookies(List<NewCookie> cookies) {
		ClientEndpointConfigurator.cookies = cookies;
	}

	public abstract String getUsername();

	public abstract String getPassword();

	public static List<NewCookie> getCookies() {
		return cookies;
	}

	@Override
	public void beforeRequest(Map<String, List<String>> headers) {
		headers.put("User-Agent", Collections.singletonList("RESTful Client"));
		try {
			headers.put(
					"Authorization",
					Collections.singletonList("Basic "
							+ new String(Base64.encodeBase64((getUsername() + ":" + getPassword()).getBytes("UTF-8")),
									"UTF-8")));
		}
		catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
		}

		if (null != cookies || cookies.size() > 0) {
			List<String> cookieList = new ArrayList<String>();
			for (NewCookie cookie : cookies) {
				cookieList.add(cookie.toString());
			}
			headers.put("Cookie", cookieList);
		}
	}

	@Override
	public void afterResponse(HandshakeResponse response) {
		cookies.clear();

		Map<String, List<String>> headers = response.getHeaders();
		Iterator<String> names = headers.keySet().iterator();
		while (names.hasNext()) {
			String name = names.next();
			List<String> values = headers.get(name);
			for (String value : values) {
				logger.info(String.format("%s: %s", name, value));
				if (name.equals("Set-Cookie")) {
					NewCookie cookie = NewCookie.valueOf(value);
					cookies.add(cookie);
				}
			}
		}
	}
}
