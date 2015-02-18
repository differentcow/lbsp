package com.lbsp.promotion.util.json;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy.PropertyNamingStrategyBase;
import org.apache.log4j.Logger;

import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Type;

/**
 * 针对实体类中属性是大小开头与json串的转换
 * 
 * @author Barry
 * 
 */
public class UpperCaseJsonMapper {

	private static ObjectMapper mapper;
	private static UpperCaseJsonMapper jsonMapper;
	private static final Logger logger = Logger
			.getLogger(UpperCaseJsonMapper.class);

	private static class ObjectMapperFactory {
		public static ObjectMapper getObjectMapper() {
			if (mapper == null) {
				mapper = new ObjectMapper();
				mapper.configure(
						DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
						false);
				mapper.setSerializationInclusion(Include.NON_NULL);
				mapper.setPropertyNamingStrategy(new PropertyNamingStrategyBase() {
					private static final long serialVersionUID = 3633970453126172410L;

					@Override
					public String translate(String propertyName) {
						String name = propertyName.replaceAll("^\\w",
								propertyName.toUpperCase().substring(0, 1));
						return name;
					}
				});
			}
			return mapper;
		}
	}

	private static class JsonMapperFactory {
		public static UpperCaseJsonMapper getJsonMapper() {
			if (jsonMapper == null) {
				jsonMapper = new UpperCaseJsonMapper();
				if (mapper == null) {
					mapper = ObjectMapperFactory.getObjectMapper();
				}
			}
			return jsonMapper;
		}
	}

	public ObjectMapper getObjectMapperInstance() {
		return ObjectMapperFactory.getObjectMapper();
	}

	public static UpperCaseJsonMapper getJsonMapperInstance() {
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
