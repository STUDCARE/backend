package com.studcare.model;

import com.studcare.data.jpa.entity.User;
import lombok.Data;

import java.util.Map;

@Data
public class MonthlyEvaluationResponseDTO {
	private Map<String, MonthlyEvaluationData> evaluations;
	private UserDTO hostelMaster;
	private String ward;
}
