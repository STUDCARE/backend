package com.studcare.data.jpa.adaptor;

import com.studcare.data.jpa.dto.SubjectDTO;
import com.studcare.data.jpa.entity.Subject;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SubjectAdapter {

	public SubjectDTO adapt(Subject subject) {
		if (subject == null) {
			return null;
		}
		SubjectDTO subjectDTO = new SubjectDTO();
		subjectDTO.setSubjectId(subject.getSubjectID());
		subjectDTO.setSubjectName(subject.getSubjectName());
		return subjectDTO;
	}

	public Subject adapt(SubjectDTO subjectDTO) {
		if (subjectDTO == null) {
			return null;
		}
		Subject subject = new Subject();
		subject.setSubjectID(subjectDTO.getSubjectId());
		subject.setSubjectName(subjectDTO.getSubjectName());
		return subject;
	}

	public List<SubjectDTO> adaptList(List<Subject> subjects) {
		if (subjects == null) {
			return null;
		}
		return subjects.stream().map(this::adapt).collect(Collectors.toList());
	}
}