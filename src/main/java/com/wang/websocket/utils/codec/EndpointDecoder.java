package com.wang.websocket.utils.codec;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EndpointDecoder implements Decoder.Text<JSONObject> {
	private static final Logger logger = LoggerFactory.getLogger(EndpointDecoder.class);

	@Override
	public void init(EndpointConfig config) {
		// TODO Auto-generated method stub
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	@Override
	public JSONObject decode(String str) throws DecodeException {
		String jsonString = str;
		JSONObject message = null;

		try {
			message = new JSONObject(jsonString);
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DecodeException(str, "Can not be converted to " + Package.class.getName(), e);
		}

		return message;
	}

	@Override
	public boolean willDecode(String s) {
		// TODO Auto-generated method stub
		return true;
	}

}
