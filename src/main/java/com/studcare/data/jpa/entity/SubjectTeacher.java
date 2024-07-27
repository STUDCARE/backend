package com.studcare.data.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serializable;

@Entity
@Data
@Table(name = "SUBJECT_TEACHER")
public class SubjectTeacher {
	@EmbeddedId
	private SubjectTeacherKey id;

	@ManyToOne
	@MapsId("subjectID")
	@JoinColumn(name = "SubjectID")
	private Subject subject;

	@ManyToOne
	@MapsId("teacherID")
	@JoinColumn(name = "TeacherID")
	private User teacher;

	@Embeddable
	@Data
	public static class SubjectTeacherKey implements Serializable {
		@Column(name = "SubjectID")
		private Long subjectID;

		@Column(name = "TeacherID")
		private Long teacherID;
	}
}