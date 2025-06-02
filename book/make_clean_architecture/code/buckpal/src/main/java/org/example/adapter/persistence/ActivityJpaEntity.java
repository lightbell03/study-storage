package org.example.adapter.persistence;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "activity")
@NoArgsConstructor
@AllArgsConstructor
public class ActivityJpaEntity {
	@Id
	@GeneratedValue
	private Long id;

	@Column
	private Long ownerAccountId;
	@Column
	private Long sourceAccountId;
	@Column
	private Long targetAccountId;
	@Column
	private Long amount;
	@Column
	private LocalDateTime timestamp;
}
