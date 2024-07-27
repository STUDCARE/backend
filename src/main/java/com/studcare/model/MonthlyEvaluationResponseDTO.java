package com.studcare.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class MonthlyEvaluationResponseDTO {
	private Map<String, List<MonthlyEvaluationData>> evaluations;
	private UserDTO hostelMaster;
	private String ward;
}
