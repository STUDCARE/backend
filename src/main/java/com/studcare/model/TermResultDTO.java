package com.studcare.model;

import lombok.Data;

@Data
public class TermResultDTO {
	private Long studentId;
	private String studentName;
	private Integer totalMarks;
	private Integer classRank;
}
