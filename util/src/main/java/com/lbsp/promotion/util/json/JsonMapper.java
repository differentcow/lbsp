package com.lbsp.promotion.util.json;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Type;

public class JsonMapper {

	private static ObjectMapper mapper;
	private static JsonMapper jsonMapper;
	private static final Logger logger = Logger.getLogger(JsonMapper.class);

	private static class ObjectMapperFactory {
		public static ObjectMapper getObjectMapper() {
			if (mapper == null) {
				mapper = new ObjectMapper();
				mapper.configure(
						DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
						false);
				mapper.setSerializationInclusion(Include.NON_NULL);
			}
			return mapper;
		}
	}

	private static class JsonMapperFactory {
		public static JsonMapper getJsonMapper() {
			if (jsonMapper == null) {
				jsonMapper = new JsonMapper();
				if (mapper == null) {
					mapper = ObjectMapperFactory.getObjectMapper();
				}
			}
			return jsonMapper;
		}
	}

	public static ObjectMapper getObjectMapperInstance() {
		return ObjectMapperFactory.getObjectMapper();
	}

	public static JsonMapper getJsonMapperInstance() {
		return JsonMapperFactory.getJsonMapper();
	}

	public String getValue(String json, String name) {
		try {
			JsonFactory factory = new JsonFactory();
			JsonParser parser = factory.createParser(json);
			parser.setCodec(ObjectMapperFactory.getObjectMapper());
			TreeNode tree = parser.readValueAsTree();
			return tree.get(name).toString().replaceAll("\"", "");
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	public String toJson(Object obj) {
		Writer strWriter = new StringWriter();
		try {
			mapper.writeValue(strWriter, obj);
		} catch (Exception e) {
			logger.error(e);
		}
		return strWriter.toString();
	}

	public <T> T readValue(String str, TypeReference<T> typeReference) {
		try {
			return mapper.readValue(str, typeReference);
		} catch (Exception e) {
			logger.error(e);
			return null;
		}
	};

	public <T> T readValue(String str, Class<T> type) {
		Type localType = super.getClass().getGenericSuperclass();
		if (localType instanceof Class) {
			try {
				return mapper.readValue(str, type);
			} catch (Exception e) {
				logger.error(e);
				return null;
			}
		}
		return null;
	};
}
