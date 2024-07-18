package com.studcare.model;

import com.studcare.constants.Status;
import lombok.Data;

import java.util.Map;

@Data
public class UserRegisterResponseDTO {
	private Status responseCode;
	private String message;
	private String errorCode;
}
