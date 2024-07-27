package com.studcare.data.jpa.repository;

import com.studcare.data.jpa.entity.StudentSubjectEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentSubjectEnrollmentRepository extends JpaRepository<StudentSubjectEnrollment, StudentSubjectEnrollment.StudentSubjectEnrollmentKey> {}
