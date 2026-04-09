package com.fahrul.threatcase.gs.be.common.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {

	@Column(updatable = false,name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	// AUTO INSERT
	@PrePersist
	public void onCreate() {

		createdAt = LocalDateTime.now();
		updatedAt = LocalDateTime.now();
	}

	// AUTO UPDATE
	@PreUpdate
	public void onUpdate() {

		updatedAt = LocalDateTime.now();
	}

}
