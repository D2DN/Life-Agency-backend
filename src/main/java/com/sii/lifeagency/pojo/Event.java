package com.sii.lifeagency.pojo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.data.annotation.Id;

//@JsonInclude(Include.NON_NULL)
public class Event {
	@Id
	private String id;
	private String title;
	private String description;
	private LocalDateTime date;
	private EventType eventType;
	private Image imgEvent;
	public Image getImgEvent() {
		return imgEvent;
	}
	public void setImgEvent(Image imgEvent) {
		this.imgEvent = imgEvent;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDate() {
		return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}
	public void setDate(String date) {
		this.date = LocalDateTime.parse(date);
	}
	public EventType getEventType() {
		return eventType;
	}
	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}
}
