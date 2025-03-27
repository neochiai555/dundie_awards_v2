package com.ninjaone.dundie_awards.messaging.message;

import com.ninjaone.dundie_awards.dto.ActivityDto;
import com.ninjaone.dundie_awards.enums.ActivityMessageType;

/**
 * Activity response message
 */
public class ActivityMessageResponse implements ActivityMessage {
	private ActivityDto activityDto;
	private ActivityMessageType type;
	private Long organizationId;
	
	public ActivityMessageResponse() {
		
	}
	
	public ActivityMessageResponse(ActivityDto activityDto, ActivityMessageType type) {
		this.activityDto = activityDto;
		this.type = type;
	}

	@Override
	public ActivityDto getActivityDto() {
		return activityDto;
	}

	@Override
	public void setActivityDto(ActivityDto activityDto) {
		this.activityDto = activityDto;
	}

	@Override
	public ActivityMessageType getType() {
		return type;
	}

	@Override
	public void setType(ActivityMessageType type) {
		this.type = type;
	}

	@Override
	public Long getOrganizationId() {
		return organizationId;
	}

	@Override
	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}
	
}
