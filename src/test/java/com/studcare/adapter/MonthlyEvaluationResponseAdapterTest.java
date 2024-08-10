package com.studcare.adapter;

import com.studcare.data.jpa.entity.GradingScale;
import com.studcare.data.jpa.entity.MonthlyEvaluation;
import com.studcare.model.MonthlyEvaluationData;
import com.studcare.model.MonthlyEvaluationResponseDTO;
import com.studcare.model.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {MonthlyEvaluationResponseAdapter.class})
class MonthlyEvaluationResponseAdapterTest {

	private final MonthlyEvaluationResponseAdapter monthlyEvaluationResponseAdapter = new MonthlyEvaluationResponseAdapter();

	@Test
	void adaptSuccess() {
		// Arrange
		List<MonthlyEvaluation> evaluations = new ArrayList<>();
		MonthlyEvaluation evaluation1 = new MonthlyEvaluation();
		evaluation1.setBehavioralData("Good behavior");
		evaluation1.setExtraNote("Needs improvement in punctuality");
		evaluation1.setExtracurricularActivities("Drama club");
		evaluation1.setHealthData("Healthy");
		evaluation1.setSportData("Soccer");
		evaluation1.setExtracurricularActivityGrade(GradingScale.EXCELLENT);
		evaluation1.setSportGrade(GradingScale.ABSENT);
		evaluation1.setEvaluationMonth("January");

		evaluations.add(evaluation1);

		UserDTO hostelMasterDTO = new UserDTO();
		hostelMasterDTO.setEmail("hostelmaster@gmail.com");

		String ward = "Ward A";

		// Act
		MonthlyEvaluationResponseDTO responseDTO = monthlyEvaluationResponseAdapter.adapt(evaluations, hostelMasterDTO, ward);

		// Assert
		Map<String, List<MonthlyEvaluationData>> expectedEvaluationMap = new HashMap<>();
		MonthlyEvaluationData expectedData1 = new MonthlyEvaluationData();
		expectedData1.setBehavioralData("Good behavior");
		expectedData1.setExtraNote("Needs improvement in punctuality");
		expectedData1.setExtracurricularActivities("Drama club");
		expectedData1.setHealthData("Healthy");
		expectedData1.setSportData("Soccer");
		expectedData1.setExtracurricularActivityGrade(GradingScale.EXCELLENT);
		expectedData1.setSportGrade(GradingScale.ABSENT);

		expectedEvaluationMap.computeIfAbsent("January", k -> new ArrayList<>()).add(expectedData1);

		assertEquals(expectedEvaluationMap, responseDTO.getEvaluations());
		assertEquals("hostelmaster@gmail.com", responseDTO.getHostelMaster().getEmail());
		assertEquals("Ward A", responseDTO.getWard());
	}
}
