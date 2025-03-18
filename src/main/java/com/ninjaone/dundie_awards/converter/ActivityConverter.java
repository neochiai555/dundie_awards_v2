package com.ninjaone.dundie_awards.converter;

import org.springframework.core.convert.converter.Converter;

import com.ninjaone.dundie_awards.dto.ActivityDto;
import com.ninjaone.dundie_awards.model.Activity;

public class ActivityConverter implements Converter<Activity, ActivityDto> {

	@Override
	public ActivityDto convert(Activity source) {
		return new ActivityDto(source.getOccuredAt(), source.getEvent());
	}

}
