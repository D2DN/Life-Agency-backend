package com.sii.lifeagency.pojo;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class EventType {
	@Id
	private String id;
	private String name;
	private Image banner;
	public String getId() {
		return id;
	}
	public EventType setId(String id) {
		this.id = id;
		return this;
	}
	public String getName() {
		return name;
	}
	public EventType setName(String name) {
		this.name = name;
		return this;
	}
	public Image getBanner() {
		return banner;
	}
	public EventType setBanner(Image banner) {
		this.banner = banner;
		return this;
	}
}
