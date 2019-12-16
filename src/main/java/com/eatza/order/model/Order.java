package com.eatza.order.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long customerId;
	private String status;
	private Long restaurantId;

	@CreationTimestamp
	private LocalDateTime createDateTime;
	@UpdateTimestamp
	private LocalDateTime updateDateTime;

	public Order(Long customerId, String status, Long restaurantId) {
		this.customerId = customerId;
		this.status = status;
		this.restaurantId = restaurantId;
	}
//
//	public Long getId() {
//		return id;
//	}
//
//	public void setId(Long id) {
//		this.id = id;
//	}
//
//	public Long getCustomerId() {
//		return customerId;
//	}
//
//	public void setCustomerId(Long customerId) {
//		this.customerId = customerId;
//	}
//
//	public String getStatus() {
//		return status;
//	}
//
//	public void setStatus(String status) {
//		this.status = status;
//	}
//
//	public Long getRestaurantId() {
//		return restaurantId;
//	}
//
//	public void setRestaurantId(Long restaurantId) {
//		this.restaurantId = restaurantId;
//	}
//
//	public LocalDateTime getCreateDateTime() {
//		return createDateTime;
//	}
//
//	public void setCreateDateTime(LocalDateTime createDateTime) {
//		this.createDateTime = createDateTime;
//	}
//
//	public LocalDateTime getUpdateDateTime() {
//		return updateDateTime;
//	}
//
//	public void setUpdateDateTime(LocalDateTime updateDateTime) {
//		this.updateDateTime = updateDateTime;
//	}
//
//	public Order() {
//	}

}
