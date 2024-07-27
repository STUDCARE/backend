package com.studcare.data.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "SCHOOL_CLASS")
public class SchoolClass {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ClassID")
	private Long classID;

	@Column(name = "ClassName")
	private String className;

	@OneToOne
	@JoinColumn(name = "ClassTeacherID", unique = true)
	private User classTeacher;

	@ManyToMany
	@JoinTable(
			name = "CLASS_SUBJECT",
			joinColumns = @JoinColumn(name = "ClassID"),
			inverseJoinColumns = @JoinColumn(name = "SubjectID")
	)
	private List<Subject> subjects;

	@CreationTimestamp
	private LocalDateTime createdTimestamp;

	@UpdateTimestamp
	private LocalDateTime modifiedTimestamp;
}