package com.ninjaone.dundie_awards.converter;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ninjaone.dundie_awards.messaging.message.ActivityMessageRequest;

@Component
public class ActivityMessageConverter implements MessageConverter {

	@Override
	public Object fromMessage(Message<?> message, Class<?> targetClass) {
		System.out.println(new String((byte[]) message.getPayload()));
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		try {
			return mapper.readValue((byte[]) message.getPayload(), ActivityMessageRequest.class);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	@Override
	public Message<?> toMessage(Object payload, MessageHeaders headers) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		byte[] bPayload = null;
		try {
			bPayload = mapper.writeValueAsBytes(payload);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		GenericMessage<byte[]> message = new GenericMessage<byte[]>(bPayload, headers);

		return message;
	}

}