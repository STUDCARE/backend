package com.studcare.data.jpa.repository;

import com.studcare.data.jpa.entity.HostelMaster;
import com.studcare.data.jpa.entity.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WardRepository extends JpaRepository<Ward, Long> {
	Optional<Ward> findByWardName(String wardName);
	Optional<Ward> findByHostelMaster(HostelMaster hostelMaster);
}
