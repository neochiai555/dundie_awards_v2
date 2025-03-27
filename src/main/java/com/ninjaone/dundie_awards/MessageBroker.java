package com.ninjaone.dundie_awards;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ninjaone.dundie_awards.converter.MessageActivityConverter;
import com.ninjaone.dundie_awards.dto.ActivityDto;
import com.ninjaone.dundie_awards.model.Activity;
import com.ninjaone.dundie_awards.service.ActivityService;

@Component
public class MessageBroker {

    private List<Activity> messages = new LinkedList<>();
    private MessageSessionHandler sessionHandler = null;
    private final ActivityService activityService;
    
    public MessageBroker(ActivityService activityService) {
    	this.activityService = activityService;
    }
    
    public void initMessageBroker() {
    	try {
    		if (sessionHandler != null) {
    			return;
    		}
	    	WebSocketClient webSocketClient = new StandardWebSocketClient();
	    	WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
	    	stompClient.setMessageConverter(new MessageActivityConverter());
	    	String url = "ws://localhost:3001/websocket";
	    	sessionHandler = new MessageSessionHandler();
	     	sessionHandler.setSession(stompClient.connectAsync(url, sessionHandler).get());
	     	
	     	sessionHandler.getSession().subscribe("/topic/messages", new StompFrameHandler() {

	     		@Override
	     		public Type getPayloadType(StompHeaders headers) {
	     			return String.class;
	     		}

	     		@Override
	     		public void handleFrame(StompHeaders headers, Object payload) {
	     			// ...
	     			ActivityDto activityDto = (ActivityDto)payload;
	     			Activity activity = new Activity(activityDto.getOccuredAt(), activityDto.getEvent());
	     			activityService.save(activity);
	     		}

	     	});
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}
    }

    public void sendMessage(Activity message) throws JsonProcessingException {
        messages.add(message);
        StompSession session = sessionHandler.getSession();
        if (session != null) {
        	ObjectMapper mapper = new ObjectMapper();
        	mapper.registerModule(new JavaTimeModule());
        	mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        	sessionHandler.getSession().send("/topic/messages", new ActivityDto(message.getOccuredAt(), message.getEvent()));
        }
    }

    public List<Activity> getMessages(){
        return messages;
    }
}
