package com.studcare.data.jpa.dto;

import com.studcare.model.UserDTO;
import lombok.Data;

@Data
public class WardDTO {
	private String wardName;
	private UserDTO hostelMaster;
}
