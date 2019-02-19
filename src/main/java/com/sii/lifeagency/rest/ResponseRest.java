package com.sii.lifeagency.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ResponseRest {
	private Object data;
	private RestError error;
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public RestError getError() {
		return error;
	}
	public void setError(RestError error) {
		this.error = error;
	}

}
