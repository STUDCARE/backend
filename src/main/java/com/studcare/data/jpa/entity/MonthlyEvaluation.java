package com.studcare.data.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "MONTHLY_EVALUATION")
public class MonthlyEvaluation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MonthlyEvaluationID")
	private Long monthlyEvaluationId;

	@ManyToOne
	@JoinColumn(name = "StudentID")
	private Student student;

	@Column(name = "EvaluationMonth")
	private String evaluationMonth;

	@Column(name = "EvaluationYear")
	private String evaluationYear;

	@Column(name = "BehavioralData")
	private String behavioralData;

	@Column(name = "ExtraNote")
	private String extraNote;

	@Column(name = "ExtracurricularActivities")
	private String extracurricularActivities;

	@Column(name = "SportData")
	private String sportData;

	@Column(name = "ExtracurricularActivitiesGrade")
	private GradingScale extracurricularActivityGrade;

	@Column(name = "sportGrade")
	private GradingScale sportGrade;

	@Column(name = "HealthData")
	private String healthData;

	@ManyToOne
	@JoinColumn(name = "HostelMasterID")
	private User hostelMasterId;
	@ManyToOne
	@JoinColumn(name = "WardID")
	private Ward wardId;

	@CreationTimestamp
	private LocalDateTime createdTimestamp;

	@UpdateTimestamp
	private LocalDateTime modifiedTimestamp;
}