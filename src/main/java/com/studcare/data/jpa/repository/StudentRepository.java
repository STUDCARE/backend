package com.studcare.data.jpa.repository;

import com.studcare.data.jpa.entity.SchoolClass;
import com.studcare.data.jpa.entity.Student;
import com.studcare.data.jpa.entity.User;
import com.studcare.data.jpa.entity.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
	List<Student> findByUser_EmailIn(List<String> emails);
	List<Student> findBySchoolClass(SchoolClass schoolClass);
	List<Student> findByWard(Ward ward);
	Optional<Student> findByUser_Email(String email);
	@Query("SELECT s.ward.hostelMaster FROM Student s WHERE s.studentId = :studentId")
	Optional<User> findHostelMasterByStudentId(@Param("studentId") Long studentId);
}