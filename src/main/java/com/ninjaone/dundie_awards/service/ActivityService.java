package com.ninjaone.dundie_awards.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ninjaone.dundie_awards.model.Activity;
import com.ninjaone.dundie_awards.repository.ActivityRepository;

/**
 * Activity service 
 */
@Service
public class ActivityService {
	private final ActivityRepository activityRepository;
	
	public ActivityService(ActivityRepository activityRepository) {
		this.activityRepository = activityRepository;
	}
	
	@Transactional
	public Activity save(Activity activity) {
		return activityRepository.save(activity);
	}
}