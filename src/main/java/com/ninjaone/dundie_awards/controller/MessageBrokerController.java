package com.ninjaone.dundie_awards.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.ninjaone.dundie_awards.dto.ActivityDto;

@Controller
public class MessageBrokerController {
	
  // Endpoint to send a message to the message queue
  @MessageMapping("/message")
  @SendTo("/topic/messages")
  public ActivityDto message(ActivityDto activity) throws Exception {
    Thread.sleep(1000); // simulated delay
    return new ActivityDto(activity.getOccuredAt(), activity.getEvent());
  }

}