package com.wang.wbsocket.endpoint.it;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.EncodeException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import javax.ws.rs.core.NewCookie;

//import org.eclipse.jetty.websocket.jsr356.ClientContainer;
import com.wang.wbsocket.endpoint.ClientEndpointConfigurator;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wang.wbsocket.endpoint.MeetWhatClientEndpoint;

public class WebsocketTest {
	static final String WEBSOCKET_SERVICE_BASE_URI = "ws://localhost:8080/htche-websocket";
	protected WebSocketContainer container = null;
	protected List<NewCookie> cookies = null;

	private static final Logger logger = LoggerFactory.getLogger(WebsocketTest.class.getName());

	@Before
	public void connectWebSocket() throws Exception {
		/**
		 * WebSockt client endpoint:
		 * 1) Enable "wss://" protocol
		 */
		System.getProperties().setProperty("org.eclipse.jetty.websocket.jsr356.ssl-trust-all", "true");

		/**
		 * 1.1) For bi-direct authentication, setup client end keystore file.
		 * final String path = ServerStarter.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		 * System.getProperties().setProperty("javax.net.ssl.keyStore", path + "client1");
		 * System.getProperties().setProperty("javax.net.ssl.keyStorePassword", "changeit");
		 */

		/**
		 * 2) Get WebSocketContainer then connect to server and send message
		 */
		container = ContainerProvider.getWebSocketContainer();

		/*Session session = container.connectToServer(WhatsFactorClientEndpoint.class, URI.create(websocketServiceUri));
		for (int i = 1; i <= 3; ++i) {
			RequestMessage message = new RequestMessage();
			session.getBasicRemote().sendObject(message);
			Thread.sleep(1000);
		}*/
	}

	@After
	public void disconnectWebSocket() throws Exception {
		/**
		 * 3) Application doesn't exit if container's threads are still running
		 */
		//((ClientContainer) container).stop();
	}

	@Test
	public void testEchoWebsocket() throws DeploymentException, IOException, EncodeException {
		final String websocketServiceUri = WEBSOCKET_SERVICE_BASE_URI + "/echo";
		Session session = container.connectToServer(MeetWhatClientEndpoint.class, URI.create(websocketServiceUri));
		cookies = ClientEndpointConfigurator.getCookies();

		JSONObject message = new JSONObject("{message:'hello websocket'}");
		session.getBasicRemote().sendObject(message);
		session.close();
	}
}
