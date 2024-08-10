package com.studcare.model;

import com.studcare.data.jpa.entity.GradingScale;
import lombok.Data;

@Data
public class MonthlyEvaluationRequestDTO {
	private String evaluationMonth;
	private String evaluationYear;
	private String behavioralData;
	private String extraNote;
	private String extracurricularActivities;
	private String healthData;
	private String wardName;
	private String sportData;
	private GradingScale extracurricularActivityGrade;
	private GradingScale sportGrade;
}