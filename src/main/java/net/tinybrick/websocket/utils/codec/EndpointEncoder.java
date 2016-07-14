package net.tinybrick.websocket.utils.codec;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import org.json.JSONObject;

public class EndpointEncoder implements Encoder.Text<JSONObject> {
	//private static final Logger logger = LoggerFactory.getLogger(EndpointEncoder.class);

	@Override
	public void init(EndpointConfig config) {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	/**
	 * 将目标类 &ltT&gt转成 JsonString
	 */
	@Override
	public String encode(JSONObject message) throws EncodeException {
		String jsonString = null;
		try {
			jsonString = message.toString();
		}
		catch (Exception e) {
			throw new EncodeException(jsonString, "Can not be encoded", e);
		}

		// 如有必要，可对JsonString做加密处�?
		/*if(null != jsonString)
		{
			try {
				jsonString = Codec.stringToBase64(jsonString);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new EncodeException(jsonString, " Cannot be encrypted", e);
			}
		}*/

		return jsonString;
	}

}
