package com.studcare.model;

import com.studcare.constants.Status;
import lombok.Data;

@Data
public class UserDeletionResponseDTO {
	private Status responseCode;
	private String message;
	private String errorCode;
}
