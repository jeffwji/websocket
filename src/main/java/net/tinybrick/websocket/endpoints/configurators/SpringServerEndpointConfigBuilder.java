package net.tinybrick.websocket.endpoints.configurators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.Decoder;
import javax.websocket.DeploymentException;
import javax.websocket.Encoder;
import javax.websocket.Extension;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;

public abstract class SpringServerEndpointConfigBuilder implements ServerEndpointConfig {
	private final Class<?> endpointClass;
	private final String path;
	private final List<Class<? extends Decoder>> decoders;
	private final List<Class<? extends Encoder>> encoders;
	private final List<String> subprotocols;

	private Map<String, Object> userProperties;
	private List<Extension> extensions;

	public SpringServerEndpointConfigBuilder(Class<?> endpointClass) throws DeploymentException {
		ServerEndpoint anno = endpointClass.getClass().getAnnotation(ServerEndpoint.class);

		this.decoders = Collections.unmodifiableList(Arrays.asList(anno.decoders()));

		this.encoders = Collections.unmodifiableList(Arrays.asList(anno.encoders()));

		this.subprotocols = Collections.unmodifiableList(Arrays.asList(anno.subprotocols()));

		this.path = anno.value();

		// supplied by init lifecycle
		this.extensions = new ArrayList<>();
		// always what is passed in
		this.endpointClass = endpointClass;
		// UserProperties in annotation
		this.userProperties = new HashMap<>();
	}

	public abstract ServerEndpointConfig.Configurator getConfigurator();

	@Override
	public List<Class<? extends Decoder>> getDecoders() {
		return decoders;
	}

	@Override
	public List<Class<? extends Encoder>> getEncoders() {
		return encoders;
	}

	@Override
	public Class<?> getEndpointClass() {
		return endpointClass;
	}

	@Override
	public List<Extension> getExtensions() {
		return extensions;
	}

	@Override
	public String getPath() {
		return path;
	}

	@Override
	public List<String> getSubprotocols() {
		return subprotocols;
	}

	@Override
	public Map<String, Object> getUserProperties() {
		return userProperties;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getName() + "[endpointClass=");
		builder.append(endpointClass);
		builder.append(",path=");
		builder.append(path);
		builder.append(",decoders=");
		builder.append(decoders);
		builder.append(",encoders=");
		builder.append(encoders);
		builder.append(",subprotocols=");
		builder.append(subprotocols);
		builder.append(",extensions=");
		builder.append(extensions);
		builder.append("]");
		return builder.toString();
	}
}
