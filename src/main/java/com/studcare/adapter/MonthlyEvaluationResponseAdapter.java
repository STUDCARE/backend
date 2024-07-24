package com.studcare.adapter;

import com.studcare.data.jpa.entity.MonthlyEvaluation;
import com.studcare.model.MonthlyEvaluationData;

import com.studcare.model.MonthlyEvaluationResponseDTO;

import com.studcare.model.UserDTO;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MonthlyEvaluationResponseAdapter {

	public MonthlyEvaluationResponseDTO adapt(List<MonthlyEvaluation> evaluations, UserDTO hostelMasterDTO, String ward) {
		MonthlyEvaluationResponseDTO responseDTO = new MonthlyEvaluationResponseDTO();
		Map<String, MonthlyEvaluationData> evaluationMap = new HashMap<>();

		for (MonthlyEvaluation evaluation : evaluations) {
			MonthlyEvaluationData data = new MonthlyEvaluationData();
			data.setBehavioralData(evaluation.getBehavioralData());
			data.setExtraNote(evaluation.getExtraNote());
			data.setExtracurricularActivities(evaluation.getExtracurricularActivities());
			data.setHealthData(evaluation.getHealthData());

			evaluationMap.put(evaluation.getEvaluationMonth(), data);
		}

		responseDTO.setEvaluations(evaluationMap);
		responseDTO.setHostelMaster(hostelMasterDTO);
		responseDTO.setWard(ward);
		return responseDTO;
	}
}