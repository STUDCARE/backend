package com.studcare.data.jpa.repository;

import com.studcare.data.jpa.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
	boolean existsBySubjectName(String subjects);
	Optional<Subject> findBySubjectName(String subjectName);
}
