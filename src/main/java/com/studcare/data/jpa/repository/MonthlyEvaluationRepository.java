package com.studcare.data.jpa.repository;

import com.studcare.data.jpa.entity.MonthlyEvaluation;
import com.studcare.data.jpa.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonthlyEvaluationRepository extends JpaRepository<MonthlyEvaluation, Long> {
	List<MonthlyEvaluation> findByStudentAndEvaluationMonthIn(Student student, List<String> months);
	List<MonthlyEvaluation> findByStudent(Student student);
}