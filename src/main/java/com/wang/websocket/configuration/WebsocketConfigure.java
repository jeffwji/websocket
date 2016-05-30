package com.wang.websocket.configuration;

import java.security.Principal;

import com.wang.thread.configure.ThreadConfig;
import com.wang.websocket.endpoints.EchoServiceEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import javax.websocket.DeploymentException;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerContainer;
import javax.websocket.server.ServerEndpointConfig;

import com.wang.websocket.endpoints.configurators.SpringServerEndpointConfigBuilder;
/**
 * Created by wangji on 2016/5/30.
 */

@Configuration
@EnableAutoConfiguration
@PropertySource(value = "classpath:config/websocket.properties")
@Import(value = { ThreadConfig.class })
public class WebsocketConfigure {
    private static final Logger logger = LoggerFactory.getLogger(WebsocketConfigure.class.getName());

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    final boolean threadSafe = false;

    @Autowired
    private static WebApplicationContext context;
    private ServerContainer container;

    public static class SpringServerEndpointConfigurator extends ServerEndpointConfig.Configurator {
        @Override
        public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
            T endpoint = context.getAutowireCapableBeanFactory().createBean(endpointClass);
            return endpoint;
        }

        /**
         * Handshake 过程是非线程安全的，因此以下代码实际上不会被执行。留存在这里只是为未来线程安全问题得到解决后留下一个可能性。比如我们可以在这里处理 http session
         */
        @Override
        public synchronized void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request,
                                                 HandshakeResponse response) {
            super.modifyHandshake(sec, request, response);

            logger.debug("Handshaking...");
			/*if(threadSafe)
			{
				synchronized(sec)
				{*/
            // Save HttpSession
            HttpSession httpSession = (HttpSession) request.getHttpSession();
            Principal principal = request.getUserPrincipal();

            if (null == httpSession || null == principal) {
                throw new SecurityException("Unauthorized access is detected.");
            }

            sec.getUserProperties().put(principal.getName(), httpSession);
			/*}
			}*/

            // Iterate headers.
			/*Map<String, List<String>> headers = request.getHeaders();
			Iterator<String> names = headers.keySet().iterator();
			while(names.hasNext())
			{
				String name = names.next();
				List<String> values = headers.get(name);
				for(String value: values)
				{
					logger.info(String.format("%s: %s", name, value));

					if(name == "Authorization")
			        {
			        	try
			        	{
			           		String authenticationToken = new String(Base64.decodeBase64(value.split(" ")[1].getBytes(Charset.forName("UTF-8"))), "UTF-8");
			           		String[] princple = authenticationToken.split(":");
			           		logger.info(String.format("\"%s\" is trying to authorize by: \"%s\"", princple[0], princple[1]));
			        	}
			        	catch(UnsupportedEncodingException e)
			        	{
			        		logger.error(e.getMessage(), e);
			        	}
			        }
			   	}
			}*/
        }
    }

    @Bean
    public ServerEndpointConfig.Configurator configurator() {
        return new SpringServerEndpointConfigurator();
    }

    @PostConstruct
    public void init() throws DeploymentException {
        container = (ServerContainer) context.getServletContext().getAttribute(
                javax.websocket.server.ServerContainer.class.getName());

        container.addEndpoint(new SpringServerEndpointConfigBuilder(EchoServiceEndpoint.class) {
            @Override
            public Configurator getConfigurator() {
                return configurator();
            }
        });
    }
}
