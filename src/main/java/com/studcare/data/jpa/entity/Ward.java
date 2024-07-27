package com.studcare.data.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "WARD")
public class Ward {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "WardID")
	private Long wardID;

	@Column(name = "WardName")
	private String wardName;

	@ManyToOne
	@JoinColumn(name = "HostelMasterID")
	private User hostelMaster;

	@CreationTimestamp
	private LocalDateTime createdTimestamp;

	@UpdateTimestamp
	private LocalDateTime modifiedTimestamp;
}
