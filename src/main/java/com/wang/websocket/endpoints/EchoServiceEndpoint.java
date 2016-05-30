package com.wang.websocket.endpoints;

import java.io.IOException;

import javax.websocket.DecodeException;
import javax.websocket.EncodeException;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.json.JSONObject;

import com.wang.websocket.utils.codec.EndpointDecoder;
import com.wang.websocket.utils.codec.EndpointEncoder;

@ServerEndpoint(value = "/echo", encoders = { EndpointEncoder.class }, decoders = { EndpointDecoder.class })
public class EchoServiceEndpoint extends EndpointBase {
	@OnMessage
	public void onMessage(final JSONObject message, final Session client) throws IOException, DecodeException,
			EncodeException {
		logger.debug(String.format("%s", "Echo service"));
	}
}
