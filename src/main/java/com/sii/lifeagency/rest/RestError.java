package com.sii.lifeagency.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.sii.lifeagency.rest.RestError.Code;

@JsonInclude(Include.NON_NULL)
public class RestError {
	public static enum Code {
		UNAUTHORIZED(1), NOTFOUND(2), DUPLICATE(3), DECRIPTIONERROR(4), INVALID(5), UNKNOWN(999);
		private int code;
 
		private Code (int code) {
			this.code = code;
		}
		public int getCode() {
			return code;
		}
	}
	private Code code;
	private String message;

	public int getCode() {
		return code.getCode();
	}
	public RestError setCode(Code code) {
		this.code = code;
		return this;
	}
	public String getMessage() {
		return message;
	}
	public RestError setMessage(String message) {
		this.message = message;
		return this;
	}
	
	public static ResponseEntity<ResponseRest> getExceptionRest(Exception ex) {
		ResponseRest response = new ResponseRest();
		RestError error = new RestError();
		
		error.setCode(Code.UNKNOWN);
		error.setMessage(ex.getMessage());
		response.setError(error);
		return new ResponseEntity<ResponseRest>(response, HttpStatus.BAD_REQUEST);
	}
}
