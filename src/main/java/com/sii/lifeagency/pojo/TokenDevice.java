package com.sii.lifeagency.pojo;

import java.time.LocalDateTime;

public class TokenDevice {
	private String token;
	private LocalDateTime lastValidPush;
	private Integer lastSequenceFailsAttempts;
	
	public TokenDevice(String token) {
		this.token = token;
		this.lastSequenceFailsAttempts = 0;
		this.lastValidPush = LocalDateTime.now();
	}
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public LocalDateTime getLastValidPush() {
		return lastValidPush;
	}

	public void setLastValidPush(LocalDateTime lastValidPush) {
		this.lastValidPush = lastValidPush;
	}

	public Integer getLastSequenceFailsAttempts() {
		return lastSequenceFailsAttempts;
	}

	public void setLastSequenceFailsAttempts(Integer lastSequenceFailsAttempts) {
		this.lastSequenceFailsAttempts = lastSequenceFailsAttempts;
	}
	
	public void addFailtAttemps() {
		this.lastSequenceFailsAttempts++;
	}
	
	public void updateValidPush() {
		lastValidPush = LocalDateTime.now();
		lastSequenceFailsAttempts = 0;
	}

	@Override
	public int hashCode() {
	    return token.hashCode();
	}

	@Override
	public boolean equals(Object me) {
		return ((TokenDevice)me).token.equals(this.token);
	}
}
