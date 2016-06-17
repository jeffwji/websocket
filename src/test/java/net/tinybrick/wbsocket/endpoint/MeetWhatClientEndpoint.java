package net.tinybrick.wbsocket.endpoint;

import java.io.IOException;

import javax.websocket.ClientEndpoint;
import javax.websocket.DecodeException;
import javax.websocket.EncodeException;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.tinybrick.wbsocket.endpoint.MeetWhatClientEndpoint.EndpointConfigurator;
import net.tinybrick.websocket.utils.codec.EndpointDecoder;
import net.tinybrick.websocket.utils.codec.EndpointEncoder;

@ClientEndpoint(encoders = { EndpointEncoder.class }, decoders = { EndpointDecoder.class }, configurator = EndpointConfigurator.class)
public class MeetWhatClientEndpoint {
	private static final Logger logger = LoggerFactory.getLogger(MeetWhatClientEndpoint.class.getName());

	@OnOpen
	public void onOpen(final Session session, EndpointConfig config) throws IOException, EncodeException {
		logger.info("Session is opening...");
	}

	@OnMessage
	public void onMessage(final JSONObject message, final Session session) throws IOException, DecodeException,
			InterruptedException {
		logger.info(String.format("Client received message '%s'", message.toString()));
	}

	@OnClose
	public void onClose(final Session session) {
		logger.info("Session is closing...");
	}

	@OnError
	public void onError(final Throwable t, final Session session) {
		logger.error(t.getMessage(), t);
	}

	static public class EndpointConfigurator extends ClientEndpointConfigurator {

		@Override
		public String getUsername() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getPassword() {
			// TODO Auto-generated method stub
			return null;
		}

	}
}
