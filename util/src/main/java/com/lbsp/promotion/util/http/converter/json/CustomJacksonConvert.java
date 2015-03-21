package com.lbsp.promotion.util.http.converter.json;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;


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

    @Override
    protected void writeInternal(Object object, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        outputMessage.getHeaders().set("If-Modified-Since","0");
        outputMessage.getHeaders().set("expire","0");
        super.writeInternal(object, outputMessage);
    }
}
