package com.ninjaone.dundie_awards.messaging.message;

import com.ninjaone.dundie_awards.dto.ActivityDto;
import com.ninjaone.dundie_awards.enums.ActivityMessageType;

/**
 * Activity message interface
 */
public interface ActivityMessage {

	ActivityDto getActivityDto();

	void setActivityDto(ActivityDto activityDto);

	ActivityMessageType getType();

	void setType(ActivityMessageType type);

	Long getOrganizationId();

	void setOrganizationId(Long organizationId);

}