package com.studcare.data.jpa.adaptor;

import com.studcare.data.jpa.entity.MonthlyEvaluation;
import com.studcare.model.MonthlyEvaluationRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class MonthlyEvaluationAdapter {
	public MonthlyEvaluation adapt(MonthlyEvaluationRequestDTO dto) {
		MonthlyEvaluation monthlyEvaluation = new MonthlyEvaluation();
		monthlyEvaluation.setEvaluationMonth(dto.getEvaluationMonth());
		monthlyEvaluation.setEvaluationYear(dto.getEvaluationYear());
		monthlyEvaluation.setBehavioralData(dto.getBehavioralData());
		monthlyEvaluation.setExtraNote(dto.getExtraNote());
		monthlyEvaluation.setExtracurricularActivities(dto.getExtracurricularActivities());
		monthlyEvaluation.setHealthData(dto.getHealthData());
		monthlyEvaluation.setSportData(dto.getSportData());
		monthlyEvaluation.setExtracurricularActivityGrade(dto.getExtracurricularActivityGrade());
		monthlyEvaluation.setSportGrade(dto.getSportGrade());
		return monthlyEvaluation;
	}
}