package com.ninjaone.dundie_awards;

import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

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