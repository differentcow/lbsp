package com.lbsp.promotion.util.http.converter.json;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;


/**
 *
 * created by Barry
 */
public class CustomJacksonConvert extends MappingJackson2HttpMessageConverter {
	public CustomJacksonConvert() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);
		mapper.setSerializationInclusion(Include.NON_NULL);
		super.setObjectMapper(mapper);
	}
}
