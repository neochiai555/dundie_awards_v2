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
import com.ninjaone.dundie_awards.model.Activity;
import com.ninjaone.dundie_awards.service.ActivityService;
import com.ninjaone.dundie_awards.service.OrganizationBusinessService;


/**
 * Message broker class
 */
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
    
    /**
     * Initialize message broker
     */
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
	     	
	     	// Subscribe to the request messages topic to handle creating of activiy
	     	sessionHandler.getSession().subscribe("/topic/requestmessages", new StompFrameHandler() {

	     		@Override
	     		public Type getPayloadType(StompHeaders headers) {
	     			return String.class;
	     		}

	     		@Override
	     		public void handleFrame(StompHeaders headers, Object payload) {
	     			// If payload is an activity creation request, create the activity
	     			if (payload instanceof ActivityMessageRequest) {
	     				ActivityMessage activityMessage = (ActivityMessage)payload;
	     				try {	     					
			     			Activity activity = new Activity(
			     					activityMessage.getActivityDto().getOccuredAt(), 
			     					activityMessage.getActivityDto().getEvent());
			     			activityService.save(activity);
			     			
			     			// Send a success message to the response messages topic
			     			sendMessageSuccess(activityMessage.getActivityDto(), activityMessage.getOrganizationId());
	     				} catch (Exception e) {
	     					try {
	     						// Rollback dundie awards for the organization in case there is an exception creating the activity
	     						organizationBusinessService.withDrawDundieAwards(activityMessage.getOrganizationId());
	     						
	     						// Send a failure message to the response messages topic
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

    /**
     * Send message to message broker
     */
    public void sendMessage(ActivityDto message, Long organizationId, 
    		String topic, ActivityMessageType type) {        
        StompSession session = sessionHandler.getSession();
        if (session != null) {        	
        	ActivityMessage activityMessage = new ActivityMessageRequest(message, type);
        	activityMessage.setOrganizationId(organizationId);
        	sessionHandler.getSession().send(topic, activityMessage);
        	messages.add(activityMessage);
        }
    }

    /**
     * Send create activity message to message broker
     */
    public void sendMessageCreate(ActivityDto message, Long organizationId) {
    	sendMessage(message, organizationId, "/topic/requestmessages", ActivityMessageType.CREATE);
    }

    /**
     * Send activity create success message to message broker
     */
    public void sendMessageSuccess(ActivityDto message, Long organizationId) {
    	sendMessage(message, organizationId, "/topic/responsemessages", ActivityMessageType.SUCCESS);
    }

    /**
     * Send activity create failure message to message broker
     */
    public void sendMessageFailure(ActivityDto message, Long organizationId) {
    	sendMessage(message, organizationId, "/topic/responsemessages", ActivityMessageType.FAILURE);
    }

    public List<ActivityMessage> getMessages(){
        return messages;
    }
}
