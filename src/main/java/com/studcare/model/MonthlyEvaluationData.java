package com.studcare.model;

import com.studcare.data.jpa.entity.GradingScale;
import jakarta.persistence.Column;
import lombok.Data;

@Data
public class MonthlyEvaluationData {
	private String behavioralData;
	private String extraNote;
	private String extracurricularActivities;
	private String healthData;
	private String sportData;
	private GradingScale extracurricularActivityGrade;
	private GradingScale sportGrade;
}