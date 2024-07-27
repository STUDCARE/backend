package com.studcare.data.jpa.repository;

import com.studcare.data.jpa.entity.Subject;
import com.studcare.data.jpa.entity.SubjectTeacher;
import com.studcare.data.jpa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectTeacherRepository extends JpaRepository<SubjectTeacher, SubjectTeacher.SubjectTeacherKey> {
	boolean existsBySubjectAndTeacher(Subject subject, User teacher);
	List<SubjectTeacher> findByTeacher(User teacher);
	List<SubjectTeacher> findBySubject(Subject subject);
}