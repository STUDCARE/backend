package com.studcare.model;

import com.studcare.constants.Status;
import lombok.Data;

import java.util.List;

@Data
public class ResponseDTO {
	private Status responseCode;
	private String message;
	private String errorCode;
	private List Data;
}
