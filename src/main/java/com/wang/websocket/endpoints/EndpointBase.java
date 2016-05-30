package com.wang.websocket.endpoints;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.websocket.DecodeException;
import javax.websocket.EncodeException;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import com.wang.cache.interfaces.ICacheManager;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.wang.websocket.services.WebsocketSessionManager;

//import net.sf.ehcache.Cache;

//@ServerEndpoint(value = "/message", encoders = { EndpointEncoder.class }, decoders = { EndpointDecoder.class }/*, configurator = SpringServerEndpointConfigurator.class*/)
public class EndpointBase {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected @Autowired
	ICacheManager websocketSessionManager;

	protected Map<String, Object> context = new HashMap<String, Object>();

	protected String principal = null;
	protected HttpSession httpSession = null;

	@OnOpen
	public void onOpen(final Session session, EndpointConfig config) {
		logger.debug("WebSocket session is opening..");

		ServletContext servletContext = (ServletContext) session.getUserProperties().get("ServletContext");

		if (null != session.getUserPrincipal()) {
			principal = (String) ((org.springframework.security.core.Authentication) (session.getUserPrincipal()))
					.getPrincipal();
		}

		if (null == principal) {
			logger.warn("Unauthorized access detected.");
			/*try {
				session.close();
			}
			catch (IOException e) {
				logger.info(e.getMessage(), e);
			}
			throw new AuthenticationException("Unauthorized access.");*/
		}

		String username = null != principal ? principal.toString() : "ANONYMOUS_" + (new Date()).getTime();
		websocketSessionManager.add(username, session);

		httpSession = (HttpSession) config.getUserProperties().remove(username);
		// Store UUID in http session.

		context.put("Principal", principal);
		context.put(HttpSession.class.getName(), httpSession);
		context.put(Session.class.getName(), session);
	}

	@OnMessage
	public void onMessage(final JSONObject message, final Session client) throws IOException, DecodeException,
			EncodeException {
		logger.debug(message.toString());
	}

	@OnError
	public void onError(final Throwable t, final Session session) {
		logger.error(session.getUserPrincipal().getName() + "-" + t.getMessage(), t);
	}

	@OnClose
	public void onClose(final Session session) {
		websocketSessionManager.remove(session.getUserPrincipal().getName());

		httpSession.invalidate();

		logger.debug("WebSocket session is closed.");
	}
}
