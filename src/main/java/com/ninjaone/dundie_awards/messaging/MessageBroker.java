package com.ninjaone.dundie_awards.messaging;

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

import com.ninjaone.dundie_awards.converter.ActivityMessageConverter;
import com.ninjaone.dundie_awards.dto.ActivityDto;
import com.ninjaone.dundie_awards.enums.ActivityMessageType;
import com.ninjaone.dundie_awards.messaging.message.ActivityMessage;
import com.ninjaone.dundie_awards.messaging.message.ActivityMessageRequest;
import com.ninjaone.dundie_awards.messaging.message.ActivityMessageResponse;
import com.ninjaone.dundie_awards.model.Activity;
import com.ninjaone.dundie_awards.service.ActivityService;
import com.ninjaone.dundie_awards.service.OrganizationBusinessService;

@Component
public class MessageBroker {

    private List<ActivityMessage> messages = new LinkedList<>();
    private MessageSessionHandler sessionHandler = null;
    private final ActivityService activityService;
    private final OrganizationBusinessService organizationBusinessService;
    
    public MessageBroker(ActivityService activityService, OrganizationBusinessService organizationBusinessService) {
    	this.activityService = activityService;
    	this.organizationBusinessService = organizationBusinessService;
    }
    
    public void initMessageBroker() {
    	try {
    		if (sessionHandler != null) {
    			return;
    		}
	    	WebSocketClient webSocketClient = new StandardWebSocketClient();
	    	WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
	    	stompClient.setMessageConverter(new ActivityMessageConverter());
	    	String url = "ws://localhost:3001/websocket";
	    	sessionHandler = new MessageSessionHandler();
	     	sessionHandler.setSession(stompClient.connectAsync(url, sessionHandler).get());
	     	
	     	sessionHandler.getSession().subscribe("/topic/requestmessages", new StompFrameHandler() {

	     		@Override
	     		public Type getPayloadType(StompHeaders headers) {
	     			return String.class;
	     		}

	     		@Override
	     		public void handleFrame(StompHeaders headers, Object payload) {
	     			if (payload instanceof ActivityMessageRequest) {
	     				ActivityMessage activityMessage = (ActivityMessage)payload;
	     				try {	     					
			     			Activity activity = new Activity(
			     					activityMessage.getActivityDto().getOccuredAt(), 
			     					activityMessage.getActivityDto().getEvent());
			     			activityService.save(activity);
			     			sendMessageSuccess(activityMessage.getActivityDto(), activityMessage.getOrganizationId());
	     				} catch (Exception e) {
	     					try {
	     						organizationBusinessService.withDrawDundieAwards(activityMessage.getOrganizationId());
	     						sendMessageFailure(activityMessage.getActivityDto(), activityMessage.getOrganizationId());
	     					} catch (Exception ex) {
	     						ex.printStackTrace();
	     					}
	     					
	     				}
	     			}
	     		}

	     	});
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}
    }

    public void sendMessageCreate(ActivityDto message, Long organizationId) {        
        StompSession session = sessionHandler.getSession();
        if (session != null) {        	
        	ActivityMessage activityMessage = new ActivityMessageRequest(message, ActivityMessageType.CREATE);
        	activityMessage.setOrganizationId(organizationId);
        	sessionHandler.getSession().send("/topic/requestmessages", activityMessage);
        	messages.add(activityMessage);
        }
    }

    public void sendMessageSuccess(ActivityDto message, Long organizationId) {        
        StompSession session = sessionHandler.getSession();
        if (session != null) {        	
        	ActivityMessageResponse activityMessage = new ActivityMessageResponse(message, ActivityMessageType.SUCCESS);
        	activityMessage.setOrganizationId(organizationId);
        	sessionHandler.getSession().send("/topic/responsemessages", activityMessage);
        	messages.add(activityMessage);
        }
    }

    public void sendMessageFailure(ActivityDto message, Long organizationId) {        
        StompSession session = sessionHandler.getSession();
        if (session != null) {
        	ActivityMessageResponse activityMessage = new ActivityMessageResponse(message, ActivityMessageType.FAILURE);
        	activityMessage.setOrganizationId(organizationId);
        	sessionHandler.getSession().send("/topic/responsemessages", activityMessage);
        	messages.add(activityMessage);
        }
    }

    public List<ActivityMessage> getMessages(){
        return messages;
    }
}
