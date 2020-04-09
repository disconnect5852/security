package asd.tsanyi.security.dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import asd.tsanyi.security.enums.ResponseTypes;

public class InfoResponse {
	//make spring generate json response
	private ResponseTypes type;
	private String message;
	
	public InfoResponse(ResponseTypes type, String message) {
		this.type = type;
		this.message = message;
	}
	
	
	public InfoResponse() {
	}


	public static ResponseEntity<InfoResponse> createResponseEntity(ResponseTypes type, String message, HttpStatus httpStatus) {
		return new ResponseEntity<>(new InfoResponse(type,message), httpStatus);
	}
	
	public ResponseTypes getType() {
		return type;
	}

	public void setType(ResponseTypes type) {
		this.type = type;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
