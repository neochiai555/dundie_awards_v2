package com.ninjaone.dundie_awards.dto;

import java.time.LocalDateTime;

/**
 * Activity DTO class
 */
public class ActivityDto {

    private long id;
    private LocalDateTime occuredAt;
    private String event;

    public ActivityDto() {

    }

    public ActivityDto(LocalDateTime localDateTime, String event) {
        super();
        this.occuredAt = localDateTime;
        this.event = event;
    }

    public Long getId() {
    	return id;
    }

	public void setId(long id) {
		this.id = id;
	}

    public String getEvent() {
        return event;
    }

	public void setEvent(String event) {
		this.event = event;
	}
	
	public void setOccuredAt(LocalDateTime occuredAt) {
		this.occuredAt = occuredAt;
	}

    public LocalDateTime getOccuredAt() {
        return occuredAt;
    }
}
