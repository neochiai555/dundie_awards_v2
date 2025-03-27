package com.ninjaone.dundie_awards.messaging;

import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

/**
 * Class to handle message broker session connection
 */
public class MessageSessionHandler extends StompSessionHandlerAdapter {
	private StompSession session;

	@Override
	public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
		// ...
		System.out.println("Session established.");
	}
	
	public StompSession getSession() {
		return session;
	}
	
	public void setSession(StompSession session) {
		this.session = session;
	}
}