package com.studcare.model;

import lombok.Data;

import java.util.List;

@Data
public class TermResultsDTO {
	private String termNumber;
	private List<SubjectResultDTO> subjectResults;
	private String classRank;
	private String termNote;
}